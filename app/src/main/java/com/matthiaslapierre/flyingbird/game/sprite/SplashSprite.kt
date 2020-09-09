package com.matthiaslapierre.flyingbird.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toRect

class SplashSprite(
    context: Context
): Sprite {

    private val hintDrawable: Drawable = Utils.getDrawable(context, R.drawable.bg_splash)
    private val hintWidth: Float = Utils.getDimenInPx(context, R.dimen.hint_width)
    private val hintHeight: Float = hintWidth * hintDrawable.intrinsicHeight / hintDrawable.intrinsicWidth
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status == Sprite.STATUS_NOT_STARTED
        val screenWidth = canvas.width
        val screenHeight = canvas.height
        hintDrawable.bounds = RectF(
            screenWidth / 2f - hintWidth / 2f,
            screenHeight / 2f - hintHeight / 2f,
            screenWidth / 2f + hintWidth / 2f,
            screenHeight / 2f + hintHeight / 2f
        ).toRect()
        hintDrawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0


}