package com.kitmak.network.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kitmak.network.local.TruckRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckRouteEntityDao {
    @Query("SELECT * FROM TruckRouteEntity")
    suspend fun getAll(): List<TruckRouteEntity>

    @Query("SELECT * FROM TruckRouteEntity")
    fun getAllFlow(): Flow<List<TruckRouteEntity>>

    @Query("SELECT * FROM TruckRouteEntity WHERE lineId IN (:truckRouteEntityIds)")
    suspend fun loadAllByIds(truckRouteEntityIds: IntArray): List<TruckRouteEntity>

    @Query("SELECT * FROM TruckRouteEntity WHERE lineId LIKE :id")
    suspend fun findById(id: String): List<TruckRouteEntity>

    @Insert
    suspend fun insertAll(vararg truckRouteEntity: TruckRouteEntity)

    @Delete
    suspend fun delete(truckRouteEntity: TruckRouteEntity)

    @Query("DELETE FROM TruckRouteEntity")
    suspend fun deleteAll()
}