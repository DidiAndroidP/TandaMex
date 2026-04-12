package com.didiermendoza.tandamex.src.core.status

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    data class Success(val message: String) : UploadStatus()
    data class Error(val message: String) : UploadStatus()
}