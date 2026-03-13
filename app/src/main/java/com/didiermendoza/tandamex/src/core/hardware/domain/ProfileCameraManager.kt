package com.didiermendoza.tandamex.src.features.Profile.domain

import android.net.Uri

interface ProfileCameraManager {
    suspend fun takePicture(imageCapture: Any): Uri?
}