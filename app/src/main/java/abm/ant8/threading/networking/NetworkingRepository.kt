package abm.ant8.threading.networking

interface NetworkingRepository {
    suspend fun send(url: String, results: List<String>)
}

class NetworkingRepositoryImpl(private val api: NetworkingApi) : NetworkingRepository {
    override suspend fun send(url: String, results: List<String>) =
        try {
            api.send(url, RequestBody(results))
        } catch (e: Exception) { }
}