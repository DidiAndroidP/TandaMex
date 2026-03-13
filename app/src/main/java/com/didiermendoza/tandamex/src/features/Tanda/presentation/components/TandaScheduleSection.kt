package com.didiermendoza.tandamex.src.features.Tanda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.ScheduleData
import com.didiermendoza.tandamex.src.features.Tanda.domain.entities.TandaMember
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TandaScheduleSection(
    scheduleData: ScheduleData,
    members: List<TandaMember>,
    currentUserId: Int?,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Calendario de Pagos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Finaliza:", style = MaterialTheme.typography.labelMedium)
                    Text(scheduleData.fechaFin, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total a recibir:", style = MaterialTheme.typography.labelMedium)
                    Text(currencyFormatter.format(scheduleData.montoTotal), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                scheduleData.turnos.forEachIndexed { index, turno ->
                    val memberName = when {
                        turno.participanteId == currentUserId -> "Tú"
                        else -> members.find { it.id == turno.participanteId }?.name ?: "Usuario ${turno.participanteId}"
                    }

                    ListItem(
                        headlineContent = {
                            Text(memberName, fontWeight = FontWeight.SemiBold)
                        },
                        supportingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(turno.fechaCobro, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(if (turno.participanteId == currentUserId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${turno.numeroTurno}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (turno.participanteId == currentUserId) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        trailingContent = {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = currencyFormatter.format(turno.montoRecibir),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (turno.estado == "pendiente") "Pendiente" else "Pagado",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (turno.estado == "pendiente") MaterialTheme.colorScheme.outline else Color(0xFF4CAF50)
                                )
                            }
                        }
                    )

                    if (index < scheduleData.turnos.size - 1) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}