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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.presentation.tanda_detail.LiveSorteoState
import com.didiermendoza.tandamex.presentation.tanda_detail.LiveSorteoViewModel
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.RouletteDialog
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaActionButtons
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaDetailInfo
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaMembersList
import com.didiermendoza.tandamex.src.features.Tanda.presentation.components.TandaScheduleSection
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.TandaViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TandaScreen(
    viewModel: TandaViewModel = hiltViewModel(),
    liveSorteoViewModel: LiveSorteoViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val tanda by viewModel.tanda.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val scheduleData by viewModel.scheduleData.collectAsStateWithLifecycle()
    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val deleteSuccess by viewModel.deleteSuccess.collectAsStateWithLifecycle()
    val accumulatedAmount by viewModel.accumulatedAmount.collectAsStateWithLifecycle()
    val stripeUrl by viewModel.stripeUrl.collectAsStateWithLifecycle()

    val sorteoState by liveSorteoViewModel.liveSorteoState.collectAsStateWithLifecycle()

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(tanda?.id) {
        tanda?.id?.let {
            liveSorteoViewModel.joinLiveRoom(it)
        }
    }

    LaunchedEffect(stripeUrl) {
        stripeUrl?.let { url ->
            uriHandler.openUri(url)
            viewModel.onStripeUrlOpened()
        }
    }

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

    if (sorteoState !is LiveSorteoState.Idle) {
        val participantNames = members.map { it.name }
        RouletteDialog(
            state = sorteoState,
            participantNames = participantNames,
            onDismiss = {
                liveSorteoViewModel.dismissSorteo()
            }
        )
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
                val isTandaFull = members.isNotEmpty() && displayMembersCount == tanda!!.totalMembers
                val hasCurrentUserPaid = members.find { it.id == currentUserId }?.hasPaid == true

                Surface(
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TandaActionButtons(
                        tanda = tanda!!,
                        realCount = displayMembersCount,
                        isLoading = isLoading,
                        allPaid = isTandaFull,
                        hasCurrentUserPaid = hasCurrentUserPaid,
                        onJoin = { viewModel.joinTanda() },
                        onLeave = { viewModel.leaveTanda() },
                        onStart = {
                            tanda?.id?.let { id ->
                                liveSorteoViewModel.startLiveSorteo(id)
                            }
                        },
                        onFinish = { viewModel.finishTanda() },
                        onDelete = { viewModel.deleteTanda() },
                        onPay = { viewModel.payMyContribution() }
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
                    val expectedTotal = tanda!!.contributionAmount * tanda!!.totalMembers

                    TandaDetailInfo(
                        amount = currencyFormatter.format(tanda!!.contributionAmount),
                        accumulatedAmount = accumulatedAmount,
                        totalExpectedAmount = expectedTotal,
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

                    if (scheduleData != null) {
                        if (members.isNotEmpty()) {
                            TandaScheduleSection(
                                scheduleData = scheduleData!!,
                                members = members,
                                currentUserId = currentUserId
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    TandaMembersList(
                        members = members,
                        creatorId = tanda!!.creatorId,
                        tandaStatus = tanda!!.status
                    )

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}