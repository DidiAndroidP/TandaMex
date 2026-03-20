package com.didiermendoza.tandamex.src.features.Profile.presentation.components

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileHeader(
    isEditing: Boolean,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    avatarContent: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                    ),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )

        if (isEditing) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onCancelClick,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) { Icon(Icons.Default.Close, contentDescription = "Cancelar", tint = Color.White) }
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) { Icon(Icons.Default.Done, contentDescription = "Guardar", tint = Color.White) }
            }
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 50.dp),
            content = avatarContent
        )
    }
}

@Composable
fun ProfileAvatar(
    initials: String,
    photoUri: Uri?,
    photoUrl: String?,
    isEditing: Boolean,
    onCameraClick: () -> Unit
) {
    val imageModel = photoUri ?: photoUrl

    Box {
        Surface(
            shape = CircleShape,
            shadowElevation = 8.dp,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.background),
            modifier = Modifier.size(120.dp)
        ) {
            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (isEditing) {
            IconButton(
                onClick = onCameraClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .size(36.dp)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Cambiar foto", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ProfileForm(
    isEditing: Boolean,
    userName: String,
    userEmail: String,
    userPhone: String,
    nameInput: String,
    phoneInput: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    successMessage: String?,
    error: String?
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = isEditing,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220, delayMillis = 90)) + scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            }, label = "FormTransition"
        ) { editing ->
            if (editing) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nameInput, onValueChange = onNameChange,
                        label = { Text("Nombre Completo") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = phoneInput, onValueChange = onPhoneChange,
                        label = { Text("Teléfono") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = userName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = userEmail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                    if (userPhone.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = userPhone, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

        if (successMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = successMessage, color = Color(0xFF4CAF50), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ProfileSettingsMenu(
    onWalletClick: () -> Unit,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Cuenta", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileOptionItem(icon = Icons.Default.Edit, title = "Editar Perfil", subtitle = "Cambiar nombre, teléfono y foto", onClick = onEditClick)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Finanzas", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileOptionItem(
                icon = Icons.Default.AccountBalanceWallet, // Ícono de billetera
                title = "Mi Billetera",
                subtitle = "Consultar saldo y métodos de pago",
                onClick = onWalletClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sesión", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileOptionItem(icon = Icons.Default.Logout, title = "Cerrar Sesión", isDestructive = true, onClick = onLogoutClick)
        }
    }
}