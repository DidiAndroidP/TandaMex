package com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.core.hardware.domain.BiometricAuthenticator
import com.didiermendoza.tandamex.src.core.hardware.domain.VibrationManager
import com.didiermendoza.tandamex.src.core.storage.TokenManager
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginInput
import com.didiermendoza.tandamex.src.features.Login.domain.entities.LoginSuccess
import com.didiermendoza.tandamex.src.features.Login.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val vibrationManager: VibrationManager,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val tokenManager: TokenManager
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

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome = _navigateToHome.asStateFlow()

    private val _showBiometricButton = MutableStateFlow(false)
    val showBiometricButton = _showBiometricButton.asStateFlow()

    init {
        _showBiometricButton.value = tokenManager.getToken() != null && biometricAuthenticator.isBiometricAvailable()
    }

    fun onChangeEmail(newValue: String) { _email.value = newValue }
    fun onChangePassword(newValue: String) { _password.value = newValue }

    fun onLoginClick() {
        _isLoading.value = true
        _error.value = null

        val input = LoginInput(email = _email.value, password = _password.value)

        viewModelScope.launch {
            val result = loginUseCase(input)

            result.fold(
                onSuccess = { data ->
                    _isLoading.value = false
                    _successData.value = data
                    vibrationManager.vibrate(150)
                },
                onFailure = { exception ->
                    _isLoading.value = false
                    _error.value = exception.message ?: "Error desconocido"
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    fun attemptBiometricLogin(activityContext: Any) {
        if (tokenManager.getToken() == null || !biometricAuthenticator.isBiometricAvailable()) {
            return
        }

        biometricAuthenticator.authenticate(
            activityContext = activityContext,
            title = "Iniciar Sesión",
            subtitle = "Usa tu huella o rostro para entrar a TandaMex",
            onSuccess = {
                vibrationManager.vibrate(150)
                _navigateToHome.value = true
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    fun resetState() {
        _email.value = ""
        _password.value = ""
        _isLoading.value = false
        _error.value = null
        _successData.value = null
        _navigateToHome.value = false
    }
}