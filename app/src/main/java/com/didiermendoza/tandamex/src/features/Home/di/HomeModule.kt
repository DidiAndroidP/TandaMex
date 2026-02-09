package com.didiermendoza.tandamex.src.features.Home.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels.HomeViewModelFactory
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase

class HomeModule(private val appContainer: AppContainer) {

    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        val homeRepository = appContainer.homeRepository
        val profileRepository = appContainer.profileRepository

        val getAvailableTandasUseCase = GetAvailableTandasUseCase(homeRepository)
        val getMyProfileUseCase = GetMyProfileUseCase(profileRepository)

        return HomeViewModelFactory(
            getAvailableTandasUseCase,
            getMyProfileUseCase
        )
    }
}