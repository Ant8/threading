package abm.ant8.threading.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    fun checkThreadsPrerequisites() {
        Log.d(TAG, "started threads")
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}