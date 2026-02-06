package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase

class HomeViewModelFactory(
    private val useCase: GetAvailableTandasUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}