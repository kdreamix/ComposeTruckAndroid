package com.kitmak.network.local.di

import android.content.Context
import androidx.room.Room
import com.kitmak.network.local.AppDatabase
import com.kitmak.network.local.dao.TruckRouteEntityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideDb(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTruckDao(database: AppDatabase): TruckRouteEntityDao = database.truckRouteEntityDao()

}