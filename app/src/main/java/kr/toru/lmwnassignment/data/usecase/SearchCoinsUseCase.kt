package kr.toru.lmwnassignment.data.usecase

import kr.toru.lmwnassignment.data.response.CoinSuggestionResponse

interface SearchCoinsUseCase {
    suspend fun searchCoins(query: String): Result<CoinSuggestionResponse>
}