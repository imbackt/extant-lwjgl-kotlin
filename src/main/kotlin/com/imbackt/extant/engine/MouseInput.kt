package com.imbackt.extant.engine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*

class MouseInput {
    private val previousPos = Vector2d(-1.0, -1.0)
    private val currentPos = Vector2d(0.0, 0.0)
    internal val displayVec = Vector2f()
    private var inWindow = false
    var leftButtonPressed = false
        private set
    var rightButtonPressed = false
        private set

    fun init(window: Window) {
        glfwSetCursorPosCallback(window.windowHandle) { _, xPos, yPos ->
            currentPos.x = xPos
            currentPos.y = yPos
        }
        glfwSetCursorEnterCallback(window.windowHandle) { _, entered ->
            inWindow = entered
        }
        glfwSetMouseButtonCallback(window.windowHandle) { _, button, action, _ ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun input(window: Window) {
        displayVec.x = 0.0f
        displayVec.y = 0.0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            val deltaX = currentPos.x - previousPos.x
            val deltaY = currentPos.y - previousPos.y
            val rotateX = deltaX != 0.0
            val rotateY = deltaY != 0.0
            if (rotateX) displayVec.y = deltaX.toFloat()
            if (rotateY) displayVec.x = deltaY.toFloat()
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }
}