package kr.toru.lmwnassignment.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kr.toru.lmwnassignment.data.usecase.GetCoinDetailUseCase
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow

    suspend fun getCoinDetail(uuid: String) {
        Event.Loading.emitEvent()

        getCoinDetailUseCase.getCoinDetail(uuid = uuid).onSuccess {
            it.let { response ->
                Event.Success(
                    data = DetailBottomSheetViewData(
                        iconUrl = response.data.coin.iconUrl,
                        coinSymbolName = response.data.coin.symbol,
                        coinName = response.data.coin.name,
                        coinDescription = response.data.coin.description,
                        marketCap = formatNumber(response.data.coin.marketCap),
                        price = getDecimalFormat(response.data.coin.price),
                        websiteUrl = response.data.coin.websiteUrl
                    )
                ).emitEvent()

                Event.Loaded.emitEvent()
            }.run {
                Event.Loaded.emitEvent()
            }
        }.onFailure {
            Event.Loaded.emitEvent()
            Event.Failure.emitEvent()
        }
    }

    private suspend fun Event.emitEvent() {
        _eventFlow.emit(this)
    }

    private fun getDecimalFormat(number: Double): String {
        val decimalFormat = DecimalFormat("#,###.###").apply {
            roundingMode = RoundingMode.DOWN
        }
        return decimalFormat.format(number)
    }

    private fun formatNumber(number: Long): String {
        if (number < 1000) return number.toString()

        val exp = floor(log10(number.toDouble()) / 3).toInt()
        val value = number / 10.0.pow(exp * 3.0)

        val roundedValue = when {
            value < 10 -> "%.2f".format(value)
            value < 100 -> "%.1f".format(value)
            else -> "%.0f".format(value)
        }

        return when (exp) {
            2 -> "$roundedValue Million"
            3 -> "$roundedValue Billion"
            4 -> "$roundedValue Trillion"
            else -> number.toString()
        }
    }


    sealed interface Event {
        data class Success(
            val data: DetailBottomSheetViewData,
        ): Event

        data object Loading: Event
        data object Loaded: Event

        data object Failure: Event
    }

    data class DetailBottomSheetViewData(
        val iconUrl: String = "",
        val coinSymbolName: String = "",
        val coinName: String = "",
        val coinDescription: String = "",
        val marketCap: String = "",
        val price: String = "",
        val websiteUrl: String = ""
    )
}