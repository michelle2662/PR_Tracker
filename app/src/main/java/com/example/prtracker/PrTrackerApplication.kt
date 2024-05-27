package com.example.prtracker

import android.app.Application

class PrTrackerApplication : Application() {

    override fun onCreate(){
        super.onCreate()
        Graph.provide(this)
    }

}