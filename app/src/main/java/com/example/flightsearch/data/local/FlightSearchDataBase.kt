package com.example.flightsearch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AirportEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FlightSearchDataBase : RoomDatabase() {
    abstract fun FSearchDao(): FSearchDao

    companion object {
        @Volatile
        private var Instance: FlightSearchDataBase? = null

        fun getDatabase(context: Context): FlightSearchDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlightSearchDataBase::class.java, "flight_database")
                    .createFromAsset("database/flight_search.db")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }

}