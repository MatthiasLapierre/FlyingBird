package com.matthiaslapierre.flyingbird.resources

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * Memory caching to improve performance.
 */
interface Cache  {

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

    fun load(context: Context)

    fun getDrawable(index: Int): Drawable

    fun getDigit(digit: Int): Drawable

    fun getBirdDrawable(): Array<Drawable>

    fun getRandomCloud(): Drawable

}