package com.imbackt.extant.game

import com.imbackt.extant.engine.GameItem
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Camera
import com.imbackt.extant.engine.graphics.ShaderProgram
import com.imbackt.extant.engine.graphics.Transformation
import com.imbackt.extant.engine.loadResource
import org.joml.Math
import org.lwjgl.opengl.GL30.*

class Renderer {
    private val shaderProgram by lazy { ShaderProgram() }
    private val transformation by lazy { Transformation() }

    fun init() {
        shaderProgram.createVertexShader(loadResource("shaders/vertex.vsh"))
        shaderProgram.createFragmentShader(loadResource("shaders/fragment.fsh"))
        shaderProgram.link()

        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")
        shaderProgram.createUniform("texture_sampler")
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, camera: Camera, gameItems: Array<GameItem>) {
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

        // Update viewMatrix
        val viewMatrix = transformation.getViewMatrix(camera)

        shaderProgram.setUniform("texture_sampler", 0)
        // Render each gameItem
        gameItems.forEach {
            // Set model view matrix for this item
            val modelViewMatrix = transformation.getModelViewMatrix(it, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
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