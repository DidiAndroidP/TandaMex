package com.didiermendoza.tandamex.src.features.Tanda.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaDetail

@Composable
fun TandaActionButtons(
    tanda: TandaDetail,
    realCount: Int,
    isLoading: Boolean,
    allPaid: Boolean,
    hasCurrentUserPaid: Boolean,
    onJoin: () -> Unit,
    onLeave: () -> Unit,
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onDelete: () -> Unit,
    onPay: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (tanda.isAdmin) {
            AdminControls(
                tanda = tanda,
                realCount = realCount,
                isLoading = isLoading,
                allPaid = allPaid,
                onStart = onStart,
                onFinish = onFinish,
                onDelete = onDelete
            )
        } else {
            UserControls(
                tanda = tanda,
                realCount = realCount,
                isLoading = isLoading,
                hasCurrentUserPaid = hasCurrentUserPaid,
                onJoin = onJoin,
                onLeave = onLeave,
                onPay = onPay
            )
        }
    }
}

@Composable
fun AdminControls(
    tanda: TandaDetail,
    realCount: Int,
    isLoading: Boolean,
    allPaid: Boolean,
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onDelete: () -> Unit
) {
    val isInProgress = tanda.status == "IN_PROGRESS"
    val isCreated = tanda.status == "CREATED"

    var showIncompleteDialog by remember { mutableStateOf(false) }
    var showDeleteRestrictionDialog by remember { mutableStateOf(false) }

    if (showIncompleteDialog) {
        AlertDialog(
            onDismissRequest = { showIncompleteDialog = false },
            icon = { Icon(Icons.Default.Group, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(text = "Faltan Participantes o Pagos") },
            text = {
                Text(
                    "No puedes iniciar la tanda aún. Se necesitan ${tanda.totalMembers} miembros y que TODOS hayan pagado su aportación inicial.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(onClick = { showIncompleteDialog = false }) {
                    Text("Entendido")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }

    if (showDeleteRestrictionDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteRestrictionDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(text = "Acción no permitida") },
            text = {
                Text(
                    "No puedes eliminar una tanda que ya está en curso. Primero debes finalizarla o esperar a que termine.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(onClick = { showDeleteRestrictionDialog = false }) {
                    Text("Entendido", color = MaterialTheme.colorScheme.error)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }

    if (isCreated || isInProgress) {
        Button(
            onClick = {
                if (isInProgress) {
                    onFinish()
                } else {
                    if (realCount < tanda.totalMembers || !allPaid) {
                        showIncompleteDialog = true
                    } else {
                        onStart()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInProgress) MaterialTheme.colorScheme.error else Color(0xFF2E7D32),
                disabledContainerColor = Color.Gray
            ),
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isInProgress && (realCount < tanda.totalMembers || !allPaid)) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isInProgress) "Finalizar Tanda" else "Iniciar Tanda",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                if (isInProgress) {
                    showDeleteRestrictionDialog = true
                } else {
                    onDelete()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            Text("Eliminar Tanda", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun UserControls(
    tanda: TandaDetail,
    realCount: Int,
    isLoading: Boolean,
    hasCurrentUserPaid: Boolean,
    onJoin: () -> Unit,
    onLeave: () -> Unit,
    onPay: () -> Unit
) {
    if (!tanda.isMember && tanda.status == "CREATED") {
        val isFull = realCount >= tanda.totalMembers
        Button(
            onClick = onJoin,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isLoading && !isFull,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            else Text(if (isFull) "Cupo Lleno" else "Unirme a esta Tanda", style = MaterialTheme.typography.titleMedium)
        }
    } else if (tanda.isMember && tanda.status == "CREATED") {
        Column {
            if (!hasCurrentUserPaid) {
                Button(
                    onClick = onPay,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    else Text("Pagar mi aportación", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = onLeave,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.error)
                else Text(if (hasCurrentUserPaid) "Salir y Reembolsar" else "Salir de la Tanda", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}