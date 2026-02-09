package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository

class UpdateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(name: String, phone: String): Result<String> {
        return repository.updateProfile(name, phone)
    }
}