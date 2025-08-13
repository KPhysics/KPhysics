package de.chaffic.dynamics.bodies

import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class AbstractPhysicalBodyTest {

    @Test
    fun `test default values`() {
        val body = TestPhysicalBody()
        
        assertEquals(Vec2(), body.velocity)
        assertEquals(Vec2(), body.force)
        assertEquals(0.0, body.angularVelocity, 0.0)
        assertEquals(0.0, body.torque, 0.0)
        assertEquals(0.8, body.restitution, 0.0)
        assertEquals(1.0, body.density, 0.0)
        assertEquals(0.0, body.mass, 0.0)
        assertEquals(0.0, body.invMass, 0.0)
        assertEquals(0.0, body.inertia, 0.0)
        assertEquals(0.0, body.invInertia, 0.0)
        assertEquals(0.0, body.angularDampening, 0.0)
        assertEquals(0.0, body.linearDampening, 0.0)
        assertTrue(body.affectedByGravity)
        assertFalse(body.particle)
    }

    @Test
    fun `test apply force to center of mass`() {
        val body = TestPhysicalBody()
        val force = Vec2(10.0, 5.0)
        
        body.applyForce(force)
        
        assertEquals(force, body.force)
    }

    @Test
    fun `test apply force to contact point`() {
        val body = TestPhysicalBody()
        val force = Vec2(10.0, 5.0)
        val contactPoint = Vec2(2.0, 3.0)
        
        body.applyForce(force, contactPoint)
        
        assertEquals(force, body.force)
        assertEquals(contactPoint.cross(force), body.torque, 0.001)
    }

    @Test
    fun `test apply linear impulse to center of mass`() {
        val body = TestPhysicalBody()
        body.mass = 10.0
        body.invMass = 0.1
        val impulse = Vec2(5.0, 3.0)
        
        body.applyLinearImpulse(impulse)
        
        assertEquals(0.5, body.velocity.x, 0.001)
        assertEquals(0.3, body.velocity.y, 0.001)
    }

    @Test
    fun `test apply linear impulse to contact point`() {
        val body = TestPhysicalBody()
        body.mass = 10.0
        body.invMass = 0.1
        body.inertia = 100.0
        body.invInertia = 0.01
        val impulse = Vec2(5.0, 3.0)
        val contactPoint = Vec2(2.0, 3.0)
        
        body.applyLinearImpulse(impulse, contactPoint)
        
        assertEquals(0.5, body.velocity.x, 0.001)
        assertEquals(0.3, body.velocity.y, 0.001)
        assertEquals(0.01 * contactPoint.cross(impulse), body.angularVelocity, 0.001)
    }

    @Test
    fun `test set static`() {
        val body = TestPhysicalBody()
        body.mass = 10.0
        body.inertia = 100.0
        
        body.setStatic()
        
        assertEquals(0.0, body.mass, 0.0)
        assertEquals(0.0, body.invMass, 0.0)
        assertEquals(0.0, body.inertia, 0.0)
        assertEquals(0.0, body.invInertia, 0.0)
    }

    @Test
    fun `test density setter with zero density`() {
        val body = TestPhysicalBody()
        
        body.density = 0.0
        
        assertEquals(0.0, body.density, 0.0)
        body.setStatic() // Should not throw exception
    }

    @Test
    fun `test density setter with non-zero density`() {
        val body = TestPhysicalBody()
        
        body.density = 2.0
        
        assertEquals(2.0, body.density, 0.0)
        assertEquals(2000.0, body.mass, 0.0) // density * 1000
        assertEquals(0.0005, body.invMass, 0.001) // 1.0 / mass
    }

    @Test
    fun `test density setter with zero mass`() {
        val body = TestPhysicalBody()
        body.mass = 0.0
        
        body.density = 1.0
        
        assertEquals(1.0, body.density, 0.0)
        assertEquals(0.0, body.invMass, 0.001) // Use tolerance for floating point comparison
    }

    @Test
    fun `test density setter with zero inertia`() {
        val body = TestPhysicalBody()
        body.inertia = 0.0
        
        body.density = 1.0
        
        assertEquals(1.0, body.density, 0.0)
        assertEquals(0.0, body.invInertia, 0.001) // Use tolerance for floating point comparison
    }

    @Test
    fun `test property setters`() {
        val body = TestPhysicalBody()
        
        body.velocity = Vec2(5.0, 3.0)
        body.force = Vec2(10.0, 7.0)
        body.angularVelocity = 2.5
        body.torque = 15.0
        body.restitution = 0.9
        body.angularDampening = 0.1
        body.linearDampening = 0.2
        body.affectedByGravity = false
        body.particle = true
        
        assertEquals(Vec2(5.0, 3.0), body.velocity)
        assertEquals(Vec2(10.0, 7.0), body.force)
        assertEquals(2.5, body.angularVelocity, 0.0)
        assertEquals(15.0, body.torque, 0.0)
        assertEquals(0.9, body.restitution, 0.0)
        assertEquals(0.1, body.angularDampening, 0.0)
        assertEquals(0.2, body.linearDampening, 0.0)
        assertFalse(body.affectedByGravity)
        assertTrue(body.particle)
    }

    // Helper class for testing AbstractPhysicalBody
    private class TestPhysicalBody : AbstractPhysicalBody() {
        override var position: Vec2 = Vec2()
    }
}
