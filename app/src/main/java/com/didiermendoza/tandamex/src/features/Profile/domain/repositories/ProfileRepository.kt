package com.didiermendoza.tandamex.src.features.Profile.domain.repositories

import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User
import java.io.File

interface ProfileRepository {
    suspend fun getMyProfile(): Result<User>
    suspend fun updateProfile(name: String, phone: String): Result<String>
    suspend fun uploadProfilePhoto(photoFile: File): Result<String>
    suspend fun sendFcmToken(token: String): Result<String>
}