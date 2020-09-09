package com.matthiaslapierre.flyingbird

import com.matthiaslapierre.flyingbird.util.DrawableCache
import com.matthiaslapierre.flyingbird.util.SoundEngine

/**
 * To solve the issue of reusing objects, you can create your own dependencies container class
 * that you use to get dependencies. All instances provided by this container can be public.
 * Because these dependencies are used across the whole application, they need to be placed in
 * a common place all activities can use: the application class.
 * @see https://developer.android.com/training/dependency-injection/manual
 */
class AppContainer {
    val drawableHelper = DrawableCache()
    val soundPoolHelper = SoundEngine()
}