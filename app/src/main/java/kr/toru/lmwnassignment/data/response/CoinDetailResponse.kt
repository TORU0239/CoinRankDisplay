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
    val volumeFor24h: String? = "",
    val allTimeHigh: AllTimeHigh? = AllTimeHigh(),
    val btcPrice: String? = "",
    val change: String? = "",
    val coinrankingUrl: String? = "",
    val color: String? = "",
    val contractAddresses: List<String?>? = listOf(),
    val description: String? = "",
    val fullyDilutedMarketCap: String? = "",
    val hasContent: Boolean?,
    val iconUrl: String? = "",
    val links: List<Link?>? = listOf(),
    val listedAt: Int? = 0,
    val lowVolume: Boolean? = false,
    val marketCap: Long? = 0L,
    val name: String? = "",
    val notices: String? = "",
    val numberOfExchanges: Int? = -1,
    val numberOfMarkets: Int? = -1,
    val price: Double? = 0.0,
    val priceAt: Int? = -1,
    val rank: Int? = -1,
    val sparkline: (List<String?>)? = null,
    val supply: Supply? = Supply(),
    val symbol: String? = "",
    val tags: List<String?>? = listOf(),
    val tier: Int? = -1,
    val uuid: String? = "",
    val websiteUrl: String? = ""
)

@Serializable
data class AllTimeHigh(
    val price: String? = "",
    val timestamp: Int? = 0
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