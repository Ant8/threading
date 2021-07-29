package abm.ant8.threading.ui.main

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
class MainViewModel @Inject constructor() : ViewModel() {

    private var locationJob: Job? = null

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

    fun startThreads() {
        Log.d(TAG, "should start threads")
        if (locationJob == null) {
            locationJob = viewModelScope.launch(newSingleThreadContext("T1 - location")) {
                while (isActive) {
                    Log.d(TAG, "should poll last known position")

//                    Log.d(TAG, locationRepository.getLocation().toString())

                    delay(3000)
                }
            }
        }
    }

    fun stopThreads() {
        locationJob?.cancel()
        locationJob = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}