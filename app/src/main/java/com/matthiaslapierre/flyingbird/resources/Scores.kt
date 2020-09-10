package com.matthiaslapierre.flyingbird.resources

import android.content.Context
import android.content.SharedPreferences




/**
 * Stores scores locally by using the Preferences API.
 */
class Scores {

    private val PREF_DEFAULT = "com.matthiaslapierre.flyingbird.PREF_DEFAULT"
    private val HIGH_SCORE = "high_score"

    /**
     * Gets the best score achieved.
     */
    fun highScore(context: Context): Int {
        val p: SharedPreferences = context.getSharedPreferences(
            PREF_DEFAULT,
            Context.MODE_PRIVATE
        )
        return p.getInt(HIGH_SCORE, 0)
    }

    /**
     * Checks if it's the new best score.
     */
    fun isNewBestScore(context: Context, score: Int): Boolean = score > highScore(context)

    /**
     * Records the new best score.
     */
    fun storeHighScore(context: Context, score: Int) {
        val p: SharedPreferences = context.getSharedPreferences(
            PREF_DEFAULT,
            Context.MODE_PRIVATE
        )
        p.edit().putInt(HIGH_SCORE, score).apply()
    }

}