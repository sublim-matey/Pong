@file:JvmName("Lwjgl3Launcher")

package com.nonnewtonian.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nonnewtonian.Game
import com.nonnewtonian.GameScreen
import com.nonnewtonian.Py4jController

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Game(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Pong")
        setWindowedMode(640, 480)
        setForegroundFPS(30)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
