package com.nonnewtonian

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

open class Game : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val font by lazy { BitmapFont() }
    val assets = AssetManager()

    override fun create() {
        addScreen(GameScreen(this))
        setScreen<GameScreen>()

        font.color = Color.WHITE
        font.data.setScale(2f)

        super.create()
    }

    override fun dispose() {
        batch.disposeSafely()
        font.disposeSafely()
        assets.disposeSafely()
        super.dispose()
    }

}
