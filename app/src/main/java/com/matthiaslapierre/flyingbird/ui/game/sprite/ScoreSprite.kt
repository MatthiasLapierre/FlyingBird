package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Display the score.
 */
class ScoreSprite(
    context: Context
) : Sprite {

    companion object {
        private const val TEXT_COLOR: Int = 0xFFFFFFFF.toInt()
        private const val SHADOW_COLOR: Int = 0x50000000
        private const val SHADOW_RADIUS = 2f
        private const val SHADOW_DELTA = 2f
    }

    var currentScore: Int = 0
    private val marginTop: Float = Utils.getDimenInPx(context, R.dimen.score_margin)
    private val textPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = Utils.getDimenInPx(context, R.dimen.score_size)
        paint.color = TEXT_COLOR
        paint.setShadowLayer(SHADOW_RADIUS, SHADOW_DELTA, SHADOW_DELTA, SHADOW_COLOR)
        paint
    }

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if (status == Sprite.STATUS_NOT_STARTED) {
            return
        }
        canvas.drawText(currentScore.toString(), canvas.width / 2f, marginTop, textPaint)
    }

    override fun isAlive(): Boolean = true

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

}