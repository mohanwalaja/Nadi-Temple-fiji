package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.data.local.entities.HoroscopeProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoroscopeDao {
    @Query("SELECT * FROM horoscope_profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<HoroscopeProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: HoroscopeProfileEntity): Long

    @Query("SELECT * FROM horoscope_profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): HoroscopeProfileEntity?

    @Delete
    suspend fun deleteProfile(profile: HoroscopeProfileEntity)

    @Query("DELETE FROM horoscope_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Long)
}
