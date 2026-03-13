package com.didiermendoza.tandamex.src.core.hardware.domain

interface BiometricAuthenticator {
    fun isBiometricAvailable(): Boolean
    fun authenticate(
        activityContext: Any,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}