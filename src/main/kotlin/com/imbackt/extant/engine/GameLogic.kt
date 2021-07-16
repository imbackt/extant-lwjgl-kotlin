package com.imbackt.extant.engine

interface GameLogic {
    fun init(window: Window)
    fun input(window: Window)
    fun update(interval: Float)
    fun render(window: Window)
    fun cleanup()
}