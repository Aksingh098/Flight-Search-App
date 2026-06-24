package com.example.flightsearch.ui

import com.example.flightsearch.domain.Airport

data class FlightSearchUiState(
    val searchedQuery: String = "",
    val searchExpanded: Boolean = false,
    val airportSuggestion: List<Airport> = emptyList()
)
