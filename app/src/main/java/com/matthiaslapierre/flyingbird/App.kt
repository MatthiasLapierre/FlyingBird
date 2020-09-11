package com.matthiaslapierre.flyingbird

import android.app.Application

/**
 * Flying Bird implementation inspired by the medium article titled "Recreating The Famous Flappy Bird Game For
 * Android".
 * @see https://medium.com/swlh/recreating-the-famous-flappy-bird-game-for-android-eb10ecd9de4e
 */
class App: Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()
        // Load resources once.
        appContainer.cache.load(applicationContext)
        appContainer.soundEngine.load(applicationContext)
    }
}