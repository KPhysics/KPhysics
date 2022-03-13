package library.rays

import library.dynamics.Body
import library.math.Vec2

/**
 * Ray information class to store relevant data about rays and any intersection found.
 */
class RayInformation {
    /**
     * Getter for body variable.
     *
     * @return returns b variable of type Body.
     */
    val b: Body

    /**
     * Getter for coords variable.
     *
     * @return returns coords variable of type Vec2.
     */
    val coordinates: Vec2

    /**
     * Getter for index variable.
     *
     * @return returns index variable of type int.
     */
    // Poly index is the first index of the line of intersection found
    val index: Int

    /**
     * Constructor to store information about a ray intersection.
     *
     * @param b     Body involved with ray intersection.
     * @param x     x position of intersection.
     * @param y     y position of intersection.
     * @param index Index of shapes side that intersection intersects.
     */
    constructor(b: Body, x: Double, y: Double, index: Int) {
        this.b = b
        coordinates = Vec2(x, y)
        this.index = index
    }

    /**
     * Convenience constructor equivalent to
     * [.RayInformation]
     *
     * @param b     Body involved with ray intersection.
     * @param v     x/y position of intersection.
     * @param index Index of shapes side that intersection intersects.
     */
    constructor(b: Body, v: Vec2, index: Int) {
        this.b = b
        coordinates = v.copy()
        this.index = index
    }
}