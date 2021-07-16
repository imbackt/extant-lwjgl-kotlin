package com.imbackt.extant.engine.graphics

import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil

class Mesh(
    positions: FloatArray,
    colors: FloatArray,
    indices: IntArray
) {
    private val vaoId by lazy { glGenVertexArrays() }
    private val posVboId by lazy { glGenBuffers() }
    private val colorVboId by lazy { glGenBuffers() }
    private val idxVboId by lazy { glGenBuffers() }
    private val vertexCount = indices.size

    init {
        glBindVertexArray(vaoId)

        // Position VBO
        val posBuffer = MemoryUtil.memAllocFloat(positions.size)
        posBuffer.put(positions).flip()
        glBindBuffer(GL_ARRAY_BUFFER, posVboId)
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Color VBO
        val colorBuffer = MemoryUtil.memAllocFloat(colors.size)
        colorBuffer.put(colors).flip()
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId)
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

        // Index VBO
        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices).flip()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        MemoryUtil.memFree(posBuffer)
        MemoryUtil.memFree(colorBuffer)
        MemoryUtil.memFree(indicesBuffer)
    }

    fun render() {
        // Draw the mesh
        glBindVertexArray(vaoId)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        // Restore the state
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1) //??

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        GL15.glDeleteBuffers(posVboId)
        GL15.glDeleteBuffers(colorVboId)
        GL15.glDeleteBuffers(idxVboId)

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}