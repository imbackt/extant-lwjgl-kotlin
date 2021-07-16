package com.imbackt.extant.game

import com.imbackt.extant.engine.GameItem
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.ShaderProgram
import com.imbackt.extant.engine.graphics.Transformation
import com.imbackt.extant.engine.loadResource
import org.joml.Math
import org.lwjgl.opengl.GL30.*

class Renderer {
    private val shaderProgram by lazy { ShaderProgram() }
    private val transformation by lazy { Transformation() }

    fun init(window: Window) {
        shaderProgram.createVertexShader(loadResource("vertex.vsh"))
        shaderProgram.createFragmentShader(loadResource("fragment.fsh"))
        shaderProgram.link()

        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("worldMatrix")

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, gameItems: Array<GameItem>) {
        clear()

        if (window.resized) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram.bind()

        // Update projection Matrix
        val projectionMatrix =
            transformation.getProjectionMatrix(FOV, window.width.toFloat(), window.height.toFloat(), Z_NEAR, Z_FAR)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        // Render each gameItem
        gameItems.forEach {
            val worldMatrix = transformation.getWorldMatrix(it.position, it.rotation, it.scale)
            shaderProgram.setUniform("worldMatrix", worldMatrix)
            // Render the mesh for the item
            it.mesh.render()
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }

    companion object {
        private val FOV = Math.toRadians(60.0f)
        private const val Z_NEAR = 0.01f
        private const val Z_FAR = 1000.0f
    }
}