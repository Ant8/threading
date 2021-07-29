package abm.ant8.threading.networking

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface NetworkingApi {
    @POST
    suspend fun send(@Url url: String, @Body results: RequestBody)
}