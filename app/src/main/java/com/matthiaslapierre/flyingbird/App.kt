package com.matthiaslapierre.flyingbird

import android.app.Application

class App: Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()
        // Load resources once.
        appContainer.cache.load(applicationContext)
        appContainer.soundEngine.load(applicationContext)
    }
}