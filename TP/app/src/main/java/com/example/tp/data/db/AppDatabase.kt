package com.example.tp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tp.data.db.dao.StatisticsDao
import com.example.tp.data.db.entities.Statistics

@Database(entities = [Statistics::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statisticsDao(): StatisticsDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blackjack_database"
                ).build()
                instance = db
                db
            }
        }
    }
}
