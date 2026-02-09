package com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Tanda.domain.usecases.CreateTandaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateTandaViewModel(
    private val createTandaUseCase: CreateTandaUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    private val _members = MutableStateFlow("")
    val members = _members.asStateFlow()

    private val _delayDays = MutableStateFlow("")
    val delayDays = _delayDays.asStateFlow()

    private val _penaltyAmount = MutableStateFlow("")
    val penaltyAmount = _penaltyAmount.asStateFlow()

    private val _frequencyLabel = MutableStateFlow("Semanal")
    val frequencyLabel = _frequencyLabel.asStateFlow()

    private val _frequencyValue = MutableStateFlow("weekly")
    val frequencyValue = _frequencyValue.asStateFlow()

    private val _isFrequencyExpanded = MutableStateFlow(false)
    val isFrequencyExpanded = _isFrequencyExpanded.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onAmountChange(value: String) { _amount.value = value }
    fun onMembersChange(value: String) { _members.value = value }
    fun onDelayDaysChange(value: String) { _delayDays.value = value }
    fun onPenaltyChange(value: String) { _penaltyAmount.value = value }

    fun toggleFrequencyDropdown(isOpen: Boolean) {
        _isFrequencyExpanded.value = isOpen
    }

    fun onFrequencySelected(label: String, value: String) {
        _frequencyLabel.value = label
        _frequencyValue.value = value
        _isFrequencyExpanded.value = false
    }

    fun createTanda() {
        val amountDouble = _amount.value.toDoubleOrNull()
        val membersInt = _members.value.toIntOrNull()
        val delayDaysInt = _delayDays.value.toIntOrNull()
        val penaltyDouble = _penaltyAmount.value.toDoubleOrNull()

        if (_name.value.isBlank() || amountDouble == null || membersInt == null || delayDaysInt == null || penaltyDouble == null) {
            _error.value = "Por favor verifica los datos numéricos"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            createTandaUseCase(
                name = _name.value,
                amount = amountDouble,
                frequency = _frequencyValue.value,
                members = membersInt,
                tolerance = delayDaysInt,
                penalty = penaltyDouble
            ).fold(
                onSuccess = {
                    _isLoading.value = false
                    _success.value = true
                },
                onFailure = { err ->
                    _isLoading.value = false
                    _error.value = err.message
                }
            )
        }
    }

    fun resetState() {
        _name.value = ""
        _amount.value = ""
        _members.value = ""
        _delayDays.value = ""
        _penaltyAmount.value = ""
        _success.value = false
        _error.value = null
    }
}