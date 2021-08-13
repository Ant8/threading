package abm.ant8.threading.update

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability.*
import io.reactivex.Single

interface UpdateRepo {
    fun getUpdateStatus(): Single<String>
}

class UpdateRepoImpl(private val context: Context) : UpdateRepo {
    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(context)
    }

    override fun getUpdateStatus(): Single<String> =
        appUpdateManager.appUpdateInfo.toSingle()
            .map { it.updateAvailability().mapUpdateAvailability() }

    private fun Int.mapUpdateAvailability() =
        when (this) {
                DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> "DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS"

                UNKNOWN -> "UNKNOWN"

                UPDATE_AVAILABLE -> "UPDATE_AVAILABLE"

                UPDATE_NOT_AVAILABLE -> "UPDATE_NOT_AVAILABLE"

            else -> ""
        }
}

