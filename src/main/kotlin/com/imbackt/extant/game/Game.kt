package com.imbackt.extant.game

import com.imbackt.extant.engine.GameItem
import com.imbackt.extant.engine.GameLogic
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Mesh
import com.imbackt.extant.engine.graphics.Texture
import org.lwjgl.glfw.GLFW.*

class Game : GameLogic {
    private val renderer by lazy { Renderer() }
    private var displayXInc = 0
    private var displayYInc = 0
    private var displayZInc = 0
    private var scaleInc = 0

    private var gameItems: Array<GameItem> = emptyArray()

    override fun init(window: Window) {
        renderer.init()
        val positions = floatArrayOf(
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f
        )
        val textCoords = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f
        )
        val indices = intArrayOf(
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7
        )
        val texture = Texture("textures/grassblock.png")
        val mesh = Mesh(positions, textCoords, indices, texture)
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
                if (it.rotation.x <= 360) it.rotation.z + 1.5f else 0f,
                if (it.rotation.y <= 360) it.rotation.z + 1.5f else 0f,
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