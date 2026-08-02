package com.example.flightsearch.ui

import com.example.flightsearch.data.local.FavoriteEntity
import com.example.flightsearch.domain.Airport

import com.example.flightsearch.domain.FlightRoute

data class FlightSearchUiState(
    val searchedQuery: String = "",
    val searchExpanded: Boolean = false,
    val airportSuggestion: List<Airport> = emptyList(),
    val flightResults: List<FlightRoute> = emptyList(),
    val hasExecutedSearch: Boolean = false,
    val favorites: List<FlightRoute> = emptyList()

)
