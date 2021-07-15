package com.imbackt.extant.game

import com.imbackt.extant.engine.GameEngine
import com.imbackt.extant.engine.GameLogic

fun main() {
    try {
        val gameLogic: GameLogic = Game()
        val gameEngine = GameEngine("GAME", 600, 480, true, gameLogic)
        gameEngine.run()
    } catch (e: Exception) {
        e.printStackTrace()
        error(-1)
    }
}