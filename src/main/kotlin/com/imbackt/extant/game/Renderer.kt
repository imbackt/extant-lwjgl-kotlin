package com.imbackt.extant.game

import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Mesh
import com.imbackt.extant.engine.graphics.ShaderProgram
import com.imbackt.extant.engine.loadResource
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*

class Renderer {
    private val shaderProgram by lazy { ShaderProgram() }

    fun init() {
        shaderProgram.createVertexShader(loadResource("vertex.vsh"))
        shaderProgram.createFragmentShader(loadResource("fragment.fsh"))
        shaderProgram.link()
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window, mesh: Mesh) {
        clear()

        if (window.resized) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram.bind()

        // Draw the mesh
        glBindVertexArray(mesh.vaoId)
        GL11.glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)

        // Restore state
        glBindVertexArray(0)

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}