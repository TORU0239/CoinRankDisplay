package kr.toru.lmwnassignment.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kr.toru.lmwnassignment.data.response.CoinInfoResponse
import kr.toru.lmwnassignment.data.usecase.GetCoinsUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
): ViewModel() {

    private val _outputEventFlow = MutableSharedFlow<Event>()
    val outputEventFlow = _outputEventFlow

    suspend fun getCoins() {
        viewModelScope.launch {
            getCoinsUseCase.getCoins()
                .onSuccess {
                    Event.Success(data = it.data.coins).emitEvent()
                }
                .onFailure {
                    Event.Failure.emitEvent()
                }
        }
    }

    private suspend fun Event.emitEvent() {
        _outputEventFlow.emit(this)
    }


    sealed class Event {
        data class Success(
            val data: List<CoinInfoResponse>
        ): Event()
        data object Failure: Event()
    }
}