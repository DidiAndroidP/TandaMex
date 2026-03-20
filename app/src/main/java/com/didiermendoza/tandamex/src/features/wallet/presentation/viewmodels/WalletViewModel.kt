package com.didiermendoza.tandamex.src.features.wallet.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.wallet.domain.usecases.CreateDefaultWalletUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.wallet.domain.usecases.GetWalletFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getWalletFlowUseCase: GetWalletFlowUseCase,
    private val createDefaultWalletUseCase: CreateDefaultWalletUseCase
) : ViewModel() {

    private var currentUserId: Int? = null

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance = _balance.asStateFlow()

    private val _hasWallet = MutableStateFlow(false)
    val hasWallet = _hasWallet.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadWalletData()
    }

    private fun loadWalletData() {
        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    currentUserId = user.id
                    _userName.value = user.name

                    getWalletFlowUseCase(user.id).collect { wallet ->
                        if (wallet != null) {
                            _hasWallet.value = true
                            _balance.value = wallet.balance
                        } else {
                            _hasWallet.value = false
                        }
                        _isLoading.value = false
                    }
                },
                onFailure = {
                    _isLoading.value = false
                }
            )
        }
    }

    fun activateWallet() {
        currentUserId?.let { userId ->
            viewModelScope.launch {
                _isLoading.value = true
                createDefaultWalletUseCase(userId)
            }
        }
    }
}