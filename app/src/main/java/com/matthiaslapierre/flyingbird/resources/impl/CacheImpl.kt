package com.matthiaslapierre.flyingbird.resources.impl

import android.content.Context
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Memory caching to improve performance.
 */
class CacheImpl: Cache  {

    private val drawables: Array<Drawable?> = arrayOfNulls(28)

    override fun load(context: Context) {
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

    override fun getDrawable(index: Int): Drawable = drawables[index]!!

    override fun getDigit(digit: Int): Drawable = getDrawable(Cache.IMG_0 + digit)

    override fun getBirdDrawable(): Array<Drawable> = arrayOf(
        Cache.IMG_BIRD_1,
        Cache.IMG_BIRD_2,
        Cache.IMG_BIRD_3
    ).map { getDrawable(it) }.toTypedArray()

    override fun getRandomCloud(): Drawable = getDrawable(Cache.IMG_CLOUD_0 + Utils.getRandomInt(2))

}