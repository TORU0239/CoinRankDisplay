package kr.toru.lmwnassignment.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinResponse(
    val status: String,
    val data: CoinDataResponse,
)

@Serializable
data class CoinDataResponse(
    val coins: List<CoinInfoResponse>,
)

@Serializable
data class CoinInfoResponse(
    val uuid: String? = null,
    val symbol: String? = null,
    val name: String? = null,
    val color: String? = null,
    val iconUrl: String? = null,
    val marketCap: String? = null,
    val price: String? = null,
    val listedAt: Long? = null,
    val tier: Int? = null,
    val change: String? = null,
    val rank: Int = -1,
    val sparkline: List<String?>? = listOf(),
    val lowVolume: Boolean? = null,
    val coinrankingUrl: String? = null,
    @SerialName("24hVolume")
    val volumeFor24h: String? = null,
    val btcPrice: String? = null,
    val contractAddresses: List<String>? = listOf(),
)

