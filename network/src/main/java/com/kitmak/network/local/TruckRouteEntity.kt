package com.kitmak.network.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TruckRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val lineId: String = "",
    val recyclingSunday: String? = null,
    val wgs84aX: String? = null,
    val wgs84aY: String? = null,
    val garbageThursday: String? = "",
    val city: String? = "",
    val latitude: String? = "",
    val lineName: String? = "",
    val memo: String? = null,
    val foodScrapsSunday: String? = null,
    val foodScrapsSaturday: String? = "",
    val foodScrapsWednesday: String? = null,
    val rank: String? = "",
    val foodScrapsMonday: String? = "",
    val village: String? = "",
    val recyclingSaturday: String? = "",
    val foodScrapsFriday: String? = "",
    val longitude: String? = "",
    val recyclingTuesday: String? = "",
    val recyclingThursday: String? = "",
    val garbageMonday: String? = "",
    val recyclingFriday: String? = "",
    val garbageTuesday: String? = "",
    val recyclingMonday: String? = "",
    val garbageWednesday: String? = null,
    val foodScrapsTuesday: String? = "",
    val recyclingWednesday: String? = null,
    val twd97X: String? = "",
    val twd97Y: String? = "",
    val garbageFriday: String? = "",
    val name: String? = "",
    val garbageSunday: String? = null,
    val foodScrapsThursday: String? = "",
    val time: String? = "",
    val garbageSaturday: String? = ""
)