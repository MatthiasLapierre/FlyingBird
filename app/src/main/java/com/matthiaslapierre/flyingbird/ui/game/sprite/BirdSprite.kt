package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect
import com.matthiaslapierre.flyingbird.Constants.UNDEFINED
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * The BirdSprite class will be in charge of managing the bird in our game.
 */
class BirdSprite(
    context: Context,
    cache: Cache
) : Sprite {

    private var count: Int = 0
    private val drawables: Array<Drawable> = cache.getBirdDrawable()
    private val birdHeight: Float = Utils.getDimenInPx(context, R.dimen.bird_height)
    private val birdWidth: Float = birdHeight * drawables[0].intrinsicWidth / drawables[0].intrinsicHeight
    private val groundHeight: Float = Utils.getDimenInPx(context, R.dimen.ground_height)
    private var x: Float = UNDEFINED
    private var y: Float = UNDEFINED
    private val acceleration: Float = Utils.getDimenInPx(context, R.dimen.bird_acceleration)
    private var currentSpeed: Float = 0f
    private val tapSpeed: Float = Utils.getFloat(context, R.dimen.bird_tap_speed)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status != Sprite.STATUS_NOT_STARTED
        val maxY = canvas.height - birdHeight - groundHeight
        val minY = 0f
        if(x == UNDEFINED && y == UNDEFINED) {
            x = canvas.width / 4 - birdWidth / 2 // 25%
            y = canvas.height / 2 - birdHeight / 2 // 50%
        }

        if(count >= 2) {
            count = 0
        }

        if(status != Sprite.STATUS_NOT_STARTED) {
            // Reproduce the effect of gravity on our bird
            y += currentSpeed
            synchronized (this) {
                currentSpeed += acceleration
            }
        }
        if(y < minY) {
            // Ensure that the bird remains within the limits of the screen by resetting its
            // current position to 0 if it reaches the top of the screen.
            y = minY
        } else if(y > maxY) {
            // The same is done for its position at the bottom of the screen
            y = maxY
        }

        val birdDrawable: Drawable = if(status == Sprite.STATUS_PLAY) {
            // Animate the bird. Simulate the flight of the bird.
            drawables[count++]
        } else {
            drawables[0]
        }
        birdDrawable.bounds = getRect()
        birdDrawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

    fun getRect(): Rect = RectF(
        x,
        y,
        x + birdWidth,
        y + birdHeight
    ).toRect()

    fun jump() {
        synchronized (this) {
            currentSpeed = tapSpeed
            y -= currentSpeed
        }
    }

}