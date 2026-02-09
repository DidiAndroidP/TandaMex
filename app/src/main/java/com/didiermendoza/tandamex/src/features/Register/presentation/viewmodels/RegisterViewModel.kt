package com.didiermendoza.tandamex.src.features.Register.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Register.domain.entities.RegisterInput
import com.didiermendoza.tandamex.src.features.Register.domain.entities.User
import com.didiermendoza.tandamex.src.features.Register.domain.usecases.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _successUser = MutableStateFlow<User?>(null)
    val successUser = _successUser.asStateFlow()

    fun onChangeName(newValue: String) {
        _name.value = newValue
    }

    fun onChangeEmail(newValue: String) {
        _email.value = newValue
    }

    fun onChangePhone(newValue: String) {
        _phone.value = newValue
    }

    fun onChangePassword(newValue: String) {
        _password.value = newValue
    }

    fun onChangeConfirmPassword(newValue: String) {
        _confirmPassword.value = newValue
    }

    fun onRegisterClick() {
        _isLoading.value = true
        _error.value = null

        val input = RegisterInput(
            name = _name.value,
            email = _email.value,
            password = _password.value,
            phone = _phone.value
        )

        viewModelScope.launch {
            val result = registerUserUseCase(input)

            result.fold(
                onSuccess = { user ->
                    _isLoading.value = false
                    _successUser.value = user
                },
                onFailure = { exception ->
                    _isLoading.value = false
                    _error.value = exception.message ?: "Error desconocido"
                }
            )
        }
    }

    fun resetState() {
        _name.value = ""
        _email.value = ""
        _phone.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _isLoading.value = false
        _error.value = null
        _successUser.value = null
    }
}