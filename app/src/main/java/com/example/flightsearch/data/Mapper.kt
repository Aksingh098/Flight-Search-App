package com.example.flightsearch.data

import com.example.flightsearch.data.local.AirportEntity
import com.example.flightsearch.data.local.FavoriteEntity
import com.example.flightsearch.domain.Airport
import com.example.flightsearch.domain.Favorite

fun AirportEntity.toDomain(): Airport {
    return Airport(
        id = this.id,
        name = this.name,
        iataCode = this.iataCode,
        passengerCount = this.passengers
    )


}

fun List<AirportEntity>.toAirportDomain(): List<Airport> {
    return this.map { it.toDomain() }
}

fun FavoriteEntity.toDomain(): Favorite {
    return Favorite(
        id = this.id,
        departureCode = this.departureCode,
        destinationCode = this.destinationCode
    )

}

fun List<FavoriteEntity>.toFavoriteDomain(): List<Favorite> {
    return this.map { it.toDomain() }
}