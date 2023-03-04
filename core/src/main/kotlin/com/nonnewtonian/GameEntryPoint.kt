package com.nonnewtonian

import py4j.GatewayServer

class GameEntryPoint(private val gameScreen: GameScreen) {
    private val toPrint = "Hello World!"
    private val py4jController = Py4jController(gameScreen)


}
