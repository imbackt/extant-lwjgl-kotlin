package com.imbackt.extant.engine

import com.imbackt.extant.engine.graphics.Mesh
import org.joml.Vector3f

class GameItem(internal val mesh: Mesh) {
    internal val position = Vector3f()
    internal val rotation = Vector3f()
    internal var scale = 1f

    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }
}