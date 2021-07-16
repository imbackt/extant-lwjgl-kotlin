package com.imbackt.extant.game

import com.imbackt.extant.engine.GameItem
import com.imbackt.extant.engine.GameLogic
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Mesh
import org.lwjgl.glfw.GLFW.*

class Game : GameLogic {
    private val renderer by lazy { Renderer() }
    private var displayXInc = 0
    private var displayYInc = 0
    private var displayZInc = 0
    private var scaleInc = 0

    private var gameItems: Array<GameItem> = emptyArray()

    override fun init(window: Window) {
        renderer.init(window)
        val positions = floatArrayOf(
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
        )
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f
        )
        val indices = intArrayOf(
            0, 1, 3, 3, 1, 2
        )

        val mesh = Mesh(positions, colors, indices)
        val gameItem = GameItem(mesh)
        gameItem.setPosition(0f, 0f, -2f)
        gameItems += gameItem
    }

    override fun input(window: Window) {
        displayXInc = 0
        displayYInc = 0
        displayZInc = 0
        scaleInc = 0

        when {
            window.isKeyPressed(GLFW_KEY_UP) -> displayYInc = 1
            window.isKeyPressed(GLFW_KEY_DOWN) -> displayYInc = -1
            window.isKeyPressed(GLFW_KEY_LEFT) -> displayXInc = -1
            window.isKeyPressed(GLFW_KEY_RIGHT) -> displayXInc = 1
            window.isKeyPressed(GLFW_KEY_A) -> displayZInc = -1
            window.isKeyPressed(GLFW_KEY_Q) -> displayZInc = 1
            window.isKeyPressed(GLFW_KEY_Z) -> scaleInc = -1
            window.isKeyPressed(GLFW_KEY_X) -> scaleInc = 1
        }
    }

    override fun update(interval: Float) {
        gameItems.forEach {
            // Update position
            it.setPosition(
                it.position.x + displayXInc * 0.1f,
                it.position.y + displayYInc * 0.1f,
                it.position.z + displayZInc * 0.1f
            )

            // Update scale
            it.scale = if (it.scale >= 0) it.scale + scaleInc * 0.05f else 0f

            // Update rotation angle
            it.setRotation(
                0f,
                0f,
                if (it.rotation.z <= 360) it.rotation.z + 1.5f else 0f
            )
        }
    }

    override fun render(window: Window) {
        renderer.render(window, gameItems)
    }

    override fun cleanup() {
        renderer.cleanup()
        gameItems.forEach {
            it.mesh.cleanup()
        }
    }
}