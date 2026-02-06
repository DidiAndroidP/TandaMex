package com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.usecases.GetAvailableTandasUseCase
import com.didiermendoza.tandamex.src.features.Home.presentation.components.TandaUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class HomeUiState(
    val isLoading: Boolean = false,
    val tandas: List<TandaUiModel> = emptyList(),
    val error: String? = null,
    val userName: String = "Usuario"
)

class HomeViewModel(
    private val getAvailableTandasUseCase: GetAvailableTandasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = getAvailableTandasUseCase()

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { entityList ->
                        // AQUÍ TRANSFORMAMOS ENTIDAD -> UI MODEL
                        val uiList = entityList.map { it.toUiModel() }

                        currentState.copy(isLoading = false, tandas = uiList)
                    },
                    onFailure = { error ->
                        currentState.copy(isLoading = false, error = error.message)
                    }
                )
            }
        }
    }

    // Función auxiliar para mapear Entidad a Modelo de UI dentro del ViewModel
    private fun Tanda.toUiModel(): TandaUiModel {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        return TandaUiModel(
            id = this.id,
            title = this.name,
            amount = formatter.format(this.amount), // Formateamos dinero aquí
            periodicity = when (this.frequency) {
                "weekly" -> "Semanal"
                "biweekly" -> "Quincenal"
                "monthly" -> "Mensual"
                else -> this.frequency
            },
            progress = this.progress,
            membersCount = this.totalMembers
        )
    }
}