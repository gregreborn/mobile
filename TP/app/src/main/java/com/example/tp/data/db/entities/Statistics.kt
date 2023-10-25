package com.example.tp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics")
data class Statistics(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "card_value") val cardValue: String,
    @ColumnInfo(name = "count") var count: Int
)
