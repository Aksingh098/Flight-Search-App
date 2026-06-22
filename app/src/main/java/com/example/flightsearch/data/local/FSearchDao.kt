package com.example.flightsearch.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FSearchDao {

    @Query("SELECT * FROM airport WHERE name LIKE :searchedQuery OR iata_code LIKE :searchedQuery ORDER BY passengers DESC ")
    fun autoSuggestion(searchedQuery: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getAllDestinations(departureCode: String): Flow<List<AirportEntity>>
}