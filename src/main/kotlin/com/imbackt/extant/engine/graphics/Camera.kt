package com.imbackt.extant.engine.graphics

import org.joml.Math.toRadians
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(
    internal val position: Vector3f = Vector3f(),
    internal val rotation: Vector3f = Vector3f()
) {
    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun movePosition(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetZ != 0f) {
            position.x += sin(toRadians(rotation.y)) * -1.0f * offsetZ
            position.z += cos(toRadians(rotation.y)) * offsetZ
        }
        if (offsetX != 0f) {
            position.x += sin(toRadians(rotation.y - 90)) * -1.0f * offsetX
            position.z += cos(toRadians(position.y - 90)) * offsetX
        }
        position.y += offsetY
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }

    fun moveRotation(offsetX: Float, offsetY: Float, offsetZ: Float) {
        rotation.x += offsetX
        rotation.y += offsetY
        rotation.z += offsetZ
    }
}