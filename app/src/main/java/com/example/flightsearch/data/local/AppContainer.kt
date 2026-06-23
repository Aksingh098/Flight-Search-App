package com.example.flightsearch.data.local

import android.content.Context
import com.example.flightsearch.data.repository.FSearchRepository
import com.example.flightsearch.data.repository.OfflineFSearchRepository

interface AppContainer {
    val fSearchRepository: FSearchRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer{

    override val fSearchRepository: FSearchRepository by lazy{
        FlightSearchDataBase.getDatabase(context).FSearchDao().let { fSearchDao ->
            OfflineFSearchRepository(fSearchDao)
        }

    }
}