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
 * Home screen.
 */
class SplashSprite(
    context: Context,
    cache: Cache,
    private var splashInterface: SplashInterface?
): Sprite {

    private val hintDrawable: Drawable = cache.getDrawable(Cache.BG_SPLASH)
    private val playDrawable: Drawable = cache.getDrawable(Cache.IMG_PLAY_BTN)
    private val hintWidth: Float = Utils.getDimenInPx(context, R.dimen.hint_width)
    private val hintHeight: Float = hintWidth * hintDrawable.intrinsicHeight / hintDrawable.intrinsicWidth
    private val btnWidth: Float = Utils.getDimenInPx(context, R.dimen.btn_width)
    private val getReadyMarginBottom: Float = Utils.getDimenInPx(context, R.dimen.get_ready_margin_bottom)
    private val btnHeight: Float = btnWidth * playDrawable.intrinsicHeight / playDrawable.intrinsicWidth
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status == Sprite.STATUS_NOT_STARTED

        val screenWidth = canvas.width.toFloat()
        val screenHeight = canvas.height.toFloat()

        hintDrawable.bounds = RectF(
            screenWidth / 2f - hintWidth / 2f,
            screenHeight / 2f - hintHeight / 2f,
            screenWidth / 2f + hintWidth / 2f,
            screenHeight / 2f + hintHeight / 2f
        ).toRect()
        hintDrawable.draw(canvas)

        playDrawable.bounds = RectF(
            screenWidth / 2f - btnWidth / 2f,
            screenHeight / 2f + hintHeight / 2f + getReadyMarginBottom,
            screenWidth / 2f + btnWidth / 2f,
            screenHeight / 2f + hintHeight / 2f + getReadyMarginBottom + btnHeight
        ).toRect()
        playDrawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

    fun onTap(x: Float, y: Float) {
        if(playDrawable.bounds.contains(x.toInt(), y.toInt())) {
            splashInterface?.onPlayBtnTapped()
        }
    }

    interface SplashInterface {
        fun onPlayBtnTapped()
    }

}