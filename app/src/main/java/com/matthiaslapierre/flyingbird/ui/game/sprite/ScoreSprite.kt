package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toDigits

/**
 * Display the score.
 */
class ScoreSprite(
    context: Context,
    private val cache: Cache
) : Sprite {

    var currentScore: Int = 0
    private val marginTop: Float = Utils.getDimenInPx(context, R.dimen.score_margin)
    private val digitWidth: Float = Utils.getDimenInPx(context, R.dimen.score_digit_width)
    private val digitHeight: Float = Utils.getDimenInPx(context, R.dimen.score_digit_height)
    private val digitMargin: Float = Utils.getDimenInPx(context, R.dimen.score_digit_margin)

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if (status == Sprite.STATUS_NOT_STARTED) {
            return
        }
        val digits = currentScore.toDigits()
        val scoreWidth = (digits.size * digitWidth) + (digitMargin * (digits.size - 1))
        val x = canvas.width / 2f - scoreWidth / 2f
        val y = marginTop
        Utils.drawScore(
            currentScore,
            cache,
            canvas,
            x,
            y,
            digitWidth,
            digitHeight,
            digitMargin
        )
    }

    override fun isAlive(): Boolean = true

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

}