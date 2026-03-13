package com.didiermendoza.tandamex.src.features.Profile.data

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.didiermendoza.tandamex.src.features.Profile.domain.ProfileCameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidProfileCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileCameraManager {

    override suspend fun takePicture(imageCapture: Any): Uri? = suspendCoroutine { continuation ->
        val capture = imageCapture as? ImageCapture
        if (capture == null) {
            continuation.resume(null)
            return@suspendCoroutine
        }

        val photoFile = File(
            context.cacheDir,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(Uri.fromFile(photoFile))
                }

                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                    continuation.resume(null)
                }
            }
        )
    }
}