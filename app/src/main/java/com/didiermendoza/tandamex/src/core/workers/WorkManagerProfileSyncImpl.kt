package com.didiermendoza.tandamex.src.core.workers

import javax.inject.Inject
import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.didiermendoza.tandamex.src.core.status.UploadStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkManagerProfileSyncImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileSyncManager {

    override fun enqueuePhotoUpload(filePath: String) {
        val inputData = Data.Builder()
            .putString(ProfilePhotoUploadWorker.KEY_FILE_PATH, filePath)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<ProfilePhotoUploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "UploadProfilePhoto",
            ExistingWorkPolicy.REPLACE,
            uploadWorkRequest
        )
    }

    override fun observeUploadState(): Flow<UploadStatus> {
        val workManager = WorkManager.getInstance(context)

        return workManager.getWorkInfosForUniqueWorkFlow("UploadProfilePhoto")
            .map { workInfoList ->
                val workInfo = workInfoList.firstOrNull() ?: return@map UploadStatus.Idle

                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> UploadStatus.Loading
                    WorkInfo.State.RUNNING -> UploadStatus.Loading
                    WorkInfo.State.SUCCEEDED -> UploadStatus.Success("Foto actualizada con éxito")
                    WorkInfo.State.FAILED -> UploadStatus.Error("Error al subir la foto.")
                    WorkInfo.State.CANCELLED -> UploadStatus.Idle
                    WorkInfo.State.BLOCKED -> UploadStatus.Loading
                }
            }
    }
}