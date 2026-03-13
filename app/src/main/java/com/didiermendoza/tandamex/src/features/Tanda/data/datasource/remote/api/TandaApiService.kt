package com.didiermendoza.tandamex.src.features.Tanda.data.datasource.remote.api

import com.didiermendoza.tandamex.src.features.Home.data.datasources.remote.model.TandaResponseDto // Si este lo usas en Home, puedes importarlo aquí o moverlo
import com.didiermendoza.tandamex.src.features.Tanda.data.datasources.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TandaApiService {
    @POST("tandas/")
    suspend fun createTanda(@Body request: CreateTandaRequestDto): Response<TandaResponseDto>

    @GET("tandas/available")
    suspend fun getAvailableTandas(): Response<List<TandaResponseDto>>
    @GET("tandas/{id}")
    suspend fun getTandaDetail(@Path("id") tandaId: Int): Response<TandaDetailDto>
    @GET("tandas/{id}/members")
    suspend fun getTandaMembers(@Path("id") tandaId: Int): Response<List<TandaMemberDto>>
    @POST("tandas/{id}/join")
    suspend fun joinTanda(@Path("id") tandaId: Int): Response<JoinResponseDto>
    @POST("tandas/{id}/leave")
    suspend fun leaveTanda(@Path("id") tandaId: Int): Response<JoinResponseDto>
    @POST("tandas/{id}/start")
    suspend fun startTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>
    @POST("tandas/{id}/finish")
    suspend fun finishTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>
    @DELETE("tandas/{id}/delete")
    suspend fun deleteTanda(@Path("id") tandaId: Int): Response<GenericMessageDto>
    @POST("tandas/{id}/schedule")
    suspend fun generateSchedule(@Path("id") tandaId: Int): Response<ScheduleResponseDto>
    @GET("tandas/{id}/summary")
    suspend fun getTandaSummary(@Path("id") tandaId: Int): Response<TandaSummaryDto>
}