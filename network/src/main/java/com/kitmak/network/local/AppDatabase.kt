package com.kitmak.network.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kitmak.network.local.dao.TruckRouteEntityDao

@Database(entities = [TruckRouteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truckRouteEntityDao(): TruckRouteEntityDao
}