package com.imbackt.extant.engine.graphics

import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil

class Mesh(
    positions: FloatArray,
    textCoords: FloatArray,
    indices: IntArray,
    private val texture: Texture
) {
    private val vaoId by lazy { glGenVertexArrays() }
    private val vboIdList: List<Int> = ArrayList()
    private val vertexCount = indices.size

    init {
        glBindVertexArray(vaoId)

        // Position VBO
        var vboId = glGenBuffers()
        vboIdList.toMutableList().add(vboId)
        val posBuffer = MemoryUtil.memAllocFloat(positions.size)
        posBuffer.put(positions).flip()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Texture coordinates VBO
        vboId = glGenBuffers()
        vboIdList.toMutableList().add(vboId)
        val textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.size)
        textCoordsBuffer.put(textCoords).flip()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        // Index VBO
        vboId = glGenBuffers()
        vboIdList.toMutableList().add(vboId)
        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices).flip()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        MemoryUtil.memFree(posBuffer)
        MemoryUtil.memFree(textCoordsBuffer)
        MemoryUtil.memFree(indicesBuffer)
    }

    fun render() {
        // Activate first texture bank
        glActiveTexture(GL_TEXTURE0)
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.id)

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
        vboIdList.forEach {
            GL15.glDeleteBuffers(it)
        }

        // Delete the texture
        texture.cleanup()

        // Delete the VAO
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }
}