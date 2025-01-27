package kr.toru.lmwnassignment.data.repository

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.response.CoinDetailResponse
import kr.toru.lmwnassignment.data.response.CoinResponse
import kr.toru.lmwnassignment.data.response.CoinSuggestionResponse
import kr.toru.lmwnassignment.network.ApiService

@Deprecated("This class is deprecated, use UseCases for your purposes instead")
class RemoteRepository(
    private val apiService: ApiService
) {
    suspend fun getCoins(): Result<CoinResponse> =
        runCatching {
            apiService.getCoins().body<CoinResponse>()
        }

    suspend fun getCoinDetail(name: String): Result<CoinDetailResponse> =
        runCatching {
            apiService.getCoinDetail(name).body<CoinDetailResponse>()
        }

    suspend fun searchCoin(query: String): Result<CoinSuggestionResponse> =
        runCatching {
            apiService.searchCoin(query).body<CoinSuggestionResponse>()
        }
}