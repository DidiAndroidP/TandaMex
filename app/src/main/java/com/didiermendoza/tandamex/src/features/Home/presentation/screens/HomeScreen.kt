package com.didiermendoza.tandamex.src.features.Home.presentation.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.src.features.Home.presentation.components.ActiveTandaCard
import com.didiermendoza.tandamex.src.features.Home.presentation.components.DiscoverTandaCard
import com.didiermendoza.tandamex.src.features.Home.presentation.components.GlobalEmptyState
import com.didiermendoza.tandamex.src.features.Home.presentation.components.HomeHeader
import com.didiermendoza.tandamex.src.features.Home.presentation.components.HomeSkeleton
import com.didiermendoza.tandamex.src.features.Home.presentation.components.SectionTitle
import com.didiermendoza.tandamex.src.features.Home.presentation.components.SmallEmptyState
import com.didiermendoza.tandamex.src.features.Home.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCreateTanda: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val myTandas by viewModel.myTandas.collectAsStateWithLifecycle()
    val availableTandas by viewModel.availableTandas.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val userPhoto by viewModel.userPhoto.collectAsStateWithLifecycle()

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) println("Permiso concedido") else println("Permiso denegado")
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = onNavigateToCreateTanda,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Tanda")
                }
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshData() },
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HomeHeader(userName = userName, userPhoto = userPhoto, onProfileClick = onNavigateToProfile)

                Crossfade(targetState = isLoading, label = "HomeState") { loading ->
                    if (loading) {
                        HomeSkeleton()
                    } else if (error != null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = error ?: "Error", color = MaterialTheme.colorScheme.error)
                        }
                    } else if (myTandas.isEmpty() && availableTandas.isEmpty()) {
                        GlobalEmptyState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            item {
                                SectionTitle(
                                    title = "Tus Inscripciones",
                                    icon = Icons.Outlined.AccountBalanceWallet,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                                )

                                if (myTandas.isEmpty()) {
                                    SmallEmptyState(
                                        message = "Aún no participas en ninguna tanda.",
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                } else {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 20.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        items(myTandas) { tanda ->
                                            ActiveTandaCard(
                                                title = tanda.name,
                                                amount = currencyFormatter.format(tanda.amount),
                                                progress = tanda.progress,
                                                onClick = { onNavigateToDetail(tanda.id) }
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            }

                            item {
                                SectionTitle(
                                    title = "Explorar Globales",
                                    icon = Icons.Outlined.TravelExplore,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .padding(bottom = 12.dp)
                                )
                            }

                            if (availableTandas.isEmpty()) {
                                item {
                                    SmallEmptyState(
                                        message = "No hay tandas nuevas disponibles por ahora.",
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                }
                            } else {
                                items(availableTandas) { tanda ->
                                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                                        DiscoverTandaCard(
                                            title = tanda.name,
                                            amount = currencyFormatter.format(tanda.amount),
                                            periodicity = when (tanda.frequency) {
                                                "weekly" -> "Semanal"
                                                "biweekly" -> "Quincenal"
                                                "monthly" -> "Mensual"
                                                else -> tanda.frequency
                                            },
                                            membersCount = tanda.totalMembers,
                                            onClick = { onNavigateToDetail(tanda.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}