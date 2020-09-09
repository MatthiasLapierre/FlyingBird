package com.matthiaslapierre.flyingbird.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import com.matthiaslapierre.flyingbird.Constants

/**
 * In order to make our Flappy Bird clone more attractive, it is essential to reproduce
 * the sounds present in the original game. These sounds occur when the game starts,
 * when the bird scores a point or when the user hits the screen and the bird gets taller.
 */
class SoundEngine {

    companion object{
        private const val SOUND_DIE = 0
        private const val SOUND_HIT = 1
        private const val SOUND_POINT = 2
        private const val SOUND_SWOOSHING = 3
        private const val SOUND_WING = 4
    }

    private val soundPool: SoundPool
    private val sounds: Array<Int?> = arrayOfNulls(5)

    init {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool
                .Builder()
                .setMaxStreams(Constants.SOUND_MAX_STREAMS)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            SoundPool(
                Constants.SOUND_MAX_STREAMS,
                AudioManager.STREAM_MUSIC,
                0
            )
        }
    }

    fun load(context: Context) {
        Thread(Runnable {
            val assets = context.resources.assets
            sounds[SOUND_DIE] = soundPool.load(assets.openFd("die.ogg"), 1)
            sounds[SOUND_HIT] = soundPool.load(assets.openFd("hit.ogg"), 1)
            sounds[SOUND_POINT] = soundPool.load(assets.openFd("point.ogg"), 1)
            sounds[SOUND_SWOOSHING] = soundPool.load(assets.openFd("swooshing.ogg"), 1)
            sounds[SOUND_WING] = soundPool.load(assets.openFd("wing.ogg"), 1)
        }).start()
    }

    fun playGameOver() = playSound(SOUND_DIE)
    fun playHit() = playSound(SOUND_HIT)
    fun playGetPoint() = playSound(SOUND_POINT)
    fun playSwooshing() = playSound(SOUND_SWOOSHING)
    fun playWing() = playSound(SOUND_WING)

    private fun playSound(index: Int) {
        sounds[index]?.let { soundId ->
            soundPool.play(soundId, 0.5f, 0.5f, 1, 0, 1f)
        }
    }

}