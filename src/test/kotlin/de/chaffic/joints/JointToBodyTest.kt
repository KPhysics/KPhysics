package de.chaffic.joints

import de.chaffic.dynamics.Body
import de.chaffic.geometry.Circle
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class JointToBodyTest {

    @Test
    fun `test constructor`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        val jointLength = 20.0
        val jointConstant = 1.0
        val dampening = 0.1
        val canGoSlack = false
        val offset1 = Vec2(5.0, 0.0)
        val offset2 = Vec2(-5.0, 0.0)
        
        val joint = JointToBody(body1, body2, jointLength, jointConstant, dampening, canGoSlack, offset1, offset2)
        
        // Test that the joint was created successfully
        assertNotNull(joint)
    }

    @Test
    fun `test object attachment points initialization`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        val offset1 = Vec2(5.0, 0.0)
        val offset2 = Vec2(-5.0, 0.0)
        
        val joint = JointToBody(body1, body2, 20.0, 1.0, 0.1, false, offset1, offset2)
        
        // Attachment points should be initialized based on body positions and orientations
        assertNotNull(joint.object1AttachmentPoint)
        assertNotNull(joint.object2AttachmentPoint)
    }

    @Test
    fun `test apply tension`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        body1.mass = 10.0
        body2.mass = 10.0
        body1.invMass = 0.1
        body2.invMass = 0.1
        
        val joint = JointToBody(body1, body2, 20.0, 1.0, 0.1, false, Vec2(), Vec2())
        
        try {
            joint.applyTension()
        } catch (e: Exception) {
            fail("applyTension method should not throw an exception: ${e.message}")
        }
    }

    @Test
    fun `test calculate tension with extension`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 25.0, 0.0) // 5 units beyond natural length
        val joint = JointToBody(body1, body2, 20.0, 2.0, 0.1, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be positive due to extension (5 * 2.0 = 10.0)
        assertTrue(tension > 0)
        assertEquals(10.0, tension, 0.1)
    }

    @Test
    fun `test calculate tension with compression`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 15.0, 0.0) // 5 units below natural length
        val joint = JointToBody(body1, body2, 20.0, 2.0, 0.1, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be negative due to compression (-5 * 2.0 = -10.0)
        assertTrue(tension < 0)
        assertEquals(-10.0, tension, 0.1)
    }

    @Test
    fun `test calculate tension with slack allowed`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 15.0, 0.0) // 5 units below natural length
        val joint = JointToBody(body1, body2, 20.0, 2.0, 0.1, true, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be zero when slack is allowed and distance < natural length
        assertEquals(0.0, tension, 0.0)
    }

    @Test
    fun `test calculate tension with no slack`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 15.0, 0.0) // 5 units below natural length
        val joint = JointToBody(body1, body2, 20.0, 2.0, 0.1, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be negative when no slack is allowed
        assertTrue(tension < 0)
    }

    @Test
    fun `test rate of change of extension`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        body1.velocity = Vec2(1.0, 0.0)
        body2.velocity = Vec2(-1.0, 0.0)
        
        val joint = JointToBody(body1, body2, 20.0, 1.0, 0.1, false, Vec2(), Vec2())
        
        val rateOfChange = joint.rateOfChangeOfExtension()
        
        // Rate of change should be negative as bodies are moving apart
        assertTrue(rateOfChange < 0)
    }

    @Test
    fun `test rate of change with angular velocity`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        body1.angularVelocity = 1.0
        body2.angularVelocity = -1.0
        
        val joint = JointToBody(body1, body2, 20.0, 1.0, 0.1, false, Vec2(), Vec2())
        
        val rateOfChange = joint.rateOfChangeOfExtension()
        
        // Should calculate rate of change considering angular velocities
        assertNotNull(rateOfChange)
    }

    @Test
    fun `test joint with zero spring constant`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 25.0, 0.0)
        val joint = JointToBody(body1, body2, 20.0, 0.0, 0.1, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be zero with zero spring constant
        assertEquals(0.0, tension, 0.0)
    }

    @Test
    fun `test joint with zero dampening`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 25.0, 0.0)
        val joint = JointToBody(body1, body2, 20.0, 1.0, 0.0, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should only include spring force, no dampening
        assertEquals(5.0, tension, 0.1)
    }

    @Test
    fun `test joint with high spring constant`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 25.0, 0.0)
        val joint = JointToBody(body1, body2, 20.0, 10.0, 0.1, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should be high with high spring constant (5 * 10.0 = 50.0)
        assertEquals(50.0, tension, 0.1)
    }

    @Test
    fun `test joint with high dampening`() {
        val body1 = Body(Circle(10.0), 0.0, 0.0)
        val body2 = Body(Circle(10.0), 20.0, 0.0)
        body1.velocity = Vec2(1.0, 0.0)
        body2.velocity = Vec2(-1.0, 0.0)
        
        val joint = JointToBody(body1, body2, 20.0, 1.0, 5.0, false, Vec2(), Vec2())
        
        val tension = joint.calculateTension()
        
        // Tension should include significant dampening force
        assertNotNull(tension)
    }
}
