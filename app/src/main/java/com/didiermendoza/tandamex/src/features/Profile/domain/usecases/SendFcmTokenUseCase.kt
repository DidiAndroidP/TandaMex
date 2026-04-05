package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class SendFcmTokenUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(token: String): Result<String> {
        return repository.sendFcmToken(token)
    }
}