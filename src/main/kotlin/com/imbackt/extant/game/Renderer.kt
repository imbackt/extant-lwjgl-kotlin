package com.imbackt.extant.game

import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.ShaderProgram
import com.imbackt.extant.engine.loadResource
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil

class Renderer {
    private val shaderProgram by lazy { ShaderProgram() }
    private val vaoId by lazy { glGenVertexArrays() }
    private val vboId by lazy { glGenBuffers() }

    fun init() {
        shaderProgram.createVertexShader(loadResource("vertex.vsh"))
        shaderProgram.createFragmentShader(loadResource("fragment.fsh"))
        shaderProgram.link()

        val vertices = floatArrayOf(
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )

        val verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
        verticesBuffer.put(vertices).flip()

        // Create the VAO and bind to it
        glBindVertexArray(vaoId)

        // Create the VBO and bind to it
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)

        // Enable location 0
        glEnableVertexAttribArray(0)
        // Define structure of data
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        // Unbind the VAO
        glBindVertexArray(0)

        MemoryUtil.memFree(verticesBuffer)
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(window: Window) {
        clear()

        if (window.resized) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram.bind()

        // Bind the VAO
        glBindVertexArray(vaoId)

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 3)

        // Restore state
        glBindVertexArray(0)

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()

        glDisableVertexAttribArray(0)

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        GL15.glDeleteBuffers(vboId)

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}