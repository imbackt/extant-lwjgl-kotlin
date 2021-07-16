package com.imbackt.extant.engine

class GameEngine(
    private val windowTitle: String,
    private val width: Int,
    private val height: Int,
    private val vSync: Boolean,
    private val gameLogic: GameLogic
) : Runnable {
    private val window by lazy { Window(windowTitle, width, height, vSync) }
    private val timer by lazy { Timer() }
    private val mouseInput by lazy { MouseInput() }

    override fun run() {
        try {
            init()
            gameLoop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cleanup()
        }
    }

    private fun init() {
        window.init()
        timer.init()
        mouseInput.init(window)
        gameLogic.init(window)
    }

    private fun gameLoop() {
        var elapsedTime: Float
        var accumulator = 0f
        val interval = 1f / TARGET_UPS

        val running = true
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.elapsedTime
            accumulator += elapsedTime

            input()

            while (accumulator >= interval) {
                update(interval)
                accumulator -= interval
            }

            render()

            if (!window.vSync) {
                sync()
            }
        }
    }

    private fun input() {
        mouseInput.input(window)
        gameLogic.input(window, mouseInput)
    }

    private fun update(interval: Float) {
        gameLogic.update(interval, mouseInput)
    }

    private fun render() {
        gameLogic.render(window)
        window.update()
    }

    private fun sync() {
        val loopSlot = 1f / TARGET_FPS
        val endTime = timer.lastLoopTime + loopSlot
        while (timer.time < endTime) {
            try {
                Thread.sleep(1)
            } catch (ie: InterruptedException) {
            }
        }
    }

    private fun cleanup() {
        gameLogic.cleanup()
    }

    companion object {
        const val TARGET_FPS = 75
        const val TARGET_UPS = 30
    }
}