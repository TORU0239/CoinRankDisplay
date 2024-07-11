package kr.toru.lmwnassignment.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kr.toru.lmwnassignment.data.usecase.GetCoinDetailUseCase
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase
): ViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow

    suspend fun getCoinDetail(uuid: String) {
        runCatching {
            getCoinDetailUseCase.getCoinDetail(uuid = uuid)
        }.onSuccess {

            it.getOrThrow().let { response ->
                Event.Success(
                    data = DetailBottomSheetViewData(
                        iconUrl = response.data.coin.iconUrl,
                        coinSymbolName = response.data.coin.symbol,
                        coinName = response.data.coin.name,
                        coinDescription = response.data.coin.description,
                    )
                ).emitEvent()
            }
        }.onFailure {
            Event.Failure.emitEvent()
        }
    }

    private suspend fun Event.emitEvent() {
        _eventFlow.emit(this)
    }


    sealed interface Event {
        data class Success(
            val data: DetailBottomSheetViewData,
        ): Event

        data object Failure: Event
    }

    data class DetailBottomSheetViewData(
        val iconUrl: String = "",
        val coinSymbolName: String = "",
        val coinName: String = "",
        val coinDescription: String = "",
    )
}