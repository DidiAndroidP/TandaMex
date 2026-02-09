package com.didiermendoza.tandamex.src.features.Profile.domain.repositories

import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User

interface ProfileRepository {
    suspend fun getMyProfile(): Result<User>
    suspend fun updateProfile(name: String, phone: String): Result<String>
}