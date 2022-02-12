package com.kitmak.repo

import com.kitmak.network.local.TruckRouteEntity
import com.kitmak.network.local.dao.TruckRouteEntityDao
import com.kitmak.network.remote.TruckApis
import com.kitmak.network.remote.responses.TruckLocation
import com.kitmak.network.remote.responses.TruckRoute
import com.kitmak.network.remote.responses.toDb
import com.kitmak.network.remote.responses.toDomain
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface TruckRepo {
    suspend fun fetchTruckRoute(pageNumber: Int): List<TruckRoute>
    suspend fun fetchTruckLocation(): Flow<TruckResult<List<TruckLocation>>>
    fun latestTruckRoute(): Flow<List<TruckRoute>>
    suspend fun saveTruckRoute(list: List<TruckRoute>)
    suspend fun groupRoute(list: List<TruckRoute>): Map<String?, List<TruckRoute>>
    suspend fun deleteAllTruckRoute()
    suspend fun findRouteById(id: String): List<TruckRouteEntity>
}

class TruckRepoImpl @Inject constructor(
    private val truckApis: TruckApis,
    private val truckRouteEntityDao: TruckRouteEntityDao
) : TruckRepo {

    override suspend fun fetchTruckRoute(pageNumber: Int) =
        truckApis.fetchTruckRoute(pageNumber)

    override suspend fun fetchTruckLocation() =
        resultFlow(truckApis.fetchTruckLocation())

    override fun latestTruckRoute(): Flow<List<TruckRoute>> {
        return truckRouteEntityDao.getAllFlow()
            .flatMapMerge {
                flowOf(it.map { routes -> routes.toDomain() })
            }
    }

    override suspend fun findRouteById(id: String): List<TruckRouteEntity> {
        return truckRouteEntityDao.findById(id)
    }

    override suspend fun saveTruckRoute(list: List<TruckRoute>) {
        val items = list.map { it.toDb() }
        truckRouteEntityDao.insertAll(*items.toTypedArray())
    }

    override suspend fun groupRoute(list: List<TruckRoute>): Map<String?, List<TruckRoute>> {
        return list.groupBy {
            it.lineId
        }
    }

    override suspend fun deleteAllTruckRoute() {
        truckRouteEntityDao.deleteAll()
    }
}