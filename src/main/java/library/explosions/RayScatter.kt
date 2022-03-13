package library.explosions

import library.dynamics.Body
import library.math.Mat2
import library.math.Vec2
import library.rays.Ray

/**
 * Models rayscatter explosions.
 */
class RayScatter(epicentre: Vec2, private val noOfRays: Int) {
    /**
     * Getter for rays.
     *
     * @return Array of all rays part of the ray scatter.
     */
    val rays = mutableListOf<Ray>()
    var epicentre: Vec2 = epicentre
        set(value) {
        field = value
        for (ray in rays) {
            ray.startPoint = field
        }
    }

    /**
     * Casts rays in 360 degrees with equal spacing.
     *
     * @param distance Distance of projected rays.
     */
    fun castRays(distance: Int) {
        val angle = 6.28319 / noOfRays
        val direction = Vec2(1.0, 1.0)
        val u = Mat2(angle)
        for (i in rays.indices) {
            rays.add(Ray(epicentre, direction, distance))
            u.mul(direction)
        }
    }

    /**
     * Updates all rays.
     *
     * @param worldBodies Arraylist of all bodies to update ray projections for.
     */
    fun updateRays(worldBodies: ArrayList<Body>) {
        for (ray in rays) {
            ray.updateProjection(worldBodies)
        }
    }
}