package com.imbackt.extant.engine.graphics

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

class Transformation {
    private val projectionMatrix = Matrix4f()
    private val worldMatrix = Matrix4f()

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar)
    }

    fun getWorldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f {
        return worldMatrix.translation(offset).rotateX(Math.toRadians(rotation.x)).rotateY(Math.toRadians(rotation.y))
            .rotateZ(Math.toRadians(rotation.z)).scale(scale)
    }
}