package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.features.Profile.domain.entities.User
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.getMyProfile()
    }
}