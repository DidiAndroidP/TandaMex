package com.didiermendoza.tandamex.src.features.Home.presentation.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.presentation.components.TandaItemCard
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
    val activeTandas by viewModel.activeTandas.collectAsStateWithLifecycle()
    val historyTandas by viewModel.historyTandas.collectAsStateWithLifecycle()
    val availableTandas by viewModel.availableTandas.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val userPhoto by viewModel.userPhoto.collectAsStateWithLifecycle()

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    val hasWallet by viewModel.hasWallet.collectAsStateWithLifecycle()
    val showWalletDialog by viewModel.showWalletDialog.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Mis Tandas", "Disponibles", "Historial")

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            println("Permiso de notificaciones concedido")
        } else {
            println("El usuario denegó las notificaciones")
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (showWalletDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.declineWalletCreation() },
            title = { Text("Activar Billetera Virtual") },
            text = {
                Text("Para poder crear o unirte a tandas, necesitas activar tu billetera simulada. Te regalaremos $10,000 MXN para que pruebes la app.")
            },
            confirmButton = {
                Button(onClick = { viewModel.acceptWalletCreation() }) {
                    Text("¡Aceptar regalo!")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.declineWalletCreation() }) {
                    Text("Ahora no")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        if (hasWallet) {
                            onNavigateToCreateTanda()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Debes crear una billetera en tu perfil primero.")
                            }
                        }
                    },
                    containerColor = if (hasWallet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (hasWallet) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeHeader(
                    userName = userName,
                    userPhoto = userPhoto,
                    onProfileClick = onNavigateToProfile
                )

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {}
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }

                Crossfade(targetState = isLoading, label = "HomeState") { loading ->
                    if (loading) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                            repeat(3) {
                                SkeletonCard()
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    } else if (error != null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
                        }
                    } else {
                        when (selectedTabIndex) {
                            0 -> TandaListContent(
                                tandas = activeTandas,
                                currencyFormatter = currencyFormatter,
                                onNavigateToDetail = onNavigateToDetail,
                                emptyMessage = "No tienes tandas activas\n¡Crea una nueva para empezar a ahorrar!",
                                emptyIcon = Icons.Outlined.Savings
                            )
                            1 -> TandaListContent(
                                tandas = availableTandas,
                                currencyFormatter = currencyFormatter,
                                onNavigateToDetail = onNavigateToDetail,
                                emptyMessage = "No hay tandas disponibles para unirse en este momento.",
                                emptyIcon = Icons.Outlined.Search
                            )
                            2 -> TandaListContent(
                                tandas = historyTandas,
                                currencyFormatter = currencyFormatter,
                                onNavigateToDetail = onNavigateToDetail,
                                emptyMessage = "Aún no tienes historial de tandas terminadas.",
                                emptyIcon = Icons.Outlined.History
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TandaListContent(
    tandas: List<Tanda>,
    currencyFormatter: NumberFormat,
    onNavigateToDetail: (Int) -> Unit,
    emptyMessage: String,
    emptyIcon: ImageVector
) {
    if (tandas.isEmpty()) {
        EmptyState(emptyMessage, emptyIcon)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tandas, key = { it.id }) { tanda ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
                ) {
                    TandaItemCard(
                        title = tanda.name,
                        amount = currencyFormatter.format(tanda.amount),
                        periodicity = when (tanda.frequency) {
                            "weekly" -> "Semanal"
                            "biweekly" -> "Quincenal"
                            "monthly" -> "Mensual"
                            else -> tanda.frequency
                        },
                        progress = tanda.progress,
                        membersCount = tanda.totalMembers,
                        status = tanda.status,
                        onClick = { onNavigateToDetail(tanda.id) }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun HomeHeader(userName: String, userPhoto: String?, onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hola, $userName \uD83D\uDC4B",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Tu resumen general",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(if (userPhoto != null) 0.dp else 4.dp)
            ) {
                if (userPhoto != null) {
                    AsyncImage(
                        model = userPhoto,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SkeletonCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = alpha))
        )
    }
}

@Composable
fun EmptyState(message: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}