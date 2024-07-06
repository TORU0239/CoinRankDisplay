package kr.toru.lmwnassignment.data.repository

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.response.CoinDetailResponse
import kr.toru.lmwnassignment.data.response.CoinResponse
import kr.toru.lmwnassignment.network.ApiService

class RemoteRepository(
    private val apiService: ApiService
) {
    suspend fun getCoins(): Result<CoinResponse> =
        kotlin.runCatching {
            apiService.getCoins().body<CoinResponse>()
        }

    suspend fun getCoinDetail(name: String): Result<CoinDetailResponse> =
        runCatching {
            apiService.getCoinDetail(name).body<CoinDetailResponse>()
        }
}