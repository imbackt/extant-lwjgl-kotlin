package com.imbackt.extant.engine

interface GameLogic {
    fun init(window: Window)
    fun input(window: Window, mouseInput: MouseInput)
    fun update(interval: Float, mouseInput: MouseInput)
    fun render(window: Window)
    fun cleanup()
}