package com.example.collegealart.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.collegealart.data.dao.AlertDAO
import com.example.collegealart.data.table.AlertTable

@Database(entities = [AlertTable::class], version = 2, exportSchema = false)
abstract class AlertDatabase : RoomDatabase()
{
    abstract fun alertDao(): AlertDAO
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AlertDatabase? = null

        fun getDatabase(context: Context): AlertDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlertDatabase::class.java,
                    "College_alert"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}