package com.didiermendoza.tandamex.src.features.Home.data.repositories

import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository

class HomeRepositoryImpl(
    private val api: TandaMexApi
) : HomeRepository {

    override suspend fun getTandas(): Result<List<Tanda>> {
        return try {
            val response = api.getAvailableTandas()
            if (response.isSuccessful && response.body() != null) {
                val tandas = response.body()!!.map { it.toDomain() }
                Result.success(tandas)
            } else {
                Result.failure(Exception("Error ${response.code()}: No se pudieron cargar las tandas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}