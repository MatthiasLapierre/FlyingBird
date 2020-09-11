package com.matthiaslapierre.flyingbird.ui

import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.matthiaslapierre.flyingbird.App
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.resources.Scores
import com.matthiaslapierre.flyingbird.ui.game.DrawingThread
import com.matthiaslapierre.flyingbird.resources.SoundEngine
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Main Activity.
 */
class GameActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener,
    DrawingThread.GameInterface {

    companion object {
        private const val TAG = "GameActivity"
    }

    private lateinit var cache: Cache
    private lateinit var soundEngine: SoundEngine
    private lateinit var scores: Scores

    private lateinit var holder: SurfaceHolder
    private val globalPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint
    }
    private var surfaceCreated: Boolean = false
    private var drawingThread: DrawingThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Inject dependencies.
        val appContainer = (application as App).appContainer
        cache = appContainer.cache
        soundEngine = appContainer.soundEngine
        scores = appContainer.scores

        surfaceView.keepScreenOn = true
        holder = surfaceView.holder
        surfaceView.setZOrderOnTop(true)
        surfaceView.setOnTouchListener(this)
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            drawingThread?.onTap(event.x, event.y)
        }
        return false
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceCreated = true
        startDrawingThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceCreated = false
        stopDrawingThread()
    }

    /**
     * Once the SurfaceHolder object is created, we start our dedicated Thread that we call
     * DrawingThread.
     */
    private fun startDrawingThread() {
        stopDrawingThread()
        drawingThread = DrawingThread(
            applicationContext,
            holder,
            globalPaint,
            cache,
            scores,
            this@GameActivity
        )
        drawingThread!!.start()
    }

    /**
     * Thread is stopped when the callback surfaceDestroyed is called telling us that the
     * SurfaceHolder object instance is destroyed. This happens when the application is
     * paused or stopped, for example.
     */
    private fun stopDrawingThread() {
        drawingThread?.interrupt()

        try {
            drawingThread?.join()
        } catch (e: InterruptedException) {
            Log.e(TAG, "Failed to interrupt the drawing thread")
        }
        drawingThread?.clean()
        drawingThread = null
    }

    override fun onWing() {
        runOnUiThread {
            soundEngine.playWing()
        }
    }

    override fun onGameStart() {
        runOnUiThread {
            soundEngine.playSwooshing()
        }
    }

    override fun onGetPoint() {
        runOnUiThread {
            soundEngine.playGetPoint()
        }
    }

    override fun onHit() {
        runOnUiThread {
            soundEngine.playHit()
        }
    }

    override fun onGameOver() {
        runOnUiThread {
            soundEngine.playGameOver()
        }
    }

}