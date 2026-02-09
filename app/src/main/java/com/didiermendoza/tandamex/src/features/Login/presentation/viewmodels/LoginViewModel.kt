package com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _successData = MutableStateFlow<LoginSuccess?>(null)
    val successData = _successData.asStateFlow()

    fun onChangeEmail(newValue: String) {
        _email.value = newValue
    }

    fun onChangePassword(newValue: String) {
        _password.value = newValue
    }

    fun onLoginClick() {
        _isLoading.value = true
        _error.value = null

        val input = LoginInput(
            email = _email.value,
            password = _password.value
        )

        viewModelScope.launch {
            val result = loginUseCase(input)

            result.fold(
                onSuccess = { data ->
                    _isLoading.value = false
                    _successData.value = data
                },
                onFailure = { exception ->
                    _isLoading.value = false
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
        }
    }

    fun resetState() {
        _email.value = ""
        _password.value = ""
        _isLoading.value = false
        _error.value = null
        _successData.value = null
    }
}