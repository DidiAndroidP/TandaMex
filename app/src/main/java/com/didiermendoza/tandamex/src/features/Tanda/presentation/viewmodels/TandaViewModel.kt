package com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import com.didiermendoza.tandamex.src.features.Tanda.domain.usecases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TandaViewModel(
    private val getTandaDetailUseCase: GetTandaDetailUseCase,
    private val joinTandaUseCase: JoinTandaUseCase,
    private val getTandaMembersUseCase: GetTandaMembersUseCase,
    private val leaveTandaUseCase: LeaveTandaUseCase,
    private val startTandaUseCase: StartTandaUseCase,
    private val finishTandaUseCase: FinishTandaUseCase,
    private val deleteTandaUseCase: DeleteTandaUseCase,
    private val generateScheduleUseCase: GenerateScheduleUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _tanda = MutableStateFlow<TandaDetail?>(null)
    val tanda = _tanda.asStateFlow()

    private val _members = MutableStateFlow<List<TandaMember>>(emptyList())
    val members = _members.asStateFlow()

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()

    fun loadTandaDetail(tandaId: Int) {
        _isLoading.value = true
        _message.value = null

        viewModelScope.launch {
            getMyProfileUseCase().onSuccess { user ->
                _currentUserId.value = user.id
            }

            getTandaDetailUseCase(tandaId).fold(
                onSuccess = { detail ->
                    _tanda.value = detail
                },
                onFailure = { err ->
                    _message.value = err.message
                }
            )

            getTandaMembersUseCase(tandaId).fold(
                onSuccess = { list ->
                    _members.value = list
                },
                onFailure = {}
            )

            _isLoading.value = false
        }
    }

    fun joinTanda() {
        performAction { joinTandaUseCase(_tanda.value!!.id) }
    }

    fun leaveTanda() {
        performAction { leaveTandaUseCase(_tanda.value!!.id) }
    }

    fun startTanda() {
        val tandaId = _tanda.value!!.id
        _isLoading.value = true
        viewModelScope.launch {
            generateScheduleUseCase(tandaId)

            startTandaUseCase(tandaId).fold(
                onSuccess = { msg ->
                    _message.value = msg
                    loadTandaDetail(tandaId)
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
                }
            )
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
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
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
                    loadTandaDetail(currentId)
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _message.value = err.message
                }
            )
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}