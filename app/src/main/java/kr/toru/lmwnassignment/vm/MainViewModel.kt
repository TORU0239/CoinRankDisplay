package kr.toru.lmwnassignment.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.data.usecase.GetCoinsUseCase
import kr.toru.lmwnassignment.presentation.adapter.ItemViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
): ViewModel() {

    private val _outputEventFlow = MutableSharedFlow<Event>()
    val outputEventFlow = _outputEventFlow

    suspend fun getCoins() {
        viewModelScope.launch {
            Event.Loading().emitEvent()
            getCoinsUseCase.getCoins()
                .onSuccess {
                    Event.Success(data = it.data.coins).emitEvent()
                    Event.Loading(isLoading = false).emitEvent()

                }
                .onFailure {
                    Event.Loading(isLoading = false).emitEvent()
                    Event.Failure(
                        listOf(
                            ItemViewModel.LoadFailureItemViewModel {
                                viewModelScope.launch {
                                    getCoins()
                                }
                            }
                        )
                    ).emitEvent()
                }
        }
    }

    private suspend fun Event.emitEvent() {
        _outputEventFlow.emit(this)
    }


    sealed interface Event {
        data class Loading(
            val isLoading: Boolean = true
        ) : Event

        data class Success(
            val data: List<CoinInfoResponse>
        ): Event

        data class Failure(
            val data: List<ItemViewModel.LoadFailureItemViewModel>
        ): Event
    }
}