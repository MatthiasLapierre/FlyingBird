package com.matthiaslapierre.flyingbird.resources

import android.content.Context
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.ui.game.sprite.BirdSprite
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Memory caching to improve performance.
 */
class Cache  {

    companion object {
        const val IMG_BIRD_1 = 0
        const val IMG_BIRD_2 = 1
        const val IMG_BIRD_3 = 2
        const val IMG_PIPE_UP = 3
        const val IMG_PIPE_DOWN = 4
        const val IMG_COIN = 5
        const val BG_GROUND_UP = 6
        const val BG_GROUND_DOWN = 7
        const val BG_DAY = 8
        const val BG_NIGHT = 9
        const val BG_SPLASH = 10
        const val BG_GAME_OVER = 11
    }

    private val drawables: Array<Drawable?> = arrayOfNulls(12)

    fun load(context: Context) {
        val drawableResIds = intArrayOf(
            R.drawable.img_bird_red_1,
            R.drawable.img_bird_red_2,
            R.drawable.img_bird_red_3,
            R.drawable.img_pipe_up,
            R.drawable.img_pipe_down,
            R.drawable.img_coin,
            R.drawable.bg_ground_up,
            R.drawable.bg_ground_down,
            R.drawable.bg_general_day,
            R.drawable.bg_general_night,
            R.drawable.bg_splash,
            R.drawable.bg_game_over
        )
        drawableResIds.forEachIndexed{ index, resId ->
            drawables[index] =
                Utils.getDrawable(
                    context,
                    resId
                )
        }
    }

    fun getDrawable(index: Int): Drawable = drawables[index]!!

    fun getBirdDrawable(): Array<Drawable> = arrayOf(
        IMG_BIRD_1,
        IMG_BIRD_2,
        IMG_BIRD_3
    ).map { getDrawable(it) }.toTypedArray()

}