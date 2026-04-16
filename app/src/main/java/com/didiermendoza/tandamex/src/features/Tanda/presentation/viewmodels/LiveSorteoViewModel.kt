package com.didiermendoza.tandamex.presentation.tanda_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.core.network.SocketManager
import com.didiermendoza.tandamex.domain.usecase.tanda.TriggerLiveScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

sealed class LiveSorteoState {
    object Idle : LiveSorteoState()
    data class Waiting(val secondsRemaining: Int) : LiveSorteoState()
    data class SpinningTurn(val targetTurn: Int) : LiveSorteoState()
    data class TurnRevealed(val turnRevealed: Int, val name: String, val history: List<Pair<Int, String>>) : LiveSorteoState()
    data class Finished(val finalSchedule: List<Pair<Int, String>>) : LiveSorteoState()
}

@HiltViewModel
class LiveSorteoViewModel @Inject constructor(
    private val triggerLiveScheduleUseCase: TriggerLiveScheduleUseCase,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _liveSorteoState = MutableStateFlow<LiveSorteoState>(LiveSorteoState.Idle)
    val liveSorteoState = _liveSorteoState.asStateFlow()

    init {
        observeSocketEvents()
    }

    fun joinLiveRoom(tandaId: Int) {
        socketManager.connectToTanda(tandaId)
    }

    fun startLiveSorteo(tandaId: Int) {
        viewModelScope.launch {
            triggerLiveScheduleUseCase(tandaId)
        }
    }

    private fun observeSocketEvents() {
        socketManager.countdownEvent.onEach { seconds ->
            startLocalCountdown(seconds)
        }.launchIn(viewModelScope)

        socketManager.sorteoResultEvent.onEach { data ->
            executeSequentialReveal(data)
        }.launchIn(viewModelScope)
    }

    private fun startLocalCountdown(initialSeconds: Int) {
        viewModelScope.launch {
            for (i in initialSeconds downTo 0) {
                if (_liveSorteoState.value !is LiveSorteoState.Waiting && _liveSorteoState.value !is LiveSorteoState.Idle) break
                _liveSorteoState.value = LiveSorteoState.Waiting(i)
                delay(1000)
            }
        }
    }

    private fun executeSequentialReveal(data: JSONObject) {
        viewModelScope.launch {
            val turnosList = parseScheduleData(data)
            val revealedSoFar = mutableListOf<Pair<Int, String>>()

            for (turno in turnosList) {
                _liveSorteoState.value = LiveSorteoState.SpinningTurn(targetTurn = turno.first)
                delay(2500)

                revealedSoFar.add(turno)
                _liveSorteoState.value = LiveSorteoState.TurnRevealed(
                    turnRevealed = turno.first,
                    name = turno.second,
                    history = revealedSoFar.toList()
                )
                delay(2000)
            }

            _liveSorteoState.value = LiveSorteoState.Finished(revealedSoFar)
        }
    }

    private fun parseScheduleData(data: JSONObject): List<Pair<Int, String>> {
        val resultList = mutableListOf<Pair<Int, String>>()
        try {
            val turnosArray = data.optJSONArray("turnosAsignados")
            if (turnosArray != null) {
                for (i in 0 until turnosArray.length()) {
                    val item = turnosArray.getJSONObject(i)
                    val numTurno = item.getInt("numeroTurno")
                    val nombre = item.optString("nombreParticipante", "Usuario ID: ${item.getInt("participanteId")}")
                    resultList.add(Pair(numTurno, nombre))
                }
            }
        } catch (e: Exception) { e.printStackTrace() }

        return resultList.sortedBy { it.first }
    }

    fun dismissSorteo() {
        _liveSorteoState.value = LiveSorteoState.Idle
        socketManager.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        socketManager.disconnect()
    }
}