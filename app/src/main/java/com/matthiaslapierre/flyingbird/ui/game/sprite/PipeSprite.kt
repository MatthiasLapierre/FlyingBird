package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect
import com.matthiaslapierre.flyingbird.Constants.UNDEFINED
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils
import kotlin.math.max
import kotlin.math.min

/**
 * Manages the pipes displayed on the screen that must be avoided by the player.
 */
class PipeSprite(
    context: Context,
    cache: Cache,
    var x: Float,
    val lastBlockY: Float?
): Sprite {

    private val drawablePipeUp: Drawable = cache.getDrawable(Cache.IMG_PIPE_UP)
    private val drawablePipeDown: Drawable = cache.getDrawable(Cache.IMG_PIPE_DOWN)
    private val speed: Float = Utils.getDimenInPx(context, R.dimen.sprite_speed)
    private val pipeWidth: Float = Utils.getDimenInPx(context, R.dimen.pipe_width)
    private val gap: Float = Utils.getDimenInPx(context, R.dimen.pipe_gap)
    private val swing: Float = Utils.getDimenInPx(context, R.dimen.pipe_swing)
    private val groundHeight: Float = Utils.getDimenInPx(context, R.dimen.ground_height)
    private val minUpHeight = Utils.getDimenInPx(context, R.dimen.pipe_min)
    private var screenHeight: Float = 0f
    private var upHeight: Float = UNDEFINED
    private var scored: Boolean = false
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if(upHeight == UNDEFINED) {
            val screenHeight = canvas.height
            var min = minUpHeight + gap / 2f
            var max = screenHeight - min - groundHeight - (gap / 2f)
            if(lastBlockY != null) {
                min = max(min, lastBlockY - swing)
                max = min(max, lastBlockY + swing)
            }
            upHeight = Utils.getRandomInt((max - min).toInt()) + min
        }
        isAlive = (status != Sprite.STATUS_NOT_STARTED && x + pipeWidth >= 0f)
        screenHeight = canvas.height.toFloat()

        if (status == Sprite.STATUS_NOT_STARTED) {
            return
        }
        if(status == Sprite.STATUS_PLAY) {
            x -= speed
        }
        drawablePipeUp.bounds = getTopPipeRect()
        drawablePipeUp.draw(canvas)
        drawablePipeDown.bounds = getBottomPipeRect()
        drawablePipeDown.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = (isAlive() && sprite is BirdSprite
            && ((getTopPipeRect().intersect(sprite.getRect()))
            || (getBottomPipeRect().intersect(sprite.getRect()))))

    override fun getScore(): Int = if (scored) 1 else 0

    private fun getTopPipeRect() = RectF(
        x,
        0f,
        x + pipeWidth,
        upHeight - gap / 2f
    ).toRect()

    private fun getBottomPipeRect() = RectF(
        x,
        upHeight + gap / 2f,
        x + pipeWidth,
        screenHeight - groundHeight
    ).toRect()

}