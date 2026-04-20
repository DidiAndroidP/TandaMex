package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(photoPath: String): Result<String> {
        val cleanPath = photoPath.removePrefix("file://")
        val originalFile = File(cleanPath)

        if (!originalFile.exists()) {
            return Result.failure(Exception("El archivo no existe"))
        }

        return try {
            // 1. Comprimir la imagen antes de subirla
            val compressedFile = compressImage(originalFile)

            // 2. Subir el archivo comprimido
            repository.uploadProfilePhoto(compressedFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(context.cacheDir, "temp_profile_${System.currentTimeMillis()}.jpg")

        val out = FileOutputStream(compressedFile)
        // Reducimos la calidad al 70% para bajar mucho el peso sin perder mucha vista
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
        out.flush()
        out.close()

        return compressedFile
    }
}