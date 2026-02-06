package com.didiermendoza.tandamex.src.features.Register.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Register.domain.entities.RegisterInput
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User
import com.didiermendoza.tandamex.src.features.Register.domain.usecases.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successUser: User? = null
)

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onRegisterClick(input: RegisterInput) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = registerUserUseCase(input)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { user ->
                        currentState.copy(isLoading = false, successUser = user)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message ?: "Error desconocido")
                    }
                )
            }
        }
    }

    fun resetState() {
        _uiState.update { RegisterUiState() }
    }
}