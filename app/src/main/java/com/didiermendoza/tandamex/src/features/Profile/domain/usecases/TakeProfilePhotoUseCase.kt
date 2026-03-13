package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import android.net.Uri
import com.didiermendoza.tandamex.src.features.Profile.domain.ProfileCameraManager
import javax.inject.Inject

class TakeProfilePhotoUseCase @Inject constructor(
    private val cameraManager: ProfileCameraManager
) {
    suspend operator fun invoke(imageCapture: Any): Result<Uri> {
        return try {
            val uri = cameraManager.takePicture(imageCapture)
            if (uri != null) {
                Result.success(uri)
            } else {
                Result.failure(Exception("No se pudo procesar la imagen capturada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}