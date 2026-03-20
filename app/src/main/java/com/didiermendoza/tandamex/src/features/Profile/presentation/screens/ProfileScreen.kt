package com.didiermendoza.tandamex.src.features.Profile.presentation.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.didiermendoza.tandamex.src.features.Profile.presentation.components.*
import com.didiermendoza.tandamex.src.features.Profile.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToWallet: () -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()
    val profilePhotoUri by viewModel.profilePhotoUri.collectAsStateWithLifecycle()

    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }

    var showCamera by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showCamera = true
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara para la foto de perfil", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(user) {
        user?.let {
            nameInput = it.name
            phoneInput = it.phone
        }
    }

    if (showCamera) {
        ProfileCameraView(
            onImageCaptured = { imageCapture ->
                viewModel.takePhoto(imageCapture)
                showCamera = false
            },
            onClose = { showCamera = false }
        )
        return
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        if (isLoading && user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(
                    isEditing = isEditing,
                    onCancelClick = { viewModel.toggleEditMode() },
                    onSaveClick = { viewModel.saveProfile(nameInput, phoneInput) }
                ) {
                    ProfileAvatar(
                        initials = user?.initials ?: "?",
                        photoUri = profilePhotoUri,
                        photoUrl = user?.photo,
                        isEditing = isEditing,
                        onCameraClick = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                ProfileForm(
                    isEditing = isEditing,
                    userName = user?.name ?: "Usuario",
                    userEmail = user?.email ?: "",
                    userPhone = user?.phone ?: "",
                    nameInput = nameInput,
                    phoneInput = phoneInput,
                    onNameChange = { nameInput = it },
                    onPhoneChange = { phoneInput = it },
                    successMessage = successMessage,
                    error = error
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (!isEditing) {
                    ProfileSettingsMenu(
                        onWalletClick = onNavigateToWallet,
                        onEditClick = { viewModel.toggleEditMode() },
                        onLogoutClick = onLogoutClick
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}