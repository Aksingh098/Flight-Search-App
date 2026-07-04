package com.example.flightsearch.domain

data class Airport(
    val id: Int,
    val name: String,
    val iataCode: String,
    val passengerCount: Int
)

data class FlightRoute(
    val departureCode: String,
    val departureName: String,
    val destinationCode: String,
    val destinationName: String
)
