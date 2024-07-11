package kr.toru.lmwnassignment.data.usecase.impl

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.response.CoinDetailResponse
import kr.toru.lmwnassignment.data.usecase.GetCoinDetailUseCase
import kr.toru.lmwnassignment.network.ApiService
import javax.inject.Inject

class GetCoinDetailUseCaseImpl @Inject constructor(
    private val apiService: ApiService
): GetCoinDetailUseCase {
    override suspend fun getCoinDetail(uuid: String): Result<CoinDetailResponse> = runCatching {
        apiService.getCoinDetail(uuid).body<CoinDetailResponse>()
    }
}