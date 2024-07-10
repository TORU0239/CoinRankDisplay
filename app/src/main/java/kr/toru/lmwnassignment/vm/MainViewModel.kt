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

            getCoinsUseCase.getCoins(offset = offset)
                .onSuccess {
                    Event.Success(
                        data = it.data.coins,
                        currentOffset = offset
                    ).emitEvent()

                    Event.Loading(isLoading = false).emitEvent()

                    offset += it.data.coins.size
                }
                .onFailure {
                    Event.Loading(isLoading = false).emitEvent()
                    Log.e("Toru", "offset: $offset")
                    Event.Failure(
                        listOf(
                            ItemViewModel.LoadFailureItemViewModel {
                                callApi {
                                    getCoins()
                                }
                            }
                        ),
                        currentOffset = offset
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
            val data: List<CoinInfoResponse>,
            val currentOffset: Int = 0
        ): Event

        data class Failure(
            val data: List<ItemViewModel.LoadFailureItemViewModel>,
            val currentOffset: Int = 0
        ): Event
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("Toru", "MainViewModel cleared!!")
    }
}