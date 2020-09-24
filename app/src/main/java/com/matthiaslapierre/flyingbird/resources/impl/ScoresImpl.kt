package com.matthiaslapierre.flyingbird.resources.impl

import android.content.Context
import android.content.SharedPreferences
import com.matthiaslapierre.flyingbird.resources.Scores

/**
 * Stores scores locally by using the Preferences API.
 */
class ScoresImpl: Scores {

    companion object {
        private const val PREF_DEFAULT = "com.matthiaslapierre.flyingbird.PREF_DEFAULT"
        private const val HIGH_SCORE = "high_score"
    }

    /**
     * Gets the best score achieved.
     */
    override fun highScore(context: Context): Int {
        val p: SharedPreferences = context.getSharedPreferences(
            PREF_DEFAULT,
            Context.MODE_PRIVATE
        )
        return p.getInt(HIGH_SCORE, 0)
    }

    /**
     * Checks if it's the new best score.
     */
    override fun isNewBestScore(context: Context, score: Int): Boolean = score > highScore(context)

    /**
     * Records the new best score.
     */
    override fun storeHighScore(context: Context, score: Int) {
        val p: SharedPreferences = context.getSharedPreferences(
            PREF_DEFAULT,
            Context.MODE_PRIVATE
        )
        p.edit().putInt(HIGH_SCORE, score).apply()
    }

}