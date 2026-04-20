package com.didiermendoza.tandamex.src.features.Tanda.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.didiermendoza.tandamex.presentation.tanda_detail.LiveSorteoState
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RouletteDialog(
    state: LiveSorteoState,
    participantNames: List<String>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            if (state is LiveSorteoState.Finished) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = state is LiveSorteoState.Finished,
            dismissOnClickOutside = state is LiveSorteoState.Finished
        )
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sorteo en Vivo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (state) {
                        is LiveSorteoState.Waiting -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Comenzando en...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${state.secondsRemaining}",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        is LiveSorteoState.SpinningTurn -> {
                            var currentNameIndex by remember { mutableIntStateOf(0) }
                            val rotation = remember { Animatable(0f) }

                            LaunchedEffect(state.targetTurn) {
                                rotation.snapTo(0f)
                                rotation.animateTo(
                                    targetValue = 360f * 6 + (0..360).random(),
                                    animationSpec = tween(durationMillis = 2400, easing = FastOutSlowInEasing)
                                )
                            }

                            LaunchedEffect(Unit) {
                                while (true) {
                                    if (participantNames.isNotEmpty()) {
                                        currentNameIndex = (currentNameIndex + 1) % participantNames.size
                                    }
                                    delay(100)
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Sorteando Turno #${state.targetTurn}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Box(contentAlignment = Alignment.TopCenter) {
                                    Canvas(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .graphicsLayer { rotationZ = rotation.value }
                                    ) {
                                        val sliceCount = if (participantNames.size > 2) participantNames.size else 6
                                        val sweep = 360f / sliceCount
                                        for (i in 0 until sliceCount) {
                                            drawArc(
                                                color = if (i % 2 == 0) Color(0xFF6650a4) else Color(0xFFD0BCFF),
                                                startAngle = i * sweep,
                                                sweepAngle = sweep,
                                                useCenter = true
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .offset(y = (-24).dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                val displayName = if (participantNames.isNotEmpty()) participantNames[currentNameIndex] else "..."

                                AnimatedContent(
                                    targetState = displayName,
                                    transitionSpec = {
                                        (slideInVertically(animationSpec = tween(100)) { height -> height } + fadeIn(animationSpec = tween(100))) togetherWith
                                                (slideOutVertically(animationSpec = tween(100)) { height -> -height } + fadeOut(animationSpec = tween(100)))
                                    }, label = "roulette_animation"
                                ) { name ->
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        is LiveSorteoState.TurnRevealed -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "¡Turno #${state.turnRevealed} asignado a!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = state.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        is LiveSorteoState.Finished -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "¡Sorteo Finalizado!",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Todos los turnos han sido asignados correctamente.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        else -> {}
                    }
                }

                if (state is LiveSorteoState.TurnRevealed || state is LiveSorteoState.Finished) {
                    val history = when (state) {
                        is LiveSorteoState.TurnRevealed -> state.history
                        is LiveSorteoState.Finished -> state.finalSchedule
                        else -> emptyList()
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        history.sortedBy { it.first }.forEach { (turno, nombre) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Turno $turno",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = nombre,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                if (state is LiveSorteoState.Finished) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Aceptar y Continuar")
                    }
                }

            }
        }
    }
}