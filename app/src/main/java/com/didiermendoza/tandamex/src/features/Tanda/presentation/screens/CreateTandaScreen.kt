package com.didiermendoza.tandamex.src.features.Tanda.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.CreateTandaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTandaScreen(
    viewModel: CreateTandaViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val delayDays by viewModel.delayDays.collectAsStateWithLifecycle()
    val penaltyAmount by viewModel.penaltyAmount.collectAsStateWithLifecycle()

    val frequencyLabel by viewModel.frequencyLabel.collectAsStateWithLifecycle()
    val isFrequencyExpanded by viewModel.isFrequencyExpanded.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val success by viewModel.success.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val frequencyOptions = mapOf("Semanal" to "weekly", "Quincenal" to "biweekly", "Mensual" to "monthly")

    LaunchedEffect(success) {
        if (success) {
            onSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Tanda") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Datos Principales", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nombre de la Tanda") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = viewModel::onAmountChange,
                    label = { Text("Monto ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = members,
                    onValueChange = viewModel::onMembersChange,
                    label = { Text("Participantes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = frequencyLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Frecuencia de Pago") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleFrequencyDropdown(true) }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = isFrequencyExpanded,
                    onDismissRequest = { viewModel.toggleFrequencyDropdown(false) }
                ) {
                    frequencyOptions.forEach { (label, value) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = { viewModel.onFrequencySelected(label, value) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Reglas de Retraso", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = delayDays,
                    onValueChange = viewModel::onDelayDaysChange,
                    label = { Text("Días tolerancia") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = penaltyAmount,
                    onValueChange = viewModel::onPenaltyChange,
                    label = { Text("Multa ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.createTanda() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Crear Tanda")
            }

            if (error != null) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}