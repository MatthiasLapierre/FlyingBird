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
import com.matthiaslapierre.flyingbird.resources.SoundEngine
import com.matthiaslapierre.flyingbird.ui.game.GameProcessor
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Main Activity.
 */
class GameActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener,
    GameProcessor.GameInterface {

    companion object {
        private const val TAG = "GameActivity"
    }

    private lateinit var cache: Cache
    private lateinit var soundEngine: SoundEngine
    private lateinit var scores: Scores
    private lateinit var gameProcessor: GameProcessor
    private lateinit var holder: SurfaceHolder
    private val globalPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint
    }
    private var surfaceCreated: Boolean = false
    private var drawingThread: Thread? = null

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

        // Initialize the GameProcessor.
        gameProcessor = GameProcessor(
            applicationContext,
            holder,
            globalPaint,
            cache,
            scores,
            this@GameActivity
        )
    }

    override fun onResume() {
        super.onResume()
        // Resume the game.
        gameProcessor.resume()
    }

    override fun onPause() {
        // Pause the game.
        gameProcessor.pause()
        super.onPause()
    }

    override fun onDestroy() {
        // Unload resources.
        soundEngine.release()
        gameProcessor.release()
        super.onDestroy()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceCreated = true
        // After the surface is created, we start the game loop.
        startDrawingThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Stop the game loop after destroying the surface.
        surfaceCreated = false
        stopDrawingThread()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            gameProcessor.onTap(event.x, event.y)
        }
        return false
    }

    /**
     * Once the SurfaceHolder object is created, we start our dedicated Thread that we call
     * DrawingThread.
     */
    private fun startDrawingThread() {
        stopDrawingThread()
        drawingThread = Thread(Runnable {
            gameProcessor.execute()
        })
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