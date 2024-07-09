package kr.toru.lmwnassignment.data.usecase

import kr.toru.lmwnassignment.data.response.CoinResponse

interface GetCoinsUseCase {
    suspend fun getCoins(offset: Int): Result<CoinResponse>
}