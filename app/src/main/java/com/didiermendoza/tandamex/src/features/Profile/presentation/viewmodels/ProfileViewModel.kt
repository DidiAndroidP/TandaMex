package com.didiermendoza.tandamex.src.features.Profile.presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.core.hardware.domain.VibrationManager
import com.didiermendoza.tandamex.src.core.services.DataSyncService
import com.didiermendoza.tandamex.src.core.status.UploadStatus
import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.ObserveUploadStatusUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UpdateProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.TakeProfilePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val takeProfilePhotoUseCase: TakeProfilePhotoUseCase,
    private val vibrationManager: VibrationManager,
    private val observeUploadStatusUseCase: ObserveUploadStatusUseCase,
    @ApplicationContext private val context: Context
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

    private val _profilePhotoUri = MutableStateFlow<Uri?>(null)
    val profilePhotoUri = _profilePhotoUri.asStateFlow()

    init {
        loadUserProfile()
        observePhotoUpload()
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

    private fun observePhotoUpload() {
        viewModelScope.launch {
            observeUploadStatusUseCase().collect { status ->
                when(status) {
                    is UploadStatus.Loading -> _isLoading.value = true
                    is UploadStatus.Success -> {
                        _isLoading.value = false
                        _successMessage.value = status.message
                        loadUserProfile()
                    }
                    is UploadStatus.Error -> {
                        _isLoading.value = false
                        _error.value = status.message
                    }
                    is UploadStatus.Idle -> {}
                }
            }
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
            vibrationManager.vibrateError()
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            updateProfileUseCase(name, phone).fold(
                onSuccess = { msg ->
                    _successMessage.value = msg
                    _isEditing.value = false
                    vibrationManager.vibrate(200)
                    loadUserProfile()
                },
                onFailure = { err ->
                    _error.value = err.message
                    _isLoading.value = false
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    fun takePhoto(imageCapture: Any) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            takeProfilePhotoUseCase(imageCapture).fold(
                onSuccess = { uri ->
                    _profilePhotoUri.value = uri
                    vibrationManager.vibrate(100)
                    uri.path?.let { path ->
                        uploadPhotoViaService(path)
                    } ?: run {
                        _isLoading.value = false
                    }
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                    vibrationManager.vibrateError()
                }
            )
        }
    }

    private fun uploadPhotoViaService(filePath: String) {
        val intent = Intent(context, DataSyncService::class.java).apply {
            action = DataSyncService.ACTION_UPLOAD_PHOTO
            putExtra(DataSyncService.EXTRA_FILE_PATH, filePath)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}