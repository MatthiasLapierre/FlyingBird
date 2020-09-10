package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils
import com.matthiaslapierre.flyingbird.util.toRect

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

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        if (status == Sprite.STATUS_PLAY || status == Sprite.STATUS_NOT_STARTED) {
            layerX -= speed
        }
        if (layerX < -groundWidth) {
            layerX = 0f
        }
        groundDown.draw(canvas)
        layerY = canvas.height - groundHeight
        val screenWidth = canvas.width
        for (x in layerX.toInt() until screenWidth step groundWidth.toInt()) {
            groundUp.bounds = RectF(
                x.toFloat(),
                layerY,
                x + groundWidth,
                layerY + groupUpHeight
            ).toRect()
            groundUp.draw(canvas)
        }
    }

    override fun isAlive(): Boolean = true

    override fun isHit(sprite: Sprite): Boolean = (sprite is BirdSprite
            && sprite.getRect().bottom >= layerY)

    override fun getScore(): Int = 0


}