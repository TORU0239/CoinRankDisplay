package kr.toru.lmwnassignment.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers

class ApiService(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.coinranking.com/v2/"
        private const val SEARCH_API ="search-suggestions"
        private const val COIN_API ="coin"
        private const val COINS_API ="coins"

        private const val CONTENT_TYPE = "application/json"
        private const val API_KEY = "coinranking98dfe01c963460fae732ec0fd3db131129391b1e50a712de"
    }

    private suspend fun apiCall(
        urlString: String,
        parameterMap: Map<String, String>? = null
    ) = httpClient.get(urlString) {
        headers {
            append("Content-Type", CONTENT_TYPE)
            append("x-access-token", API_KEY)
        }

        if (parameterMap != null) {
            url {
                parameterMap.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
        }
    }

    suspend fun getCoins() = apiCall(
        urlString = BASE_URL + COINS_API,
        parameterMap = mapOf("limit" to "20")
    )

    suspend fun getCoinDetail(coinUUID: String) = apiCall("$BASE_URL$COIN_API/$coinUUID")

    suspend fun searchCoin(query: String) = apiCall(
        BASE_URL + SEARCH_API,
        mapOf("query" to query)
    )

}