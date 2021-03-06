package com.imbackt.extant.engine.graphics

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer


class Texture(internal val id: Int) {

    constructor(fileName: String) : this(loadTexture(fileName))

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun cleanup() {
        glDeleteTextures(id)
    }

    companion object {
        private fun loadTexture(fileName: String): Int {
            var width: Int
            var height: Int
            var buffer: ByteBuffer?
            MemoryStack.stackPush().use { stack ->
                val w = stack.mallocInt(1)
                val h = stack.mallocInt(1)
                val channels = stack.mallocInt(1)
                buffer = stbi_load(fileName, w, h, channels, 4)
                if (buffer == null) {
                    throw Exception("Image file [$fileName] not loaded: ${stbi_failure_reason()}")
                }

                /* Get width and height of image */
                width = w.get()
                height = h.get()
            }

            // Create a new OpenGL texture
            val textureId = glGenTextures()
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId)

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Upload the texture data
            glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffer
            )
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D)
            stbi_image_free(buffer!!)
            return textureId
        }
    }
}