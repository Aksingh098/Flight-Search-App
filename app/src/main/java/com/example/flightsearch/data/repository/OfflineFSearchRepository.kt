package com.example.flightsearch.data.repository

import com.example.flightsearch.data.local.AirportEntity
import com.example.flightsearch.data.local.FSearchDao
import com.example.flightsearch.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class OfflineFSearchRepository(private val searchDao: FSearchDao) : FSearchRepository {
    override fun autoSuggestion(searchedQuery: String): Flow<List<AirportEntity>> =
        searchDao.autoSuggestion(searchedQuery)

    override fun getAllDestination(departureCode: String): Flow<List<AirportEntity>> =
        searchDao.getAllDestinations(departureCode)

    override fun getSelectedAirport(departureCode: String): Flow<AirportEntity> =
        searchDao.getSelectedAirport(departureCode)

    override fun getFavorites(): Flow<List<FavoriteEntity>> = searchDao.getFavorites()

    override suspend fun addFavorite(favorite: FavoriteEntity) = searchDao.addFavorite(favorite)

    override suspend fun deleteFavorite(departureCode: String, destinationCode: String) =
        searchDao.deleteFavorite(departureCode, destinationCode)
}
