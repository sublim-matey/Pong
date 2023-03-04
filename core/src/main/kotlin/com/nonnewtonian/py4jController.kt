package com.nonnewtonian

import com.badlogic.gdx.Input
import py4j.GatewayServer

class Py4jController(private val gameScreen: GameScreen) {
    val orders = ArrayDeque<Int>()

    init {
        println("Py4jController initialized")
    }

    fun test() {
        orders.add(3)
//        println("test")
    }

    fun paddle1Up() {
        orders.add(1)
//        gameScreen.playerControl(1)
    }

    fun paddle1Down() {
        orders.add(2)
//        gameScreen.playerControl(2)
    }
}
