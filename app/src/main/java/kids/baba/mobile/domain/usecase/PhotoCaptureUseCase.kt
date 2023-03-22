package kids.baba.mobile.domain.usecase

import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.OutputOptions
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject

class PhotoCaptureUseCase @Inject constructor(
    private val cameraExecutor: Executor
) {

    private val TAG = "PhotoCaptureUseCase"

    suspend fun getMe(
        imageCapture: ImageCapture,
        outputOptions: ImageCapture.OutputFileOptions,
        photoFile: File, dateInfo: String
    ) = callbackFlow {

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Log.d(TAG, msg)
                    val data = savePhoto(savedUri.toString(), dateInfo)
                    Log.e(TAG, data.toString())
                    trySendBlocking(data)
                    Log.e(TAG, "trySendBlocking  called")

                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    Log.e(TAG, msg)
                }

            }

        )
        awaitClose()

    }

    private fun savePhoto(path: String, dateInfo: String): MediaData {
        val file = File(path)

        return MediaData(
            mediaName = file.name,
            mediaPath = path,
            mediaDate = dateInfo
        )
    }
}