package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.Constants.UNDEFINED
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toRect

/**
 * Display the pieces to be collected.
 */
class CoinSprite(
    context: Context,
    cache: Cache,
    private var x: Float
): Sprite {

    private val drawable: Drawable = cache.getDrawable(Cache.IMG_COIN)
    private val coinWidth: Float = Utils.getDimenInPx(context, R.dimen.coin_width)
    private val coinHeight: Float = Utils.getDimenInPx(context, R.dimen.coin_width)
    private var y: Float = UNDEFINED
    private var speed: Float = Utils.getDimenInPx(context, R.dimen.coin_speed)
    private var alpha: Int = 255
    private var alphaSpeed: Int = 3
    private val minAlpha = 128
    private val maxAlpha = 255
    private var isWon: Boolean = false
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = (status != Sprite.STATUS_NOT_STARTED && !isWon && (x + coinWidth > 0f))
        if(y == UNDEFINED) {
            val screenHeight = canvas.height
            y = screenHeight / 4f + Utils.getRandomInt(screenHeight / 2) - coinHeight / 2f
        }

        if (status == Sprite.STATUS_NOT_STARTED) {
            return
        }
        if(status == Sprite.STATUS_PLAY) {
            x -= speed
            // Animate the coin
            alpha -= alphaSpeed
            if(alpha < minAlpha) {
                alpha = minAlpha
                alphaSpeed = -alphaSpeed
            } else if(alpha > maxAlpha) {
                alpha = maxAlpha
                alphaSpeed = -alphaSpeed
            }
        }
        drawable.alpha = alpha
        drawable.bounds = getRect()
        drawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = (isAlive() && sprite is BirdSprite
            && getRect().intersect(sprite.getRect())).also { isHiten ->
        if(isHiten) {
            isWon = true
        }
    }

    override fun getScore(): Int = if (isWon) 1 else 0

    private fun getRect(): Rect = RectF(
        x,
        y,
        x + coinWidth,
        y + coinHeight
    ).toRect()

}