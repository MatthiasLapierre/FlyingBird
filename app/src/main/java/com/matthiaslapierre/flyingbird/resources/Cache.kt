package com.matthiaslapierre.flyingbird.resources

import android.content.Context
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
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
        const val IMG_PLAY_BTN = 6
        const val IMG_NEW = 7
        const val IMG_0 = 8
        const val IMG_1 = 9
        const val IMG_2 = 10
        const val IMG_3 = 11
        const val IMG_4 = 12
        const val IMG_5 = 13
        const val IMG_6 = 14
        const val IMG_7 = 15
        const val IMG_8 = 16
        const val IMG_9 = 17
        const val IMG_GOLD_MEDAL = 18
        const val IMG_CLOUD_0 = 19
        const val IMG_CLOUD_1 = 20
        const val BG_GROUND_UP = 21
        const val BG_GROUND_DOWN = 22
        const val BG_DAY = 23
        const val BG_NIGHT = 24
        const val BG_SPLASH = 25
        const val BG_GAME_OVER = 26
        const val BG_FINAL_SCORE = 27
    }

    private val drawables: Array<Drawable?> = arrayOfNulls(28)

    fun load(context: Context) {
        val drawableResIds = intArrayOf(
            R.drawable.img_bird_red_1,
            R.drawable.img_bird_red_2,
            R.drawable.img_bird_red_3,
            R.drawable.img_pipe_up,
            R.drawable.img_pipe_down,
            R.drawable.img_coin,
            R.drawable.img_play_btn,
            R.drawable.img_new,
            R.drawable.img_0,
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,
            R.drawable.img_9,
            R.drawable.img_gold_medal,
            R.drawable.img_cloud_0,
            R.drawable.img_cloud_1,
            R.drawable.bg_ground_up,
            R.drawable.bg_ground_down,
            R.drawable.bg_general_day,
            R.drawable.bg_general_night,
            R.drawable.bg_splash,
            R.drawable.bg_game_over,
            R.drawable.bg_final_score
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

    fun getDigit(digit: Int): Drawable = getDrawable(IMG_0 + digit)

    fun getBirdDrawable(): Array<Drawable> = arrayOf(
        IMG_BIRD_1,
        IMG_BIRD_2,
        IMG_BIRD_3
    ).map { getDrawable(it) }.toTypedArray()

    fun getRandomCloud(): Drawable = getDrawable(IMG_CLOUD_0 + Utils.getRandomInt(2))

}