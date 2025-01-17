package de.chaffic.collision.bodies

import de.chaffic.collision.AxisAlignedBoundingBox
import de.chaffic.geometry.Shape
import de.chaffic.math.Vec2

class CollisionBody(override var shape: Shape, x: Double, y: Double): CollisionBodyInterface {
    override var position: Vec2 = Vec2(x, y)
    override var dynamicFriction = .5
    override var staticFriction = .2
    override var orientation = .0
        set(value) {
            field = value
            shape.orientation.set(orientation)
            shape.createAABB()
        }
    override lateinit var aabb: AxisAlignedBoundingBox

    init {
        shape.body = this
        shape.orientation.set(orientation)
        shape.createAABB()
    }
}