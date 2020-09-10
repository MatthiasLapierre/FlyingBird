package com.matthiaslapierre.flyingbird.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.matthiaslapierre.flyingbird.resources.Cache
import kotlin.random.Random


/**
 * Utility methods.
 */
object Utils {

    fun getDimenInPx(context: Context, @DimenRes id: Int): Float = context.resources.getDimensionPixelSize(id).toFloat()

    fun getFloat(context: Context, @DimenRes id: Int): Float = context.resources.getDimension(id)

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable = ContextCompat.getDrawable(context, id)!!

    fun getRandomInt(maxValue: Int): Int = Random.nextInt(0, maxValue)

    fun drawScore(
        points: Int,
        cache: Cache,
        canvas: Canvas,
        x: Float,
        y: Float,
        digitWidth: Float,
        digitHeight: Float,
        digitMargin: Float
    ) {
        val digits = points.toDigits()
        var currentX = x
        digits.reversedArray().forEach { digit ->
            val drawable = cache.getDigit(digit)
            drawable.bounds = RectF(currentX, y, currentX + digitWidth, y + digitHeight).toRect()
            drawable.draw(canvas)
            currentX += digitWidth + digitMargin
        }
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