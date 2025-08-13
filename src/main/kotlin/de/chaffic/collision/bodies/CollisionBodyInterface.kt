package de.chaffic.collision.bodies

import de.chaffic.collision.AxisAlignedBoundingBox
import de.chaffic.geometry.Shape
import de.chaffic.geometry.bodies.TranslatableBody

interface CollisionBodyInterface : TranslatableBody {
    var shape: Shape
    var dynamicFriction: Double
    var staticFriction: Double
    var orientation: Double
    var aabb: AxisAlignedBoundingBox
}