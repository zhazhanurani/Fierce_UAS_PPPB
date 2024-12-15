package com.example.fierce.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fierce.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_table")
    fun getAllHistory(): LiveData<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)

    // delete all
    @Query("DELETE FROM history_table")
    fun deleteAllHistory()

}