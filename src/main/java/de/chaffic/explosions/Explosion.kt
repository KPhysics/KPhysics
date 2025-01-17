package de.chaffic.explosions

import de.chaffic.geometry.bodies.TranslatableBody
import de.chaffic.math.Vec2

/**
 * Interface detailing what explosions need to include.
 */
interface Explosion {
    /**
     * Applies a blast impulse to the effected bodies.
     *
     * @param blastPower The impulse magnitude.
     */
    fun applyBlastImpulse(blastPower: Double)

    /**
     * Updates the arraylist to reevaluate what objects are effected/within the proximity.
     *
     * @param bodiesToEvaluate Arraylist of bodies in the world to check.
     */
    fun update(bodiesToEvaluate: ArrayList<TranslatableBody>)

    /**
     * Sets the epicentre to a different coordinate.
     *
     * @param v The vector position of the new epicentre.
     */
    fun setEpicentre(v: Vec2)
}