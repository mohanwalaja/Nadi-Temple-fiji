package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.SavedTempleEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TempleEventDao {
    @Query("SELECT * FROM saved_temple_events")
    fun getAllSavedEvents(): Flow<List<SavedTempleEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvent(event: SavedTempleEventEntity)

    @Query("DELETE FROM saved_temple_events WHERE eventId = :eventId")
    suspend fun removeEvent(eventId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_temple_events WHERE eventId = :eventId)")
    suspend fun isEventSaved(eventId: String): Boolean
}
