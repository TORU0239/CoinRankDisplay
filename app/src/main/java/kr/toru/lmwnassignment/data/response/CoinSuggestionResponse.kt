package kr.toru.lmwnassignment.data.response

import kotlinx.serialization.Serializable

@Serializable
data class CoinSuggestionResponse(
    val status: String,
    val data: SuggestedCoins,
)

@Serializable
data class SuggestedCoins(
    val coins: List<SuggestedCoinResponse>,
)

@Serializable
data class SuggestedCoinResponse(
    val uuid: String,
    val iconUrl: String,
    val name: String,
    val symbol: String,
    val price: String? = null,
)