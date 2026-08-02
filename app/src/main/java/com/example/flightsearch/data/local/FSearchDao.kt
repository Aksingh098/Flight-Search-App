package com.example.flightsearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FSearchDao {

    @Query(
        """
    SELECT * FROM airport 
    WHERE name LIKE '%' || :searchedQuery || '%' 
       OR iata_code LIKE '%' || :searchedQuery || '%' 
    ORDER BY passengers DESC
"""
    )
    fun autoSuggestion(searchedQuery: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getAllDestinations(departureCode: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport WHERE iata_code = :departureCode LIMIT 1")
    fun getSelectedAirport(departureCode: String): Flow<AirportEntity>

    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Insert
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)

    @Query("SELECT * FROM airport")
    fun getAirports(): Flow<List<AirportEntity>>
}