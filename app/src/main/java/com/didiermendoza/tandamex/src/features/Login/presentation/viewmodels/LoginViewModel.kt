package com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successData: LoginSuccess? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onLoginClick(email: String, pass: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val input = LoginInput(email, pass)
            val result = loginUseCase(input)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { data ->
                        currentState.copy(isLoading = false, successData = data)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message ?: "Error desconocido")
                    }
                )
            }
        }
    }

    fun resetState() {
        _uiState.update { LoginUiState() }
    }
}