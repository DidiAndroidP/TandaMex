package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.CheckWalletExistsUseCase
import com.didiermendoza.tandamex.src.features.wallet.domain.usecases.CreateDefaultWalletUseCase
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.SyncTandasUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAvailableTandasUseCase: GetAvailableTandasUseCase,
    private val syncTandasUseCase: SyncTandasUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val checkWalletExistsUseCase: CheckWalletExistsUseCase,
    private val createDefaultWalletUseCase: CreateDefaultWalletUseCase
) : ViewModel() {

    private val _tandas = MutableStateFlow<List<Tanda>>(emptyList())
    val tandas = _tandas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _userName = MutableStateFlow("Usuario")
    val userName = _userName.asStateFlow()

    private val _userPhoto = MutableStateFlow<String?>(null)
    val userPhoto = _userPhoto.asStateFlow()

    private var currentUserId: Int? = null
    private val _hasWallet = MutableStateFlow(true)
    val hasWallet = _hasWallet.asStateFlow()

    private val _showWalletDialog = MutableStateFlow(false)
    val showWalletDialog = _showWalletDialog.asStateFlow()

    init {
        observeTandas()
        loadData()
    }

    private fun observeTandas() {
        getAvailableTandasUseCase().onEach { list ->
            _tandas.value = list
            if (list.isNotEmpty()) {
                _isLoading.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun loadData() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    currentUserId = user.id
                    _userName.value = user.name.split(" ").firstOrNull() ?: user.name
                    _userPhoto.value = user.photo

                    checkUserWallet(user.id)
                },
                onFailure = {
                    _error.value = "Error al obtener perfil"
                }
            )
        }

        viewModelScope.launch {
            try {
                syncTandasUseCase()
            } catch (e: Exception) {
                _error.value = "Error de conexión: Mostrando datos guardados."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun checkUserWallet(userId: Int) {
        val exists = checkWalletExistsUseCase(userId)
        _hasWallet.value = exists
        if (!exists) {
            _showWalletDialog.value = true
        }
    }

    fun acceptWalletCreation() {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                _isLoading.value = true
                createDefaultWalletUseCase(userId)
                _hasWallet.value = true
                _showWalletDialog.value = false
                _isLoading.value = false
            }
        }
    }

    fun declineWalletCreation() {
        _showWalletDialog.value = false
        _hasWallet.value = false
    }
}