package kr.toru.lmwnassignment.data.usecase.impl

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.response.CoinResponse
import kr.toru.lmwnassignment.data.usecase.GetCoinsUseCase
import kr.toru.lmwnassignment.network.ApiService
import javax.inject.Inject

class GetCoinsUseCaseImpl @Inject constructor(
    private val apiService: ApiService
): GetCoinsUseCase {
    override suspend fun getCoins(): Result<CoinResponse> = runCatching {
        apiService.getCoins().body<CoinResponse>()
    }
}