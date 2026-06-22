package com.example.flightsearch.domain

data class Airport(
    val id: Int,
    val name: String,
    val iataCode: String,
    val passengerCount: Int
)
