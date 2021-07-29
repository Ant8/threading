package abm.ant8.threading.networking

import com.squareup.moshi.Json

data class RequestBody(@field:Json(name = "results") val results: List<String>)
