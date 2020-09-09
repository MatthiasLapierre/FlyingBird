package com.matthiaslapierre.flyingbird.util

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.random.Random


/**
 * Utility methods.
 */
object Utils {

    fun getDimenInPx(context: Context, @DimenRes id: Int): Float = context.resources.getDimensionPixelSize(id).toFloat()

    fun getFloat(context: Context, @DimenRes id: Int): Float = context.resources.getDimension(id)

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable = ContextCompat.getDrawable(context, id)!!

    fun getRandomInt(maxValue: Int): Int = Random.nextInt(0, maxValue)

}

fun RectF.toRect() = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())