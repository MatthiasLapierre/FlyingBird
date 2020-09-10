package com.matthiaslapierre.flyingbird.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.matthiaslapierre.flyingbird.resources.Cache
import kotlin.random.Random

import android.graphics.Canvas
import com.matthiaslapierre.flyingbird.R


/**
 * Utility methods.
 */
object Utils {

    fun getDimenInPx(context: Context, @DimenRes id: Int): Float = context.resources.getDimensionPixelSize(id).toFloat()

    fun getFloat(context: Context, @DimenRes id: Int): Float = context.resources.getDimension(id)

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable = ContextCompat.getDrawable(context, id)!!

    fun getRandomInt(maxValue: Int): Int = Random.nextInt(0, maxValue)

    fun generateScore(
        context: Context,
        points: Int,
        cache: Cache
    ): Bitmap {
        val digits = points.toDigits()
        var currentX = 0f
        val digitWidth = getDimenInPx(context, R.dimen.score_digit_width)
        val height = getDimenInPx(context, R.dimen.score_digit_height)
        val digitMargin = getDimenInPx(context, R.dimen.score_digit_margin)
        val width = (digits.size * digitWidth) + (digitMargin * (digits.size - 1))
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        digits.reversedArray().forEach { digit ->
            val drawable = cache.getDigit(digit)
            drawable.bounds = RectF(currentX, 0f, currentX + digitWidth, height).toRect()
            drawable.draw(canvas)
            currentX += digitWidth + digitMargin
        }
        return bitmap
    }

}

fun RectF.toRect() = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

fun Int.toDigits(): Array<Int> {
    val digits = mutableListOf<Int>()
    var i = this
    if(i == 0) {
        digits.add(0)
    } else {
        while (i > 0) {
            digits.add(i % 10)
            i /= 10
        }
    }
    return digits.toTypedArray()
}