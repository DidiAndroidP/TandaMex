package com.didiermendoza.tandamex.src.features.Register.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Register.domain.usecases.RegisterUserUseCase
import com.didiermendoza.tandamex.src.features.Register.presentation.viewmodels.RegisterViewModelFactory

class RegisterModule(
    private val appContainer: AppContainer
) {

    private fun provideRegisterUserUseCase(): RegisterUserUseCase {
        return RegisterUserUseCase(appContainer.registerRepository)
    }

    fun provideRegisterViewModelFactory(): RegisterViewModelFactory {
        return RegisterViewModelFactory(
            registerUserUseCase = provideRegisterUserUseCase()
        )
    }
}