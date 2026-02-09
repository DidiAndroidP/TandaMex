package com.didiermendoza.tandamex.src.features.Profile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing = _isEditing.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage = _successMessage.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = {
                    _user.value = it
                    _isLoading.value = false
                },
                onFailure = {
                    _error.value = it.message
                    _isLoading.value = false
                }
            )
        }
    }

    fun toggleEditMode() {
        _isEditing.value = !_isEditing.value
        _error.value = null
        _successMessage.value = null
    }

    fun saveProfile(name: String, phone: String) {
        if (name.isBlank()) {
            _error.value = "El nombre no puede estar vacío"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            updateProfileUseCase(name, phone).fold(
                onSuccess = { msg ->
                    _successMessage.value = msg
                    _isEditing.value = false
                    loadUserProfile()
                },
                onFailure = { err ->
                    _error.value = err.message
                    _isLoading.value = false
                }
            )
        }
    }
}