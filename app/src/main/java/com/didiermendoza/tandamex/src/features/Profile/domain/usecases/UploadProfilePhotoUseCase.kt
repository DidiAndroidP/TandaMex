package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import java.io.File
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(photoPath: String): Result<String> {
        val file = File(photoPath)
        if (!file.exists()) {
            return Result.failure(Exception("El archivo de la foto no existe"))
        }
        return repository.uploadProfilePhoto(file)
    }
}