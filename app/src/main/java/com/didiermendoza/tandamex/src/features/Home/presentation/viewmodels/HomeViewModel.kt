package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.SyncTandasUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAvailableTandasUseCase: GetAvailableTandasUseCase,
    private val syncTandasUseCase: SyncTandasUseCase,
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

    private val _userPhoto = MutableStateFlow<String?>(null)
    val userPhoto = _userPhoto.asStateFlow()

    init {
        observeTandas()
        loadData()
    }

    private fun observeTandas() {
        getAvailableTandasUseCase().onEach { list ->
            _tandas.value = list
            if (list.isNotEmpty()) {
                _isLoading.value = false
            }
        }.launchIn(viewModelScope)
    }

    fun loadData() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    _userName.value = user.name.split(" ").firstOrNull() ?: user.name
                    _userPhoto.value = user.photo
                },
                onFailure = {
                }
            )
        }

        viewModelScope.launch {
            try {
                syncTandasUseCase()
            } catch (e: Exception) {
                _error.value = "Error de conexión: Mostrando datos guardados."
            } finally {
                _isLoading.value = false
            }
        }
    }
}