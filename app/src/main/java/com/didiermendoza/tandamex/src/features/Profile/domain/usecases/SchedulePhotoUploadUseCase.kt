package com.didiermendoza.tandamex.src.features.Profile.domain.usecases

import com.didiermendoza.tandamex.src.core.workers.ProfileSyncManager
import javax.inject.Inject

class SchedulePhotoUploadUseCase @Inject constructor(
    private val profileSyncManager: ProfileSyncManager
) {
    operator fun invoke(filePath: String) {
        profileSyncManager.enqueuePhotoUpload(filePath)
    }
}