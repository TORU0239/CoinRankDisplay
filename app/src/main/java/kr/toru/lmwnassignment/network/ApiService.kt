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

    suspend fun getCoins() = apiCall(BASE_URL + COINS_API)

    suspend fun getCoinDetail(coinUUID: String) = httpClient.get("$BASE_URL$COIN_API/$coinUUID") {
        headers {
            append("Content-Type", CONTENT_TYPE)
            append("x-access-token", API_KEY)
        }
    }

    suspend fun searchCoin(query: String) = httpClient.get(BASE_URL + SEARCH_API) {
        headers {
            append("Content-Type", CONTENT_TYPE)
            append("x-access-token", API_KEY)
        }
        url {
            parameters.append("query", query)
        }
    }
}