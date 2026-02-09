package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAvailableTandasUseCase: GetAvailableTandasUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModel() {

    private val _tandas = MutableStateFlow<List<Tanda>>(emptyList())
    val tandas = _tandas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _userName = MutableStateFlow("Usuario")
    val userName = _userName.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    _userName.value = user.name.split(" ").firstOrNull() ?: user.name
                },
                onFailure = {
                }
            )

            getAvailableTandasUseCase().fold(
                onSuccess = { list ->
                    _tandas.value = list
                    _isLoading.value = false
                },
                onFailure = { err ->
                    _error.value = err.message
                    _isLoading.value = false
                }
            )
        }
    }
}