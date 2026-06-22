package com.example.flightsearch.data.repository

import com.example.flightsearch.data.local.AirportEntity
import kotlinx.coroutines.flow.Flow

interface FSearchRepository {
    fun autoSuggestion(searchQuery: String): Flow<List<AirportEntity>>

}