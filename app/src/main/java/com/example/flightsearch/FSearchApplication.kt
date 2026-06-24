package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.local.AppContainer
import com.example.flightsearch.data.local.DefaultAppContainer

class FSearchApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}