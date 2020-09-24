package com.matthiaslapierre.flyingbird.resources

import android.content.Context

/**
 * Stores scores locally by using the Preferences API.
 */
interface Scores {

    /**
     * Gets the best score achieved.
     */
    fun highScore(context: Context): Int

    /**
     * Checks if it's the new best score.
     */
    fun isNewBestScore(context: Context, score: Int): Boolean

    /**
     * Records the new best score.
     */
    fun storeHighScore(context: Context, score: Int)

}