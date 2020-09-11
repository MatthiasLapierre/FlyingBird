package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Displays the score.
 */
class ScoreSprite(
    private val context: Context,
    private val cache: Cache
) : Sprite {

    var currentScore: Int = 0
    private val marginTop: Float = Utils.getDimenInPx(context, R.dimen.score_margin)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if (status == Sprite.STATUS_NOT_STARTED) {
            isAlive = false
            return
        }
        val scoreBmp = Utils.generateScore(
            context,
            currentScore,
            cache
        )
        val x = canvas.width / 2f - scoreBmp.width / 2f
        val y = marginTop
        canvas.drawBitmap(scoreBmp, x, y, null)
        scoreBmp.recycle()
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

}