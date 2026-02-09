package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase

class HomeViewModelFactory(
    private val getAvailableTandasUseCase: GetAvailableTandasUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getAvailableTandasUseCase,
                getMyProfileUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}