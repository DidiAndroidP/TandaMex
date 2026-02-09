package com.didiermendoza.tandamex.src.features.Profile.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UpdateProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.presentation.viewmodels.ProfileViewModelFactory

class ProfileModule(private val appContainer: AppContainer) {

    fun provideProfileViewModelFactory(): ProfileViewModelFactory {
        val repository = appContainer.profileRepository

        val getProfileUseCase = GetMyProfileUseCase(repository)
        val updateProfileUseCase = UpdateProfileUseCase(repository)
        return ProfileViewModelFactory(getProfileUseCase, updateProfileUseCase)
    }
}