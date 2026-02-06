package com.didiermendoza.tandamex.src.features.Profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Importa tu componente
import com.didiermendoza.tandamex.src.features.Profile.presentation.components.ProfileOptionItem

@Composable
fun ProfileScreen() {
    // Datos simulados (ViewModel vendría aquí)
    val userName = "Didier Mendoza"
    val userEmail = "didier@tandamex.com"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. HEADER DEL PERFIL ---
            // (Como es único en esta pantalla, lo construimos aquí mismo)
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1), // Inicial del nombre
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- 2. SECCIÓN DE OPCIONES (Usando el Componente) ---
            // Aquí es donde brilla el componente: Código limpio y repetible.

            Text(
                text = "Cuenta",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionItem(
                icon = Icons.Default.Person,
                title = "Editar Datos Personales",
                subtitle = "Nombre, teléfono, dirección",
                onClick = { /* Navegar a editar */ }
            )

            ProfileOptionItem(
                icon = Icons.Default.CreditCard, // O AccountBalance
                title = "Métodos de Pago",
                subtitle = "Tarjetas y cuentas CLABE",
                onClick = { /* Navegar a pagos */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "General",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileOptionItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones",
                onClick = { /* Configurar notificaciones */ }
            )

            ProfileOptionItem(
                icon = Icons.Default.Security,
                title = "Seguridad y Privacidad",
                onClick = { /* Cambiar password, etc */ }
            )

            ProfileOptionItem(
                icon = Icons.Default.Help,
                title = "Ayuda y Soporte",
                onClick = { /* Abrir chat o FAQ */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Opción Destructiva (Logout)
            ProfileOptionItem(
                icon = Icons.Default.Logout,
                title = "Cerrar Sesión",
                isDestructive = true,
                onClick = { /* Lógica de Logout */ }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}