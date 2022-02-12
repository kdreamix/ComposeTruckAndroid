package com.kitmak.network.remote

import com.kitmak.network.remote.responses.TruckLocation
import com.kitmak.network.remote.responses.TruckRoute
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject


interface TruckApis {
    suspend fun fetchTruckRoute(pageNumber: Int): List<TruckRoute>
    suspend fun fetchTruckLocation(): List<TruckLocation>
}

class TruckApisImpl @Inject constructor(private val client: HttpClient) : TruckApis {
    override suspend fun fetchTruckRoute(pageNumber: Int) =
        client.get<List<TruckRoute>>(EndPoints.TRUCK_ROUTE) {
            parameter("page", pageNumber)
            parameter("size", 1000)
        }

    override suspend fun fetchTruckLocation() =
        client.get<List<TruckLocation>>(EndPoints.TRUCK_LCOATIONS) {
            parameter("page", 0)
            parameter("size", 100)
        }
}