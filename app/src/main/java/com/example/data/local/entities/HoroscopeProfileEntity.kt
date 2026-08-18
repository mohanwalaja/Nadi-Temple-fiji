package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horoscope_profiles")
data class HoroscopeProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDay: Int,
    val birthHour: Int,
    val birthMinute: Int,
    val birthPlace: String,
    val lagnaRasiIndex: Int,
    val chandraRasiIndex: Int,
    val nakshatram: String,
    val pada: Int,
    val createdAt: Long = System.currentTimeMillis()
)
