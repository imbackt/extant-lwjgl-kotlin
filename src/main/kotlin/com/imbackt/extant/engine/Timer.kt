package com.imbackt.extant.engine

class Timer {
    var lastLoopTime = 0.0
        private set

    fun init() {
        lastLoopTime = time
    }

    internal val time: Double
        get() = System.nanoTime() / 1_000_000_000.0
    val elapsedTime: Float
        get() {
            val time = time
            val elapsedTime = (time - lastLoopTime).toFloat()
            lastLoopTime = time
            return elapsedTime
        }
}