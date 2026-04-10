package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.core.status.UploadStatus
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUploadStatusUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UploadStatus> {
        return repository.uploadStatus
    }
}