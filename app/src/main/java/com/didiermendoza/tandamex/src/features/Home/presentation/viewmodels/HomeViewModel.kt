package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.core.status.UploadStatus
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetMyTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.SyncTandasUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.ObserveUploadStatusUseCase
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.SendFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessaging
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
    private val getMyTandasUseCase: GetMyTandasUseCase,
    private val syncTandasUseCase: SyncTandasUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val sendFcmTokenUseCase: SendFcmTokenUseCase,
    private val observeUploadStatusUseCase: ObserveUploadStatusUseCase
) : ViewModel() {

    private val _availableTandas = MutableStateFlow<List<Tanda>>(emptyList())
    val availableTandas = _availableTandas.asStateFlow()

    private val _activeTandas = MutableStateFlow<List<Tanda>>(emptyList())
    val activeTandas = _activeTandas.asStateFlow()

    private val _historyTandas = MutableStateFlow<List<Tanda>>(emptyList())
    val historyTandas = _historyTandas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _userName = MutableStateFlow("Usuario")
    val userName = _userName.asStateFlow()

    private val _userPhoto = MutableStateFlow<String?>(null)
    val userPhoto = _userPhoto.asStateFlow()

    private var currentUserId: Int? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        refreshData()
        observeTandas()
        observeProfileChanges()
    }

    private fun observeTandas() {
        getAvailableTandasUseCase().onEach { list ->
            _availableTandas.value = list
            if (list.isNotEmpty()) {
                _isLoading.value = false
            }
        }.launchIn(viewModelScope)
    }

    private fun observeProfileChanges() {
        viewModelScope.launch {
            observeUploadStatusUseCase().collect { status ->
                if (status is UploadStatus.Success) {
                    loadData()
                }
            }
        }
    }

    fun loadData() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMyProfileUseCase().fold(
                onSuccess = { user ->
                    currentUserId = user.id
                    _userName.value = user.name.split(" ").firstOrNull() ?: user.name
                    val cleanUrl = user.photo?.let { url ->
                        val sansQuery = url.split("?").first() // Quitamos parámetros ?_a=...

                        // 1. Forzamos formato JPG en el path de transformación
                        val withFormat = sansQuery.replace("f_auto", "f_jpg")

                        // 2. IMPORTANTE: Añadimos la extensión al final del ID público
                        if (!withFormat.lowercase().endsWith(".jpg")) {
                            "$withFormat.jpg"
                        } else {
                            withFormat
                        }
                    }

                    Log.d("DEBUG_PHOTO", "URL FORZADA: $cleanUrl")
                    _userPhoto.value = cleanUrl

                    registerDeviceToken()
                },
                onFailure = {
                    _error.value = "Error al obtener perfil"
                }
            )
        }

        viewModelScope.launch {
            try {
                syncTandasUseCase()
            } catch (e: Exception) {
                println("Error syncTandas: ${e.message}")
            }
        }

        viewModelScope.launch {
            getMyTandasUseCase().fold(
                onSuccess = { miLista ->
                    val (history, active) = miLista.partition { it.status.equals("finished", ignoreCase = true) }
                    _historyTandas.value = history
                    _activeTandas.value = active
                },
                onFailure = { error ->
                    _error.value = "Error Mis Tandas: ${error.message}"
                }
            )
            _isLoading.value = false
        }
    }

    private fun registerDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("FCM: Error al obtener el token de Firebase")
                return@addOnCompleteListener
            }

            val token = task.result
            println("FCM Token obtenido:")

            viewModelScope.launch {
                sendFcmTokenUseCase(token).fold(
                    onSuccess = { println("Token guardado en Backend exitosamente") },
                    onFailure = { println("Error al guardar token en Backend: ${it.message}") }
                )
            }
        }
    }

    fun refreshData() {
        _isRefreshing.value = true
        _error.value = null

        loadData()

        _isRefreshing.value = false
    }
}