package abm.ant8.threading.ui.main

import abm.ant8.threading.battery.BatteryRepository
import abm.ant8.threading.location.LocationRepository
import abm.ant8.threading.networking.NetworkingRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject

@ObsoleteCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val locationRepository: LocationRepository,
    private val batteryRepository: BatteryRepository,
    private val networkingRepository: NetworkingRepository,
) : ViewModel() {

    private var locationJob: Job? = null
    private var batteryJob: Job? = null
    private var resultsSendingJob: Job? = null
    private var resultsCollectingActor: SendChannel<String>? = null

    val requirePermissionsLiveData: LiveData<Unit> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()

        stopThreads()
    }

    fun checkThreadsPrerequisites() {
        Log.d(TAG, "checkThreadsPrerequisites")
        if (locationJob == null) {
            (requirePermissionsLiveData as MutableLiveData).postValue(Unit)
        }
    }

    fun startThreads(
        locationInterval: Int,
        batteryInterval: Int,
        queueCapacity: Int,
        url: String
    ) {
        Log.d(TAG, "should start threads")
        startLocationJob(locationInterval)
        startBatteryJob(batteryInterval)
        startCollectingResults(queueCapacity, url)
    }

    fun stopThreads() {
        Log.d(TAG, "should stop threads")
        locationJob?.cancel()
        locationJob = null
        batteryJob?.cancel()
        batteryJob = null
    }

    private fun startLocationJob(intervalInSeconds: Int) {
        if (locationJob == null) {
            locationJob = viewModelScope.launch(newSingleThreadContext("T1 - location")) {
                while (isActive) {
                    Log.d(TAG, "should poll last known position")

                    locationRepository.getLocation().toString().let {
                        Log.d(TAG, it)
                        resultsCollectingActor?.send(it)
                    }

                    delay(intervalInSeconds * 1000L)
                }
            }
        }
    }

    private fun startBatteryJob(intervalInSeconds: Int) {
        if (batteryJob == null) {
            batteryJob = viewModelScope.launch(newSingleThreadContext("T2 - battery")) {
                while (isActive) {
                    Log.d(TAG, "should poll last known battery state")

                    batteryRepository.getBatteryLevel().toString().let {
                        Log.d(TAG, it)
                        resultsCollectingActor?.send(it)
                    }

                    delay(intervalInSeconds * 1000L)
                }
            }
        }
    }

    private fun startCollectingResults(capacity: Int, url: String) {
        val context = newSingleThreadContext("T3 - collecting")

        resultsCollectingActor = viewModelScope.actor(context = context, capacity = capacity) {
            val results = ArrayList<String>(capacity)
            for (msg in channel) {
                results += msg
                if (results.size == capacity) {
                    Log.d(TAG, "should send the results")
                    networkingRepository.send(url, results)
                    results.clear()
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}