package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(name: String, phone: String): Result<String> {
        return repository.updateProfile(name, phone)
    }
}