package com.didiermendoza.tandamex.src.core.hardware.data

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.didiermendoza.tandamex.src.core.hardware.domain.BiometricAuthenticator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidBiometricAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context
) : BiometricAuthenticator {

    private val allowedAuthenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK

    override fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(allowedAuthenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    override fun authenticate(
        activityContext: Any,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val activity = activityContext as? FragmentActivity
        if (activity == null) {
            onError("Contexto no válido para biometría. Asegúrate de que MainActivity herede de FragmentActivity.")
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(allowedAuthenticators)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}