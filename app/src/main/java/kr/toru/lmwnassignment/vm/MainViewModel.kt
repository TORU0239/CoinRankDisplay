package kr.toru.lmwnassignment.vm

import android.util.Log
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

    // offset
    private var offset = 0

    suspend fun getCoins(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            Event.Loading().emitEvent()
            if (isRefreshing) {
                offset = 0
            }
            else {
                offset += 20
            }
            getCoinsUseCase.getCoins(offset = offset)
                .onSuccess {
                    offset += it.data.coins.size
                    Event.Success(data = it.data.coins).emitEvent()
                    Event.Loading(isLoading = false).emitEvent()
                }
                .onFailure {
                    Event.Loading(isLoading = false).emitEvent()
                    Event.Failure(
                        listOf(
                            ItemViewModel.LoadFailureItemViewModel {
                                callApi {
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

    suspend fun resetCoinCall() {
        Log.e("Toru", "resetCoinCall")
        offset = 0
        callApi {
            getCoins()
        }
    }

    private fun callApi(f: suspend () -> Unit) {
        viewModelScope.launch {
            f.invoke()
        }
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

    override fun onCleared() {
        super.onCleared()
        Log.e("Toru", "MainViewModel cleared!!")
    }
}