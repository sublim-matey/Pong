package com.nonnewtonian

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.*
import ktx.graphics.color
import ktx.graphics.use
import java.lang.Thread.sleep
import py4j.GatewayServer

class GameScreen(val game: Game) : KtxScreen {
    val py4jController = Py4jController(this)

    private val ballImage = game.assets.loadOnDemand<Texture>("ball.png")
    private val paddleImage = game.assets.loadOnDemand<Texture>("paddle.png")
    private val ball = Circle(640f, 360f, 13f)
    private val paddle1 = Rectangle(100f, 160f, 5f, 65f)
    private val paddle2 = Rectangle(1280 - 100f, 360f, 5f, 65f)
    private val board = game.assets.loadOnDemand<Texture>("board.png")

    private val camera = OrthographicCamera().apply { setToOrtho(false, 1280f, 720f) }

    private var paddleM1 = 0f
    private var paddleM2 = 0f

    private var ballMX = 200f
    private var ballMY = 0f

    private var score1 = 0
    private var score2 = 0

    init {
        val gatewayServer = GatewayServer(py4jController)
        gatewayServer.start()
        println("Gateway Server Started")
        println(gatewayServer.listeningPort)
    }


    fun test() {
        println("test")
    }


    fun spawnBall() {
        ball.x = 640f
        ball.y = 360f
        ballMX = 200f
        ballMY = 0f
        paddle2.y = 360f
        paddleM2 = 0f
    }

    override fun render(delta: Float) {
        camera.update() //update camera matrix
        game.batch.projectionMatrix = camera.combined


        //Doesn't seem fair lol
        // Also there's a bug with the paddle and top of screen

        //CPU Paddle Control
        paddleM2 += controlPaddleValue(paddle2, ball, paddleM2)
//        paddleM1 += controlPaddleValue(paddle1, ball, paddleM1)

        if (py4jController.orders.isNotEmpty()) {
            when (py4jController.orders.removeFirst()) {
                1 -> paddleM1 += 10f
                2 -> paddleM1 -= 10f
                3 -> println("test")
            }
        }


        //Player Input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            paddleM1 += 10f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            paddleM1 -= 10f
        }


        //Score Detection
        if(ball.x < 0) {
            score2++
            spawnBall()
        }
        if (ball.x > 1280) {
            score1++
            spawnBall()
        }


        // Collision Detection
        if(Intersector.overlaps(ball, paddle1)) {
            ballMX *= -1.05f
            ballMY += paddleM1 * 0.2f
            ball.x = paddle1.x +20
//            println("ball.x = ${ball.x} paddle1.x = ${paddle1.x} time = ${TimeUtils.millis()} ballMX = $ballMX")
        }

        if(Intersector.overlaps(ball, paddle2)) {
            ballMX *= -1.05f
            ballMY += paddleM2 * 0.2f
            ball.x = paddle2.x - 20
        }

        if (ball.y < 0) {
            ballMY *= -1
            ball.y = 0f
        }

        if (ball.y > 720 - 13) {
            ballMY *= -1
            ball.y = 720f - 20f
        }

        // begin a new batch and draw board, etc
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        game.batch.use {
            paddle1.y += paddleM1 * delta
            paddle2.y += paddleM2 * delta

            game.batch.draw(board.asset, 0f, 0f, 1280f, 720f)
            game.font.draw(game.batch, score1.toString(), 100f, 720f - 100f)
            game.font.draw(game.batch, score2.toString(), 1280 - 100f, 720f - 100f)
            game.batch.draw(paddleImage.asset, 100f, paddle1.y, 11f, 65f)
            game.batch.draw(paddleImage.asset, 1280 - 100f, paddle2.y, 11f, 65f)
            game.batch.draw(ballImage.asset, ball.x - 12, ball.y - 12, 26f, 26f)
//            println(computerVisionOutput(this@GameScreen, delta))
        }

        if (paddle1.y < 0) {
            paddle1.y = 0f
            paddleM1 *= -1 * 0.2f
        }
        if (paddle1.y > 720 - 65) {
            paddle1.y = 720f - 65f
            paddleM1 *= -1 * 0.2f
        }
        if (paddle2.y < 0) {
            paddle2.y = 0f
            paddleM2 *= -1 * 0.2f
        }
        if (paddle2.y > 720 - 65) {
            paddle2.y = 720f - 65f
            paddleM2 *= -1 * 0.2f
        }
        // update ball position
        ball.x += ballMX * delta
        ball.y += ballMY * delta
    }

    override fun dispose() {
    }


    // Does the AI necessarily need paddle's X values?
    fun computerVisionOutput(gS: GameScreen, delta: Float): String {
        return "${gS.ball.x},${gS.ball.y},${gS.paddle1.x},${gS.paddle1.y},${gS.paddle2.x},${gS.paddle2.y}," +
                "${gS.ballMX},${gS.ballMY},${gS.score1},${gS.score2},$delta"
    }

    //returns value to add to paddle momentum
    fun controlPaddleValue(paddle: Rectangle, ball: Circle, paddleM:Float): Float {
        return if (ball.y < paddle.y - 200) {
            -10f
        } else if(ball.y > paddle.y + 200) {
            10f
        } else if (ball.y < paddle.y - 10) {
            -2f
        } else if(ball.y > paddle.y + 10) {
            2f
        } else {
            paddleM * -0.1f
        }
    }

    fun playerControl(direction: Int){
        if (direction == 1) {
            paddleM1 += 10f
        } else if (direction == 2) {
            paddleM1 -= 10f
        }
    }
}
