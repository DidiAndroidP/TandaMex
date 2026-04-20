package com.didiermendoza.tandamex.src.core.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.UploadProfilePhotoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class ProfilePhotoUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val uploadProfilePhotoUseCase: UploadProfilePhotoUseCase
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_FILE_PATH = "key_file_path"
        private const val TAG = "ProfileWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val filePath = inputData.getString(KEY_FILE_PATH) ?: return@withContext Result.failure()

        try {
            val uploadResult = uploadProfilePhotoUseCase(filePath)
            if (uploadResult.isSuccess) {
                Result.success()
            } else {
                val error = uploadResult.exceptionOrNull()?.message ?: ""
                // Si el error es 413 o similar, no reintentes, es un fallo definitivo
                if (error.contains("413") || error.contains("400") || error.contains("404")) {
                    Log.e("Worker", "Error fatal del servidor, no se reintentará: $error")
                    Result.failure()
                } else {
                    Result.retry() // Reintenta solo si parece un error de red temporal
                }
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}