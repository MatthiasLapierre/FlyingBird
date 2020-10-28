package com.matthiaslapierre.flyingbird.ui.game.sprite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect
import com.matthiaslapierre.flyingbird.Constants
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * Game Over screen.
 */
class GameOverSprite(
    private val context: Context,
    private val cache: Cache,
    private val score: Int,
    private val newBestScore: Boolean,
    private val bestScore: Int,
    private var splashInterface: SplashSprite.SplashInterface?
) : Sprite {

    private val titleDrawable: Drawable = cache.getDrawable(Cache.BG_GAME_OVER)
    private val bgFinalScoreDrawable: Drawable = cache.getDrawable(Cache.BG_FINAL_SCORE)
    private val newDrawable: Drawable = cache.getDrawable(Cache.IMG_NEW)
    private val playDrawable: Drawable = cache.getDrawable(Cache.IMG_PLAY_BTN)
    private val goldMedalDrawable: Drawable = cache.getDrawable(Cache.IMG_GOLD_MEDAL)
    private val titleWidth: Float = Utils.getDimenInPx(context, R.dimen.game_over_width)
    private val titleHeight: Float =
        titleDrawable.intrinsicHeight * titleWidth / titleDrawable.intrinsicWidth
    private val bgFinalScoreMarginTop: Float =
        Utils.getDimenInPx(context, R.dimen.final_score_margin_top)
    private val bgFinalScoreMarginBottom: Float =
        Utils.getDimenInPx(context, R.dimen.final_score_margin_bottom)
    private val bgFinalScoreWidth: Float = Utils.getDimenInPx(context, R.dimen.final_score_width)
    private val bgFinalScoreHeight: Float =
        bgFinalScoreDrawable.intrinsicHeight * bgFinalScoreWidth / bgFinalScoreDrawable.intrinsicWidth
    private val newWidth: Float = Utils.getDimenInPx(context, R.dimen.new_width)
    private val newHeight: Float =
        newDrawable.intrinsicHeight * newWidth / newDrawable.intrinsicWidth
    private val btnWidth: Float = Utils.getDimenInPx(context, R.dimen.btn_width)
    private val btnHeight: Float =
        btnWidth * playDrawable.intrinsicHeight / playDrawable.intrinsicWidth
    private val digitHeight: Float = Utils.getDimenInPx(context, R.dimen.score_small_digit_height)
    private var isAlive: Boolean = true

    override fun onDraw(canvas: Canvas, globalPaint: Paint, status: Int) {
        isAlive = status == Sprite.STATUS_GAME_OVER

        val screenWidth = canvas.width
        val screenHeight = canvas.height

        // "Game Over" title
        var y =
            (screenHeight / 2f) - (titleHeight / 2f) - (bgFinalScoreHeight / 2f) - (bgFinalScoreMarginTop / 2f) - (bgFinalScoreMarginBottom / 2f)
        titleDrawable.bounds = RectF(
            screenWidth / 2f - titleWidth / 2f,
            y - titleHeight / 2f,
            screenWidth / 2f + titleWidth / 2f,
            y + titleHeight / 2f
        ).toRect()
        titleDrawable.draw(canvas)

        // Score background
        y += titleHeight + bgFinalScoreMarginTop
        bgFinalScoreDrawable.bounds = RectF(
            screenWidth / 2f - bgFinalScoreWidth / 2f,
            y,
            screenWidth / 2f + bgFinalScoreWidth / 2f,
            y + bgFinalScoreHeight
        ).toRect()
        bgFinalScoreDrawable.draw(canvas)

        // Last score
        val scoreDeltaX =
            (screenWidth / 2f) + (bgFinalScoreWidth / 2f) - (bgFinalScoreWidth * .095f)
        val scoreBmp = Utils.generateScore(context, score, cache)
        val scoreRatio = digitHeight / scoreBmp.height
        val scoreX = scoreDeltaX - (scoreBmp.width * scoreRatio)
        val scoreY = y + bgFinalScoreHeight * .28f
        canvas.drawBitmap(
            scoreBmp,
            Rect(0, 0, scoreBmp.width, scoreBmp.height),
            RectF(
                scoreX,
                scoreY,
                scoreX + scoreBmp.width * scoreRatio,
                scoreY + scoreBmp.height * scoreRatio
            ),
            null
        )
        scoreBmp.recycle()

        // Best score
        val bestScoreBmp = Utils.generateScore(context, bestScore, cache)
        val bestScoreRatio = digitHeight / bestScoreBmp.height
        val bestScoreX = scoreDeltaX - (bestScoreBmp.width * bestScoreRatio)
        val bestScoreY = y + bgFinalScoreHeight * .65f
        canvas.drawBitmap(
            bestScoreBmp,
            Rect(0, 0, bestScoreBmp.width, bestScoreBmp.height),
            RectF(
                bestScoreX,
                bestScoreY,
                bestScoreX + bestScoreBmp.width * bestScoreRatio,
                bestScoreY + bestScoreBmp.height * bestScoreRatio
            ),
            null
        )
        bestScoreBmp.recycle()

        // New best score icon
        if (newBestScore) {
            val xNewBestScore =
                (screenWidth / 2f) + (bgFinalScoreWidth / 2f) - (bgFinalScoreWidth * .4f)
            val yNewBestScore = y + bgFinalScoreHeight * .51f
            newDrawable.bounds = RectF(
                xNewBestScore,
                yNewBestScore,
                xNewBestScore + newWidth,
                yNewBestScore + newHeight
            ).toRect()
            newDrawable.draw(canvas)
        }

        // Golden medal
        if (score > Constants.GOLDEN_SCORE) {
            val xGoldMedal =
                (screenWidth / 2f) - (bgFinalScoreWidth / 2f) + (bgFinalScoreWidth * .11f)
            val yGoldMedal = y + bgFinalScoreHeight * .37f
            val goldMedalSize = bgFinalScoreWidth * .20f
            goldMedalDrawable.bounds = RectF(
                xGoldMedal,
                yGoldMedal,
                xGoldMedal + goldMedalSize,
                yGoldMedal + goldMedalSize
            ).toRect()
            goldMedalDrawable.draw(canvas)
        }

        // Replay button
        y += bgFinalScoreHeight + bgFinalScoreMarginBottom
        playDrawable.bounds = RectF(
            screenWidth / 2f - btnWidth / 2f,
            y,
            screenWidth / 2f + btnWidth / 2f,
            y + btnHeight
        ).toRect()
        playDrawable.draw(canvas)
    }

    override fun isAlive(): Boolean = isAlive

    override fun isHit(sprite: Sprite): Boolean = false

    override fun getScore(): Int = 0

    fun onTap(x: Float, y: Float) {
        if (playDrawable.bounds.contains(x.toInt(), y.toInt())) {
            splashInterface?.onPlayBtnTapped()
        }
    }

    interface GameOverInterface {
        fun onReplayBtnTapped()
    }

}