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
                hasCurrentUserPaid = hasCurrentUserPaid,
                onStart = onStart,
                onFinish = onFinish,
                onDelete = onDelete,
                onPay = onPay
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
    hasCurrentUserPaid: Boolean,
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onDelete: () -> Unit,
    onPay: () -> Unit
) {
    val isInProgress = tanda.status == "IN_PROGRESS"
    val isCreated = tanda.status == "CREATED"

    var showIncompleteDialog by remember { mutableStateOf(false) }
    var showDeleteRestrictionDialog by remember { mutableStateOf(false) }

    if (showIncompleteDialog) {
        AlertDialog(
            onDismissRequest = { showIncompleteDialog = false },
            icon = { Icon(Icons.Default.Group, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(text = "Faltan Participantes") },
            text = {
                Text(
                    "No puedes iniciar la tanda aún. El cupo no está lleno (se necesitan ${tanda.totalMembers} miembros).",
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

        // 👇 EL ADMIN SOLO PUEDE PAGAR CUANDO LA TANDA ESTÁ EN CURSO 👇
        if (isInProgress && !hasCurrentUserPaid) {
            Button(
                onClick = onPay,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Pagar mi aportación", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (isInProgress) {
                    onFinish()
                } else {
                    // Solo revisamos que el cupo esté lleno para iniciarla
                    if (realCount < tanda.totalMembers) {
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
                    if (!isInProgress && realCount < tanda.totalMembers) {
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
    } else if (tanda.isMember) {
        Column {
            // 👇 EL USUARIO SOLO PUEDE PAGAR CUANDO LA TANDA ESTÁ EN CURSO 👇
            if (tanda.status == "IN_PROGRESS" && !hasCurrentUserPaid) {
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

            // 👇 EL USUARIO SOLO PUEDE SALIRSE EN ESTADO "CREATED" 👇
            if (tanda.status == "CREATED") {
                OutlinedButton(
                    onClick = onLeave,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.error)
                    else Text("Salir de la Tanda", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}