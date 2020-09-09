package com.matthiaslapierre.flyingbird.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toRect

class GameOverSprite(
    context: Context
): Sprite {

    private val titleDrawable: Drawable = Utils.getDrawable(context, R.drawable.bg_game_over)
    private val titleWidth: Float = Utils.getDimenInPx(context, R.dimen.game_over_width)
    private val titleHeight: Float = titleDrawable.intrinsicHeight * titleWidth / titleDrawable.intrinsicWidth
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status == Sprite.STATUS_GAME_OVER
        val screenWidth = canvas.width
        val screenHeight = canvas.width
        titleDrawable.bounds = RectF(
            screenWidth / 2f - titleWidth / 2f,
            screenHeight / 4f,
            screenWidth / 2f + titleWidth / 2f,
            screenHeight / 4f + titleHeight
        ).toRect()
        titleDrawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

}