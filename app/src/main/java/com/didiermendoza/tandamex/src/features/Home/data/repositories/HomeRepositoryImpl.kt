package com.didiermendoza.tandamex.src.features.Home.data.repositories
import com.didiermendoza.tandamex.src.core.database.dao.TandaDao
import com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.api.HomeApiService
import com.didiermendoza.tandamex.src.features.Home.domain.entities.Tanda
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.didiermendoza.tandamex.src.features.Home.data.datasource.local.mapper.toDomain
import com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.mapper.toEntity

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApiService,
    private val tandaDao: TandaDao,
) : HomeRepository {

    override fun getAvailableTandas(): Flow<List<Tanda>> {
        return tandaDao.getAllTandas().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncTandas() {
        try {
            val response = api.getAvailableTandas()
            if (response.isSuccessful && response.body() != null) {
                val remoteTandas = response.body()!!
                tandaDao.replaceAllTandas(remoteTandas.map { it.toEntity() })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}