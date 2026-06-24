package com.example.flightsearch.data.repository

import com.example.flightsearch.data.local.AirportEntity
import com.example.flightsearch.data.local.FSearchDao
import kotlinx.coroutines.flow.Flow

class OfflineFSearchRepository(private val searchDao: FSearchDao): FSearchRepository {
    override fun autoSuggestion(searchedQuery: String): Flow<List<AirportEntity>> = searchDao.autoSuggestion(searchedQuery)

}