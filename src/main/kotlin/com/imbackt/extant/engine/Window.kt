package com.imbackt.extant.engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class Window(
    private val title: String,
    internal var width: Int,
    internal var height: Int,
    internal val vSync: Boolean
) {
    internal val windowHandle by lazy { glfwCreateWindow(width, height, title, NULL, NULL) }
    internal var resized = false

    fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE) // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

        // Create the window
        if (windowHandle == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle) { _, width, height ->
            this.width = width
            this.height = height
            this.resized = true
        }

        // Setup a key callback. It will be called every time a key is pressed or released
        glfwSetKeyCallback(windowHandle) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true)
            }
        }

        // Get the resolution of the primary monitor
        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        // Center the window
        glfwSetWindowPos(
            windowHandle,
            (videoMode.width() - width) / 2,
            (videoMode.height() - height) / 2
        )

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle)

        if (vSync) {
            // Enable vSync
            glfwSwapInterval(1)
        }

        // Make  the window visible
        glfwShowWindow(windowHandle)

        createCapabilities()

        // Set the clear color
        glClearColor(0f, 0f, 0f, 0f)
        glEnable(GL_DEPTH_TEST)
    }

    fun setClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        glClearColor(red, green, blue, alpha)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS
    }

    fun windowShouldClose(): Boolean {
        return glfwWindowShouldClose(windowHandle)
    }

    fun update() {
        glfwSwapBuffers(windowHandle)
        glfwPollEvents()
    }
}