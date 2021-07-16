package com.imbackt.extant.game

import com.imbackt.extant.engine.GameItem
import com.imbackt.extant.engine.GameLogic
import com.imbackt.extant.engine.MouseInput
import com.imbackt.extant.engine.Window
import com.imbackt.extant.engine.graphics.Camera
import com.imbackt.extant.engine.graphics.Mesh
import com.imbackt.extant.engine.graphics.Texture
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class Game : GameLogic {
    private val renderer by lazy { Renderer() }
    private val camera by lazy { Camera() }
    private val cameraInc = Vector3f()

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
        val gameItem1 = GameItem(mesh)
        gameItem1.scale = 0.5f
        gameItem1.setPosition(0f, 0f, -2f)
        val gameItem2 = GameItem(mesh)
        gameItem2.scale = 0.5f
        gameItem2.setPosition(0.5f, 0.5f, -2f)
        val gameItem3 = GameItem(mesh)
        gameItem3.scale = 0.5f
        gameItem3.setPosition(0.0f, 0.0f, -2.5f)
        val gameItem4 = GameItem(mesh)
        gameItem4.scale = 0.5f
        gameItem4.setPosition(0.5f, 0.0f, -2.5f)
        gameItems = arrayOf(gameItem1, gameItem2, gameItem3, gameItem4)
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.set(0f, 0f, 0f)
        when {
            window.isKeyPressed(GLFW_KEY_W) -> cameraInc.z = -1f
            window.isKeyPressed(GLFW_KEY_S) -> cameraInc.z = 1f
            window.isKeyPressed(GLFW_KEY_A) -> cameraInc.x = -1f
            window.isKeyPressed(GLFW_KEY_D) -> cameraInc.x = 1f
            window.isKeyPressed(GLFW_KEY_Z) -> cameraInc.y = -1f
            window.isKeyPressed(GLFW_KEY_X) -> cameraInc.y = 1f
        }
    }

    override fun update(interval: Float, mouseInput: MouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        // Update camera based on mouse
        if (mouseInput.rightButtonPressed) {
            camera.moveRotation(
                mouseInput.displayVec.x * MOUSE_SENSITIVITY,
                mouseInput.displayVec.y * MOUSE_SENSITIVITY,
                0f
            )
        }
    }

    override fun render(window: Window) {
        renderer.render(window, camera, gameItems)
    }

    override fun cleanup() {
        renderer.cleanup()
        gameItems.forEach {
            it.mesh.cleanup()
        }
    }

    companion object {
        const val CAMERA_POS_STEP = 0.05f
        const val MOUSE_SENSITIVITY = 0.2f
    }
}