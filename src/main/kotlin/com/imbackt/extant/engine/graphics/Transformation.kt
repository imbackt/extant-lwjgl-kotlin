package com.imbackt.extant.engine.graphics

import com.imbackt.extant.engine.GameItem
import org.joml.Math.toRadians
import org.joml.Matrix4f
import org.joml.Vector3f

class Transformation {
    private val projectionMatrix = Matrix4f()
    private val modelViewMatrix = Matrix4f()
    private val viewMatrix = Matrix4f()

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar)
    }

    fun getViewMatrix(camera: Camera): Matrix4f {
        viewMatrix.identity()
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate(toRadians(camera.rotation.x), Vector3f(1f, 0f, 0f))
            .rotate(toRadians(camera.rotation.y), Vector3f(0f, 1f, 0f))
        // Then do the transition
        viewMatrix.translate(-camera.position.x, -camera.position.y, -camera.position.z)
        return viewMatrix
    }

    fun getModelViewMatrix(gameItem: GameItem, viewMatrix: Matrix4f): Matrix4f {
        modelViewMatrix.identity().translate(gameItem.position).rotateX(toRadians(-gameItem.rotation.x))
            .rotateY(toRadians(-gameItem.rotation.y)).rotateZ(toRadians(-gameItem.rotation.z)).scale(gameItem.scale)
        val viewCurrent = Matrix4f(viewMatrix)
        return viewCurrent.mul(modelViewMatrix)
    }
}