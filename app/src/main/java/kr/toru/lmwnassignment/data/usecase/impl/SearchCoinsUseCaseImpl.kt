package kr.toru.lmwnassignment.data.usecase.impl

import io.ktor.client.call.body
import kr.toru.lmwnassignment.data.response.CoinSuggestionResponse
import kr.toru.lmwnassignment.data.usecase.SearchCoinsUseCase
import kr.toru.lmwnassignment.network.ApiService
import javax.inject.Inject

class SearchCoinsUseCaseImpl @Inject constructor (
    private val apiService: ApiService
): SearchCoinsUseCase {
    override suspend fun searchCoins(query: String): Result<CoinSuggestionResponse> = runCatching {
        apiService.searchCoin(query).body<CoinSuggestionResponse>()
    }
}