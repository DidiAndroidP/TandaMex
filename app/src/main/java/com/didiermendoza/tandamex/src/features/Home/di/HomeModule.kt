package com.didiermendoza.tandamex.src.features.Home.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Home.data.repositories.HomeRepositoryImpl
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels.HomeViewModelFactory

class HomeModule(private val appContainer: AppContainer) {
    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        val repository = HomeRepositoryImpl(appContainer.tandaMexApi)
        val useCase = GetAvailableTandasUseCase(repository)
        return HomeViewModelFactory(useCase)
    }
}