package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect
import com.matthiaslapierre.flyingbird.Constants.UNDEFINED
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Manages the clouds in the sky.
 */
class CloudSprite(
    context: Context,
    cache: Cache,
    private var x: Float
): Sprite {

    private val drawable: Drawable = cache.getRandomCloud()
    private var y: Float = UNDEFINED
    private var cloudWidth: Float = UNDEFINED
    private var cloudHeight: Float = UNDEFINED
    private val cloudMinHeight = Utils.getDimenInPx(context, R.dimen.cloud_min_height)
    private val cloudMaxHeight = Utils.getDimenInPx(context, R.dimen.cloud_max_height)
    private var speed: Float = UNDEFINED
    private var minSpeed: Float = Utils.getDimenInPx(context, R.dimen.cloud_min_speed)
    private var maxSpeed: Float = Utils.getDimenInPx(context, R.dimen.cloud_max_speed)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if(y == UNDEFINED) {
            val screenHeight = canvas.height
            y = Utils.getRandomInt(screenHeight / 3).toFloat()
            // random speed
            speed = Utils.getRandomInt((maxSpeed - minSpeed).toInt()) + minSpeed
            // random size
            cloudHeight = Utils.getRandomInt((cloudMaxHeight - cloudMinHeight).toInt()) + cloudMinHeight
            cloudWidth = drawable.intrinsicWidth * cloudHeight / drawable.intrinsicHeight
        }
        isAlive = (status != Sprite.STATUS_NOT_STARTED && (x + cloudWidth > 0f))

        if(status == Sprite.STATUS_PLAY) {
            x -= speed
        }

        drawable.bounds = RectF(
            x,
            y,
            x + cloudWidth,
            y + cloudHeight
        ).toRect()
        drawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

}