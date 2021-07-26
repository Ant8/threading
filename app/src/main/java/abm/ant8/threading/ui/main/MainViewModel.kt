package abm.ant8.threading.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
class MainViewModel : ViewModel() {

    private var locationJob: Job? = null

    val requirePermissionsLiveData: LiveData<Unit> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()

        locationJob?.cancel()
        locationJob = null
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

                    delay(3000)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}