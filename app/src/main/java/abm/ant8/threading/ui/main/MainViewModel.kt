package abm.ant8.threading.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var threadsStarted = false

    val requirePermissionsLiveData: LiveData<Unit> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()

        threadsStarted = false
    }

    fun checkThreadsPrerequisites() {
        (requirePermissionsLiveData as MutableLiveData).postValue(Unit)
    }

    fun startThreads() {
        Log.d(TAG, "threads actually started")
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}