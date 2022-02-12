package com.kitmak.network.remote.responses

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
    lineId(清運路線編號)、
    car(車牌號碼)、
    time(回報時間)、
    location(所在位址)、
    longitude(經度)、
    latitude(緯度)、
    cityId(行政區代號)、
    cityName(行政區名稱)
 */

@Serializable
data class TruckLocation(
    val lineId: String?,
    val car: String?,
    val time: String?,
    val location: String?,
    val longitude: String?,
    val latitude: String?,
    val cityId: String?,
    val cityName: String?,
) {
    val latitudeDouble = latitude?.toDouble()
    val longitudeDouble = longitude?.toDouble()
}

data class SafeTruckLocation(
    val lineId: String,
    val car: String,
    val cityName: String,
    val time: String,
    val location: String,
    val longitude: Double,
    val latitude: Double,
) {
    val readableTime = LocalDateTime.parse(time, intputFormatter).format(formatter)
}

val formatter = DateTimeFormatter.ofPattern("HH:mm")
val intputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")


fun TruckLocation.safe(): SafeTruckLocation? {
    return car?.let {
        lineId?.let {
            cityName?.let {
                time?.let {
                    location?.let {
                        longitudeDouble?.let {
                            latitudeDouble?.let {
                                SafeTruckLocation(
                                    lineId,
                                    car,
                                    cityName,
                                    time,
                                    location,
                                    longitudeDouble,
                                    latitudeDouble
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



