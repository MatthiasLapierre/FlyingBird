package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Manages the ground and its horizontal scrolling to give the illusion of the bird’s
 * progress on the screen.
 */
class GroundSprite(
    context: Context,
    cache: Cache
): Sprite {

    private val speed: Float = Utils.getDimenInPx(context, R.dimen.sprite_speed)
    private var layerX: Float = 0f
    private var layerY: Float = 0f
    private val groundDown: Drawable = cache.getDrawable(Cache.BG_GROUND_DOWN)
    private val groundUp: Drawable = cache.getDrawable(Cache.BG_GROUND_UP)
    private val groundWidth: Float = Utils.getDimenInPx(context, R.dimen.ground_width)
    private val groundHeight: Float = Utils.getDimenInPx(context, R.dimen.ground_height)
    private val groupUpHeight: Float = groundHeight - Utils.getDimenInPx(context, R.dimen.ground_margin)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status != Sprite.STATUS_NOT_STARTED

        val screenWidth = canvas.width.toFloat()
        val screenHeight = canvas.height.toFloat()

        if (status == Sprite.STATUS_PLAY) {
            layerX -= speed
        }
        if (layerX < -groundWidth) {
            layerX = 0f
        }
        layerY = screenHeight - groundHeight

        groundDown.bounds = RectF(
            0f,
            layerY,
            screenWidth,
            screenHeight
        ).toRect()
        groundDown.draw(canvas)


        for (x in layerX.toInt() until screenWidth.toInt() step groundWidth.toInt()) {
            groundUp.bounds = RectF(
                x.toFloat(),
                layerY,
                x + groundWidth,
                layerY + groupUpHeight
            ).toRect()
            groundUp.draw(canvas)
        }
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = (sprite is BirdSprite
            && sprite.getRect().bottom >= layerY)

    override fun getScore(): Int = 0


}