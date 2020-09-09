package com.matthiaslapierre.flyingbird.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toRect

class BirdSprite(
    context: Context
) : Sprite {
    companion object {
        private val DRAWABLE_BIRD: Array<Int> = arrayOf(
            R.drawable.img_bird_red_1,
            R.drawable.img_bird_red_2,
            R.drawable.img_bird_red_3
        )
        private const val UNSPECIFIED = -999f
    }

    private var count: Int = 0
    private val drawables: Array<Drawable> by lazy {
        DRAWABLE_BIRD.map { Utils.getDrawable(context, it) }.toTypedArray()
    }
    private val birdWidth: Float by lazy {
        birdHeight * drawables[0].intrinsicWidth / drawables[0].intrinsicHeight
    }
    private val birdHeight: Float = Utils.getDimenInPx(context, R.dimen.bird_height)
    private val groundHeight: Float = Utils.getDimenInPx(context, R.dimen.ground_height)
    private var x: Float = UNSPECIFIED
    private var y: Float = UNSPECIFIED
    private val acceleration: Float = Utils.getDimenInPx(context, R.dimen.bird_acceleration)
    private var currentSpeed: Float = 0f
    private val tapSpeed: Float = Utils.getFloat(context, R.dimen.bird_tap_speed)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status != Sprite.STATUS_NOT_STARTED
        val maxY = canvas.height - birdHeight - groundHeight
        val minY = 0f
        if(x == UNSPECIFIED && y == UNSPECIFIED) {
            x = canvas.width / 4 - birdWidth / 2 // 25%
            y = canvas.height / 2 - birdHeight / 2 // 50%
        }

        if(count >= 2) {
            count = 0
        }

        if(status != Sprite.STATUS_NOT_STARTED) {
            y += currentSpeed
            synchronized (this) {
                currentSpeed += acceleration
            }
        }
        if(y < minY) {
            y = minY
        } else if(y > maxY) {
            y = maxY
        }

        val birdDrawable: Drawable = if(status == Sprite.STATUS_PLAY) {
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