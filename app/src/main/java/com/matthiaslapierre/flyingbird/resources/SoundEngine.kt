package com.matthiaslapierre.flyingbird.resources

import android.content.Context

/**
 * In order to make our Flying Bird clone more attractive, it is essential to reproduce
 * the sounds present in the original game. These sounds occur when the game starts,
 * when the bird scores a point or when the user hits the screen and the bird gets taller.
 */
interface SoundEngine {

    /**
     * Loads sound effects.
     */
    fun load(context: Context)

    /**
     * Unloads sound effects.
     */
    fun release()

    fun playGameOver()

    fun playHit()

    fun playGetPoint()

    fun playSwooshing()

    fun playWing()

}