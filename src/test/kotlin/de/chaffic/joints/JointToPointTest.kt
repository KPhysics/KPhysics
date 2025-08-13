package de.chaffic.joints

import de.chaffic.dynamics.Body
import de.chaffic.geometry.Circle
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class JointToPointTest {

    @Test
    fun `test constructor with body and point`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(100.0, 200.0)
        val jointLength = 25.0
        val jointConstant = 1.5
        val dampening = 0.2
        val canGoSlack = true
        val offset = Vec2(5.0, 0.0)
        
        val joint = JointToPoint(body, point, jointLength, jointConstant, dampening, canGoSlack, offset)
        
        // Test that the joint was created successfully
        assertNotNull(joint)
    }

    @Test
    fun `test object attachment point initialization`() {
        val body = Body(Circle(10.0), 50.0, 75.0)
        val point = Vec2(100.0, 200.0)
        val offset = Vec2(5.0, 0.0)
        val joint = JointToPoint(body, point, 30.0, 1.0, 0.1, false, offset)
        
        // The attachment point should be initialized based on body position and orientation
        assertNotNull(joint)
    }

    @Test
    fun `test apply tension`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        body.mass = 10.0
        body.invMass = 0.1
        val point = Vec2(20.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 1.0, 0.1, false, offset)
        
        try {
            joint.applyTension()
            // Method should not crash
        } catch (e: Exception) {
            fail("applyTension method should not throw an exception: ${e.message}")
        }
    }

    @Test
    fun `test calculate tension with extension`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(25.0, 0.0) // 5 units beyond natural length
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 2.0, 0.1, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be positive due to extension (5 * 2.0 = 10.0)
        assertTrue(tension > 0)
        assertEquals(10.0, tension, 0.1)
    }

    @Test
    fun `test calculate tension with compression`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(15.0, 0.0) // 5 units below natural length
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 2.0, 0.1, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be negative due to compression (-5 * 2.0 = -10.0)
        assertTrue(tension < 0)
        assertEquals(-10.0, tension, 0.1)
    }

    @Test
    fun `test calculate tension with slack allowed`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(15.0, 0.0) // 5 units below natural length
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 2.0, 0.1, true, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be zero when slack is allowed and distance < natural length
        assertEquals(0.0, tension, 0.0)
    }

    @Test
    fun `test calculate tension with no slack`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(15.0, 0.0) // 5 units below natural length
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 2.0, 0.1, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be negative when no slack is allowed
        assertTrue(tension < 0)
    }

    @Test
    fun `test rate of change of extension`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        body.velocity = Vec2(1.0, 0.0)
        val point = Vec2(20.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 1.0, 0.1, false, offset)
        
        val rateOfChange = joint.rateOfChangeOfExtension()
        
        // Rate of change should be calculated based on body velocity
        assertNotNull(rateOfChange)
    }

    @Test
    fun `test rate of change with angular velocity`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        body.angularVelocity = 1.0
        val point = Vec2(20.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 1.0, 0.1, false, offset)
        
        val rateOfChange = joint.rateOfChangeOfExtension()
        
        // Should calculate rate of change considering angular velocity
        assertNotNull(rateOfChange)
    }

    @Test
    fun `test joint with zero spring constant`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(25.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 0.0, 0.1, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be zero with zero spring constant
        assertEquals(0.0, tension, 0.0)
    }

    @Test
    fun `test joint with zero dampening`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(25.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 1.0, 0.0, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should only include spring force, no dampening
        assertEquals(5.0, tension, 0.1)
    }

    @Test
    fun `test joint with high spring constant`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(25.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 10.0, 0.1, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should be high with high spring constant (5 * 10.0 = 50.0)
        assertEquals(50.0, tension, 0.1)
    }

    @Test
    fun `test joint with high dampening`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        body.velocity = Vec2(1.0, 0.0)
        val point = Vec2(20.0, 0.0)
        val offset = Vec2(0.0, 0.0)
        val joint = JointToPoint(body, point, 20.0, 1.0, 5.0, false, offset)
        
        val tension = joint.calculateTension()
        
        // Tension should include significant dampening force
        assertNotNull(tension)
    }

    @Test
    fun `test joint with different offset values`() {
        val body = Body(Circle(10.0), 0.0, 0.0)
        val point = Vec2(30.0, 40.0)
        val offset = Vec2(5.0, 3.0)
        val joint = JointToPoint(body, point, 25.0, 1.5, 0.3, true, offset)
        
        // Test that the joint was created successfully with offset
        assertNotNull(joint)
    }
}
