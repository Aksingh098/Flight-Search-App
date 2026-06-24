package com.example.flightsearch.data

import com.example.flightsearch.data.local.AirportEntity
import com.example.flightsearch.domain.Airport

fun AirportEntity.toDomain(): Airport {
    return Airport(
        id = this.id,
        name = this.name,
        iataCode = this.iataCode,
        passengerCount = this.passengers
    )


}

fun List<AirportEntity>.toDomain(): List<Airport> {
    return this.map { it.toDomain() }
}