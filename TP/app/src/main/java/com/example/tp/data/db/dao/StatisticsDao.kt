package com.example.tp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tp.data.db.entities.Statistics


@Dao
interface StatisticsDao {

    @Query("SELECT * FROM statistics")
    suspend fun getAllStatistics(): List<Statistics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(statistic: Statistics)

}
