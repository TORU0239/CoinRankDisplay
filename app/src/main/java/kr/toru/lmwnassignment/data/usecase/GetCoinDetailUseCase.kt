package kr.toru.lmwnassignment.data.usecase

import kr.toru.lmwnassignment.data.response.CoinDetailResponse

interface GetCoinDetailUseCase {
    suspend fun getCoinDetail(uuid: String): Result<CoinDetailResponse>
}