package com.didiermendoza.tandamex.src.core.di

import com.didiermendoza.tandamex.src.core.hardware.data.AndroidBiometricAuthenticator
import com.didiermendoza.tandamex.src.core.hardware.data.AndroidVibrationManager
import com.didiermendoza.tandamex.src.core.hardware.domain.BiometricAuthenticator
import com.didiermendoza.tandamex.src.core.hardware.domain.VibrationManager
import com.didiermendoza.tandamex.src.features.Profile.data.AndroidProfileCameraManager
import com.didiermendoza.tandamex.src.features.Profile.domain.ProfileCameraManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindVibrationManager(
        impl: AndroidVibrationManager
    ): VibrationManager

    @Binds
    @Singleton
    abstract fun bindBiometricAuthenticator(
        impl: AndroidBiometricAuthenticator
    ): BiometricAuthenticator

    @Binds
    @Singleton
    abstract fun bindProfileCameraManager(
        impl: AndroidProfileCameraManager
    ): ProfileCameraManager
}