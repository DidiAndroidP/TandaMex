package com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model

data class ReviewRequestDto(
    val tandaId: Int,
    val rating: Int,
    val comment: String
)