package com.imbackt.extant.game

import com.imbackt.extant.engine.GameLogic
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Mesh
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP

class Game : GameLogic {
    private val renderer by lazy { Renderer() }
    private var direction = 0
    private var color = 0f

    private lateinit var mesh: Mesh

    override fun init() {
        renderer.init()
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
            0, 1, 3,
            3, 1, 2
        )

        mesh = Mesh(positions, colors, indices)
    }

    override fun input(window: Window) {
        direction = when {
            window.isKeyPressed(GLFW_KEY_UP) -> 1
            window.isKeyPressed(GLFW_KEY_DOWN) -> -1
            else -> 0
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f
        if (color > 1) color = 1f
        else if (color < 0) color = 0f
    }

    override fun render(window: Window) {
        window.setClearColor(color, color, color, 0.0f)
        renderer.render(window, mesh)
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh.cleanup()
    }
}