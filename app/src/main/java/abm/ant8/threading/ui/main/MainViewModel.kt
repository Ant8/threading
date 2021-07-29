package abm.ant8.threading.ui.main

import abm.ant8.threading.battery.BatteryRepository
import abm.ant8.threading.location.LocationRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@ObsoleteCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val locationRepository: LocationRepository,
    private val batteryRepository: BatteryRepository,
) : ViewModel() {

    private var locationJob: Job? = null
    private var batteryJob: Job? = null

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

    fun startThreads(locationInterval: Int, batteryInterval: Int) {
        Log.d(TAG, "should start threads")
        startLocationJob(locationInterval)
        startBatteryJob(batteryInterval)
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

                    Log.d(TAG, locationRepository.getLocation().toString())

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

                    Log.d(TAG, batteryRepository.getBatteryLevel().toString())

                    delay(intervalInSeconds * 1000L)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}