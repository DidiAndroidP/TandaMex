package com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.api

import com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model.TandaResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface HomeApiService {
    @GET("tandas/available")
    suspend fun getAvailableTandas(): Response<List<TandaResponseDto>>
}