package de.chaffic.dynamics.bodies

import de.chaffic.math.Vec2

class PhysicalBody(x: Double, y: Double) :
    AbstractPhysicalBody() {
    override var position = Vec2(x, y)
    init {
        density = density
    }
}