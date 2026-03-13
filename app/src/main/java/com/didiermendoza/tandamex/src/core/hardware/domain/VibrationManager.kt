package com.didiermendoza.tandamex.src.core.hardware.domain

interface VibrationManager {
    fun vibrate(durationMillis: Long = 100)
    fun vibrateError()
}