package com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.core.hardware.domain.VibrationManager
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class TandaViewModel @Inject constructor(
    private val getTandaDetailUseCase: GetTandaDetailUseCase,
    private val getTandaMembersUseCase: GetTandaMembersUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val syncTandaDetailUseCase: SyncTandaDetailUseCase,
    private val joinTandaUseCase: JoinTandaUseCase,
    private val leaveTandaUseCase: LeaveTandaUseCase,
    private val startTandaUseCase: StartTandaUseCase,
    private val finishTandaUseCase: FinishTandaUseCase,
    private val deleteTandaUseCase: DeleteTandaUseCase,
    private val generateScheduleUseCase: GenerateScheduleUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val createPaymentSessionUseCase: CreatePaymentSessionUseCase,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _tanda = MutableStateFlow<TandaDetail?>(null)
    val tanda = _tanda.asStateFlow()

    private val _members = MutableStateFlow<List<TandaMember>>(emptyList())
    val members = _members.asStateFlow()

    private val _scheduleData = MutableStateFlow<ScheduleData?>(null)
    val scheduleData = _scheduleData.asStateFlow()

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()

    private val _stripeUrl = MutableStateFlow<String?>(null)
    val stripeUrl = _stripeUrl.asStateFlow()

    private var observeJob: Job? = null

    private val _accumulatedAmount = MutableStateFlow(0.0)
    val accumulatedAmount = _accumulatedAmount.asStateFlow()

    fun loadTandaDetail(tandaId: Int) {
        _isLoading.value = true
        _message.value = null

        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user -> _currentUserId.value = user.id },
                onFailure = {}
            )
        }

        observeData(tandaId)
        syncData(tandaId)
    }

    private fun observeData(tandaId: Int) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getTandaDetailUseCase(tandaId).onEach { detail ->
                _tanda.value = detail
                if (detail != null) {
                    _isLoading.value = false
                }
            }.launchIn(this)

            getTandaMembersUseCase(tandaId).onEach { list ->
                _members.value = list
                val cost = _tanda.value?.contributionAmount ?: 0.0
                val paidMembersCount = list.count { it.hasPaid }
                _accumulatedAmount.value = paidMembersCount * cost
            }.launchIn(this)

            getScheduleUseCase(tandaId).onEach { data ->
                _scheduleData.value = data
            }.launchIn(this)
        }
    }

    fun payMyContribution() {
        val tandaInfo = _tanda.value ?: return

        _isLoading.value = true
        viewModelScope.launch {
            createPaymentSessionUseCase(tandaInfo.id, 1, tandaInfo.contributionAmount).fold(
                onSuccess = { url ->
                    _isLoading.value = false
                    _stripeUrl.value = url
                },
                onFailure = { err ->
                    _message.value = err.message
                    _isLoading.value = false
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    fun onStripeUrlOpened() {
        _stripeUrl.value = null
    }

    private fun syncData(tandaId: Int) {
        viewModelScope.launch {
            try {
                syncTandaDetailUseCase(tandaId)
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun joinTanda() {
        performAction { joinTandaUseCase(_tanda.value!!.id) }
    }

    fun leaveTanda() {
        val tandaInfo = _tanda.value ?: return

        val currentUserMember = _members.value.find { it.id == userId }
        val amountToRefund = if (currentUserMember?.hasPaid == true) tandaInfo.contributionAmount else 0.0

        _isLoading.value = true
        viewModelScope.launch {
            leaveTandaUseCase(tandaInfo.id, userId, amountToRefund).fold(
                onSuccess = { msg ->
                    _message.value = msg
                    vibrationManager.vibrate(150)
                    syncData(tandaInfo.id)
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    fun startTanda() {
        val tandaId = _tanda.value?.id ?: return
        _isLoading.value = true

        viewModelScope.launch {
            val scheduleResult = generateScheduleUseCase(tandaId)

            if (scheduleResult.isSuccess) {
                val startResult = startTandaUseCase(tandaId)

                if (startResult.isSuccess) {
                    _message.value = startResult.getOrNull()
                    vibrationManager.vibrate(200)
                    syncData(tandaId)
                } else {
                    _isLoading.value = false
                    _message.value = startResult.exceptionOrNull()?.message
                    vibrationManager.vibrateError()
                }
            } else {
                _isLoading.value = false
                _message.value = scheduleResult.exceptionOrNull()?.message
                vibrationManager.vibrateError()
            }
        }
    }

    fun finishTanda() {
        performAction { finishTandaUseCase(_tanda.value!!.id) }
    }

    fun deleteTanda() {
        val currentId = _tanda.value?.id ?: return
        _isLoading.value = true

        viewModelScope.launch {
            deleteTandaUseCase(currentId).fold(
                onSuccess = {
                    _isLoading.value = false
                    _deleteSuccess.value = true
                    vibrationManager.vibrate(150)
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    private fun performAction(action: suspend () -> Result<String>) {
        val currentId = _tanda.value?.id ?: return
        _isLoading.value = true

        viewModelScope.launch {
            action().fold(
                onSuccess = { msg ->
                    _message.value = msg
                    vibrationManager.vibrate(150)
                    syncData(currentId)
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun refreshCurrentTanda() {
        _tanda.value?.id?.let { syncData(it) }
    }
}