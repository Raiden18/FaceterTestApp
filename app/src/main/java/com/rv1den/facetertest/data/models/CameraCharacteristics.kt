package com.rv1den.facetertest.data.models

import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES
import android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CameraMetadata.*
import android.os.Build
import androidx.annotation.RequiresApi
import android.hardware.camera2.CameraCharacteristics as AndroidCameraCharacteristics

class CameraCharacteristics(private val cameraCharacteristics: AndroidCameraCharacteristics) {
    private val capabilities =
        cameraCharacteristics.get(REQUEST_AVAILABLE_CAPABILITIES)
    private val outputFormats = cameraCharacteristics.get(SCALER_STREAM_CONFIGURATION_MAP)!!.outputFormats

    val isBackwardCompatible = capabilities?.contains(REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) ?: false

    val isCapableForRaw = capabilities!!.contains(REQUEST_AVAILABLE_CAPABILITIES_RAW)
    val isOutputRawSensor = outputFormats.contains(ImageFormat.RAW_SENSOR)

    val isCapableDepthOutPut = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        capabilities!!.contains(REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT)
    } else {
        false
    }
    val isCapableDepthJpeg = outputFormats.contains(ImageFormat.DEPTH_JPEG)


}