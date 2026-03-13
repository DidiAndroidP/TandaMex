package com.didiermendoza.tandamex.src.features.Login.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.src.features.Login.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val successData by viewModel.successData.collectAsStateWithLifecycle()

    val navigateToHome by viewModel.navigateToHome.collectAsStateWithLifecycle()
    val showBiometricButton by viewModel.showBiometricButton.collectAsStateWithLifecycle()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val isFormValid = email.isNotEmpty() && password.isNotEmpty()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(successData, navigateToHome) {
        if (successData != null || navigateToHome) {
            onNavigateToHome()
            viewModel.resetState()
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(visible = isVisible, enter = scaleIn(tween(500)) + fadeIn(tween(500))) {
                    Box(
                        modifier = Modifier.size(100.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Login, contentDescription = "Login Logo", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(50.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                AnimatedVisibility(visible = isVisible, enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(500, delayMillis = 200)) + fadeIn()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Bienvenido de nuevo", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold)
                        Text("Ingresa tus credenciales para continuar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(visible = isVisible, enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(500, delayMillis = 400)) + fadeIn()) {
                    Column {
                        OutlinedTextField(
                            value = email, onValueChange = { viewModel.onChangeEmail(it) },
                            label = { Text("Correo Electrónico") }, leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary) },
                            modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            enabled = !isLoading
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password, onValueChange = { viewModel.onChangePassword(it) },
                            label = { Text("Contraseña") }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary) },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, "Ver contraseña")
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            enabled = !isLoading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(visible = error != null) {
                    Text(error ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 16.dp))
                }

                AnimatedVisibility(visible = isVisible, enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(500, delayMillis = 600)) + fadeIn()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onLoginClick() },
                            modifier = Modifier.weight(1f).height(56.dp),
                            enabled = isFormValid && !isLoading,
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(8.dp, 2.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                            } else {
                                Text("Iniciar Sesión", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (showBiometricButton) {
                            FilledIconButton(
                                onClick = { viewModel.attemptBiometricLogin(context) },
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = "Biometría", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text("¿No tienes una cuenta? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Regístrate aquí", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToRegister() })
                }
            }
        }
    }
}