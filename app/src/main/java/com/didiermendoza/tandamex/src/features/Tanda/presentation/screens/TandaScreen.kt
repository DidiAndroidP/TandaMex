package com.didiermendoza.tandamex.src.features.Tanda.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaActionButtons
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaDetailInfo
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaMembersList
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.TandaViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TandaScreen(
    viewModel: TandaViewModel,
    onBackClick: () -> Unit
) {
    val tanda by viewModel.tanda.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val deleteSuccess by viewModel.deleteSuccess.collectAsStateWithLifecycle()

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tanda?.name ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (tanda != null) {
                val displayMembersCount = if (members.isNotEmpty()) members.size else tanda!!.currentMembers

                Surface(
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TandaActionButtons(
                        tanda = tanda!!,
                        realCount = displayMembersCount,
                        isLoading = isLoading,
                        onJoin = { viewModel.joinTanda() },
                        onLeave = { viewModel.leaveTanda() },
                        onStart = { viewModel.startTanda() },
                        onFinish = { viewModel.finishTanda() },
                        onDelete = { viewModel.deleteTanda() }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (tanda == null && isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (tanda != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    val displayMembersCount = if (members.isNotEmpty()) members.size else tanda!!.currentMembers

                    TandaDetailInfo(
                        amount = currencyFormatter.format(tanda!!.contributionAmount),
                        frequency = when (tanda!!.frequency) {
                            "weekly" -> "Semanal"
                            "biweekly" -> "Quincenal"
                            "monthly" -> "Mensual"
                            else -> tanda!!.frequency
                        },
                        members = "$displayMembersCount/${tanda!!.totalMembers}",
                        status = when (tanda!!.status.lowercase()) {
                            "created" -> "Abierta"
                            "in_progress" -> "En curso"
                            "finished" -> "Finalizada"
                            else -> tanda!!.status
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    TandaMembersList(
                        members = members,
                        creatorId = tanda!!.creatorId
                    )

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}