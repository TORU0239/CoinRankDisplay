package kr.toru.lmwnassignment.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailResponse(
    val status: String,
    val data: CoinDetailWrapperResponse
)

@Serializable
data class CoinDetailWrapperResponse (
    val coin: CoinDetailInfoResponse,
)

@Serializable
data class CoinDetailInfoResponse(
    @SerialName("24hVolume")
    val volumeFor24h: String,
    val allTimeHigh: AllTimeHigh,
    val btcPrice: String,
    val change: String,
    val coinrankingUrl: String,
    val color: String,
    val contractAddresses: List<String>,
    val description: String,
    val fullyDilutedMarketCap: String,
    val hasContent: Boolean,
    val iconUrl: String,
    val links: List<Link>,
    val listedAt: Int,
    val lowVolume: Boolean,
    val marketCap: String,
    val name: String,
    val notices: String? = "",
    val numberOfExchanges: Int,
    val numberOfMarkets: Int,
    val price: String,
    val priceAt: Int,
    val rank: Int,
    val sparkline: List<String>,
    val supply: Supply,
    val symbol: String,
    val tags: List<String>,
    val tier: Int,
    val uuid: String,
    val websiteUrl: String
)

@Serializable
data class AllTimeHigh(
    val price: String,
    val timestamp: Int
)

@Serializable
data class Link(
    val name: String,
    val type: String,
    val url: String
)

@Serializable
data class Supply(
    val confirmed: Boolean? = false,
    val supplyAt: Int? = -1,
    val max: String? = "",
    val total: String? = "",
    val circulating: String? = "",
)