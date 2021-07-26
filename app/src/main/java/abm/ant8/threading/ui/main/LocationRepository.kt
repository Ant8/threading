package abm.ant8.threading.ui.main

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface LocationRepository {
    data class Location(val latitude: Double, val longitude: Double)

    suspend fun getLocation(): Location
}

class LocationRepositoryImpl @Inject constructor(context: Context) : LocationRepository {
    //    we assume google play services are present on host
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getLocation(): LocationRepository.Location =
        try {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_LOW_POWER,
                cancellationTokenSource.token
            ).await()
                .let { LocationRepository.Location(it.latitude, it.longitude) }
        } catch (e: SecurityException) {
            throw e
        }
}
