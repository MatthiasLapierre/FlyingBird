package com.matthiaslapierre.flyingbird.ui.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.view.SurfaceHolder
import com.matthiaslapierre.flyingbird.Constants
import com.matthiaslapierre.flyingbird.R
import com.matthiaslapierre.flyingbird.resources.Cache
import com.matthiaslapierre.flyingbird.resources.Scores
import com.matthiaslapierre.flyingbird.ui.game.sprite.*
import com.matthiaslapierre.flyingbird.util.Utils

/**
 * The evolution of any game in computer is carried out within what is commonly called
 * the Game Loop. As part of the implementation of our Flappy Bird game, we implement
 * it by creating a Thread separated from the UI rendering Thread of the Android application.
 * This separated Thread will be in charge of managing the Game Loop.
 */
class DrawingThread(
    private val context: Context,
    private val holder: SurfaceHolder,
    private val globalPaint: Paint,
    private val cache: Cache,
    private val scores: Scores,
    private var gameInterface: GameInterface?
): Thread(), SplashSprite.SplashInterface, GameOverSprite.GameOverInterface {

    companion object {
        private const val MIN_PIPES = 60
    }

    var currentStatus: Int = Sprite.STATUS_NOT_STARTED
    var points: Int = 0

    private var workSprites: MutableList<Sprite> = mutableListOf()
    private var birdSprite: BirdSprite? = null
    private var scoreSprite: ScoreSprite? = null
    private var groundSprite: GroundSprite? = null
    private var splashSprite: SplashSprite? = null
    private var gameOverSprite: GameOverSprite? = null
    private var lastPipeSprite: PipeSprite? = null
    private var countPipes: Int = 0
    private val pipeWidth = Utils.getDimenInPx(context, R.dimen.pipe_width)
    private val coinWidth = Utils.getDimenInPx(context, R.dimen.coin_width)
    private val pipeInterval = Utils.getDimenInPx(context, R.dimen.pipe_interval)
    private var newBestScore: Boolean = false

    override fun run() {
        super.run()

        /*
        In our DrawingThread, we loop as long as the Thread is active.
        First, we take care of the rendering of our game. To do this, we obtain a reference to the
        Canvas of our SurfaceHolder object by calling its lockCanvas method.
        We then empty the content of this Canvas before iterating on all the elements of our
        game that we want to return to the screen. These elements being our Sprites.
         */
        while (!interrupted()) {
            val startTime = System.currentTimeMillis()
            val canvas = holder.lockCanvas()
            val screenWidth = canvas.width.toFloat()

            try  {
                cleanCanvas(canvas)

                /*
                This iteration is performed via an Iterator object and we use it to delete Sprites
                considered as no longer alive. This work is encapsulated within a try / finally block.
                In the finally part, we ask that the updates we have made on the Canvas be posted on
                the SurfaceHolder via a call to the unlockCanvasAndPost method with the current
                instance of Canvas passed as a parameter.
                 */
                val iterator: MutableListIterator<Sprite> = workSprites.listIterator()
                while (iterator.hasNext()) {
                    val sprite = iterator.next()
                    if(sprite.isAlive()) {
                        sprite.onDraw(canvas, globalPaint, currentStatus)
                    } else {
                        if(sprite is PipeSprite) {
                            scoreSprite?.currentScore = ++points
                            countPipes--
                        }
                        iterator.remove()
                    }
                }
            } finally {
                holder.unlockCanvasAndPost(canvas)
            }

            /*
            You also have noticed that the rendering time is measured before comparing this time
            to a constant called GAP. This constant allows us to add, if necessary, a delay to
            avoid that the rendering phase of the Game Loop be too fast.
             */
            val duration = System.currentTimeMillis() - startTime
            val gap = Constants.MS_PER_FRAME - duration
            if(gap > 0) {
                try {
                    sleep(gap)
                } catch (e: Exception) {
                    break
                }
            }

            when(currentStatus) {
                Sprite.STATUS_NOT_STARTED -> {
                    // Show the home screen
                    if(splashSprite == null || !splashSprite!!.isAlive()) {
                        splashSprite = SplashSprite(context, cache, this@DrawingThread)
                        workSprites.add(splashSprite!!)
                    }
                }
                Sprite.STATUS_GAME_OVER -> {
                    // Show the Game Over screen.
                    if(gameOverSprite == null || !gameOverSprite!!.isAlive()) {
                        gameOverSprite = GameOverSprite(
                            context,
                            cache,
                            points,
                            newBestScore,
                            scores.highScore(context),
                            this@DrawingThread
                        )
                        workSprites.add(gameOverSprite!!)
                    }
                }
                Sprite.STATUS_PLAY -> {
                    // Show the game.
                    // Draw the score, the ground, the bird...
                    if(scoreSprite == null || !scoreSprite!!.isAlive()) {
                        scoreSprite = ScoreSprite(context, cache)
                        workSprites.add(scoreSprite!!)
                    }
                    if(groundSprite == null || !groundSprite!!.isAlive()) {
                        groundSprite = GroundSprite(context, cache)
                        workSprites.add(groundSprite!!)
                    }
                    if(birdSprite == null || !birdSprite!!.isAlive()) {
                        birdSprite = BirdSprite(context, cache)
                        workSprites.add(birdSprite!!)
                    }

                    // don't forget the obstacles and the rewards !
                    var nextPipeX = screenWidth
                    if(lastPipeSprite != null) {
                        nextPipeX = lastPipeSprite!!.x + pipeInterval
                    }
                    val cloudSprites = mutableListOf<CloudSprite>()
                    while(countPipes < MIN_PIPES) {
                        lastPipeSprite = PipeSprite(
                            context,
                            cache,
                            nextPipeX,
                            lastPipeSprite?.lastBlockY
                        )
                        val lastCoinSprite = CoinSprite(
                            context,
                            cache,
                            nextPipeX + pipeWidth + pipeInterval / 2f - coinWidth / 2f
                        )
                        val cloudSprite = CloudSprite(
                            context,
                            cache,
                            nextPipeX
                        )
                        cloudSprites.add(cloudSprite)
                        workSprites.add(0, lastPipeSprite!!)
                        workSprites.add(0, lastCoinSprite)

                        nextPipeX += pipeWidth + pipeInterval
                        countPipes++
                    }
                    workSprites.addAll(0, cloudSprites)


                    /*
                    We can now complete our Game Loop with game updates about Sprites.
                    We will add the following code to detect if the game should stop because
                    the player has lost:
                     */
                    val iterator: MutableListIterator<Sprite> = workSprites.listIterator()
                    while (iterator.hasNext()) {
                        val sprite = iterator.next()
                        if(sprite.isHit(birdSprite!!)) {
                            when(sprite) {
                                is PipeSprite, is GroundSprite -> {
                                    gameInterface?.onHit()
                                    newBestScore = scores.isNewBestScore(context, points)
                                    if(newBestScore) {
                                        scores.storeHighScore(context, points)
                                    }
                                    /*
                                    the game is over and we will have to display the end screen to
                                    the player the next time the Game Loop passes.
                                     */
                                    currentStatus = Sprite.STATUS_GAME_OVER
                                    gameInterface?.onGameOver()
                                }
                                is CoinSprite -> {
                                    points += sprite.getScore()
                                    scoreSprite?.currentScore = points
                                    gameInterface?.onGetPoint()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPlayBtnTapped() {
        startGame()
    }

    override fun onReplayBtnTapped() {
        startGame()
    }

    /**
     * The goal of Flappy Bird is to allow the player to progress the bird by tapping on the screen,
     * so we must act within the onTouch method of the OnTouchListener interface that our main
     * activity inherits. It is in this method that we will interact with the player when they type
     * on the screen.
     */
    fun onTap(x: Float, y: Float) {
        when(currentStatus) {
            Sprite.STATUS_NOT_STARTED -> {
                /*
                If the game has not yet started, a first tap on the screen will allow you to
                change its status to the STATUS_PLAY constant. Have a good game!
                 */
                splashSprite?.onTap(x, y)
            }
            Sprite.STATUS_PLAY -> {
                /*
                In case the game is in progress, we call the onTap method of the BirdSprite
                to increment its current speed which causes the bird to rise on the screen.
                This rise on the bird’s screen fights against the effect of gravity by preventing
                it from falling to the ground.
                 */
                birdSprite?.jump()
                gameInterface?.onWing()
            }
            Sprite.STATUS_GAME_OVER -> {
                /*
                Finally, if the game is over, we call the onTap method of the GameOverSprite object
                which will return a constant as output allowing us to know which part of the end
                screen the player to touch in order to react accordingly either to start a new game
                or to share the successful score via social networks for example.
                 */
                gameOverSprite?.onTap(x, y)
            }
        }
    }

    /**
     * Prevents memory leakage (some objects were not releasing memory).
     */
    fun clean() {
        gameInterface = null
    }

    /**
     * Cleans all sprites. Resets the score.
     */
    private fun resetGame() {
        workSprites = mutableListOf()
        scoreSprite = null
        groundSprite = null
        birdSprite = null
        splashSprite = null
        gameOverSprite = null
        lastPipeSprite = null
        countPipes = 0
        points = 0
    }

    private fun startGame() {
        resetGame()
        currentStatus = Sprite.STATUS_PLAY
        gameInterface?.onGameStart()
    }

    private fun cleanCanvas(canvas: Canvas) {
        canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR)
    }

    interface GameInterface {
        fun onWing()
        fun onGameStart()
        fun onGetPoint()
        fun onHit()
        fun onGameOver()
    }

}