package com.example.flightsearch.data.repository

import com.example.flightsearch.data.local.AirportEntity
import com.example.flightsearch.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FSearchRepository {
    fun autoSuggestion(searchedQuery: String): Flow<List<AirportEntity>>

    fun getAllDestination(departureCode: String): Flow<List<AirportEntity>>
    fun getSelectedAirport(departureCode: String): Flow<AirportEntity>

    fun getFavorites(): Flow<List<FavoriteEntity>>

    suspend fun addFavorite(favorite: FavoriteEntity)

    suspend fun deleteFavorite(departureCode: String, destinationCode: String)
}