package com.didiermendoza.tandamex.src.features.Login.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Login.domain.usecases.LoginUseCase
import com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels.LoginViewModelFactory

class LoginModule(private val appContainer: AppContainer) {

    private fun provideLoginUseCase(): LoginUseCase {
        return LoginUseCase(appContainer.loginRepository)
    }

    fun provideLoginViewModelFactory(): LoginViewModelFactory {
        return LoginViewModelFactory(provideLoginUseCase())
    }
}