package com.didiermendoza.tandamex.src.core.workers

import com.didiermendoza.tandamex.src.core.status.UploadStatus
import kotlinx.coroutines.flow.Flow

interface ProfileSyncManager {
    fun enqueuePhotoUpload(filePath: String)
    fun observeUploadState(): Flow<UploadStatus>
}

//jj