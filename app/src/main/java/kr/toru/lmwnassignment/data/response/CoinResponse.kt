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
    val uuid: String,
    val symbol: String,
    val name: String,
    val color: String,
    val iconUrl: String,
    val marketCap: String,
    val price: String,
    val listedAt: Long,
    val tier: Int,
    val change: String,
    val rank: Int,
    val sparkline: List<String?>? = listOf(),
    val lowVolume: Boolean,
    val coinrankingUrl: String,
    @SerialName("24hVolume")
    val volumeFor24h: String,
    val btcPrice: String,
    val contractAddresses: List<String>,
)

