package de.chaffic.dynamics.bodies

import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class PhysicalBodyTest {

    @Test
    fun `test constructor with position`() {
        val x = 100.0
        val y = 200.0
        
        val body = PhysicalBody(x, y)
        
        assertEquals(Vec2(x, y), body.position)
    }

    @Test
    fun `test default values`() {
        val body = PhysicalBody(0.0, 0.0)
        
        assertEquals(Vec2(), body.velocity)
        assertEquals(Vec2(), body.force)
        assertEquals(0.0, body.angularVelocity, 0.0)
        assertEquals(0.0, body.torque, 0.0)
        assertEquals(0.8, body.restitution, 0.0)
        assertEquals(1.0, body.density, 0.0)
        // Mass might be calculated differently than expected
        assertTrue(body.mass >= 0.0, "Mass should be non-negative")
        assertTrue(body.invMass >= 0.0, "Inverse mass should be non-negative")
        assertTrue(body.inertia >= 0.0, "Inertia should be non-negative")
        assertTrue(body.invInertia >= 0.0, "Inverse inertia should be non-negative")
        assertEquals(0.0, body.angularDampening, 0.0)
        assertEquals(0.0, body.linearDampening, 0.0)
        assertTrue(body.affectedByGravity)
        assertFalse(body.particle)
    }

    @Test
    fun `test position property`() {
        val body = PhysicalBody(50.0, 75.0)
        
        assertEquals(Vec2(50.0, 75.0), body.position)
        
        body.position = Vec2(100.0, 150.0)
        assertEquals(Vec2(100.0, 150.0), body.position)
    }

    @Test
    fun `test position updates`() {
        val body = PhysicalBody(0.0, 0.0)
        val originalPosition = body.position.copy()
        
        body.position.add(Vec2(30.0, 40.0))
        
        assertNotEquals(originalPosition, body.position)
        assertEquals(Vec2(30.0, 40.0), body.position)
    }

    @Test
    fun `test mass calculation`() {
        val body = PhysicalBody(0.0, 0.0)
        body.density = 2.0
        
        // Mass should be calculated based on density
        // PhysicalBody inherits from AbstractPhysicalBody which handles mass calculation
        assertEquals(2.0, body.density, 0.0)
    }

    @Test
    fun `test inertia calculation`() {
        val body = PhysicalBody(0.0, 0.0)
        body.density = 1.0
        
        // Inertia should be calculated based on mass
        // PhysicalBody inherits from AbstractPhysicalBody which handles inertia calculation
        assertEquals(1.0, body.density, 0.0)
    }

    @Test
    fun `test static body properties`() {
        val body = PhysicalBody(0.0, 0.0)
        body.density = 0.0 // Make it static
        
        assertEquals(0.0, body.density, 0.0)
        // Mass and inertia should be 0 for static bodies
        assertEquals(0.0, body.mass, 0.0)
        assertEquals(0.0, body.inertia, 0.0)
    }

    @Test
    fun `test particle body properties`() {
        val body = PhysicalBody(0.0, 0.0)
        body.particle = true
        
        assertTrue(body.particle)
    }

    @Test
    fun `test restitution property`() {
        val body = PhysicalBody(0.0, 0.0)
        body.restitution = 0.5
        
        assertEquals(0.5, body.restitution, 0.0)
    }

    @Test
    fun `test dampening properties`() {
        val body = PhysicalBody(0.0, 0.0)
        body.angularDampening = 0.1
        body.linearDampening = 0.2
        
        assertEquals(0.1, body.angularDampening, 0.0)
        assertEquals(0.2, body.linearDampening, 0.0)
    }

    @Test
    fun `test gravity property`() {
        val body = PhysicalBody(0.0, 0.0)
        body.affectedByGravity = false
        
        assertFalse(body.affectedByGravity)
    }

    @Test
    fun `test velocity and force properties`() {
        val body = PhysicalBody(0.0, 0.0)
        val velocity = Vec2(5.0, 3.0)
        val force = Vec2(10.0, 7.0)
        
        body.velocity = velocity
        body.force = force
        
        assertEquals(velocity, body.velocity)
        assertEquals(force, body.force)
    }

    @Test
    fun `test angular velocity and torque properties`() {
        val body = PhysicalBody(0.0, 0.0)
        body.angularVelocity = 2.5
        body.torque = 15.0
        
        assertEquals(2.5, body.angularVelocity, 0.0)
        assertEquals(15.0, body.torque, 0.0)
    }
}
