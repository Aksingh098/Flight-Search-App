package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FSearchDao {

    @Query("SELECT * FROM airport WHERE name LIKE :searchedQuery OR iata_code LIKE :searchedQuery ORDER BY passengers DESC ")
    fun AutoSuggestion(searchedQuery: String): Flow<List<Airport>>
}