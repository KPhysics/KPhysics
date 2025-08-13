package de.chaffic.collision

import de.chaffic.dynamics.Body
import de.chaffic.dynamics.World
import de.chaffic.geometry.Circle
import de.chaffic.geometry.Polygon
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class ArbiterTest {

    @Test
    fun `test constructor with collision bodies`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 20.0, 0.0)
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        assertEquals(bodyA, arbiter.a)
        assertEquals(bodyB, arbiter.b)
    }

    @Test
    fun `test narrow phase circle circle collision`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        assertEquals(1, arbiter.contactCount)
        assertTrue(arbiter.contactNormal.length() > 0)
    }

    @Test
    fun `test narrow phase circle circle no collision`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 25.0, 0.0) // Not overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        assertEquals(0, arbiter.contactCount)
    }

    @Test
    fun `test narrow phase circle polygon collision`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Polygon(20.0, 20.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        assertEquals(1, arbiter.contactCount)
    }

    @Test
    fun `test narrow phase polygon circle collision`() {
        val world = World()
        val bodyA = Body(Polygon(20.0, 20.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        assertEquals(1, arbiter.contactCount)
        // Contact normal should be flipped
        assertTrue(arbiter.contactNormal.length() > 0)
    }

    @Test
    fun `test narrow phase polygon polygon collision`() {
        val world = World()
        val bodyA = Body(Polygon(20.0, 20.0), 0.0, 0.0)
        val bodyB = Body(Polygon(20.0, 20.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        assertEquals(1, arbiter.contactCount)
    }

    @Test
    fun `test penetration resolution`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        val originalPosA = bodyA.position.copy()
        val originalPosB = bodyB.position.copy()
        
        arbiter.penetrationResolution()
        
        // Positions should have changed due to penetration resolution
        assertNotEquals(originalPosA, bodyA.position)
        assertNotEquals(originalPosB, bodyB.position)
    }

    @Test
    fun `test solve method`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0) // Overlapping
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        try {
            arbiter.solve()
        } catch (e: Exception) {
            fail("solve method should not throw an exception: ${e.message}")
        }
    }

    @Test
    fun `test isPointInside static method`() {
        val world = World()
        val body = Body(Circle(10.0), 0.0, 0.0)
        world.addBody(body)
        
        val pointInside = Vec2(5.0, 5.0)
        val pointOutside = Vec2(15.0, 15.0)
        
        assertTrue(Arbiter.isPointInside(body, pointInside))
        assertFalse(Arbiter.isPointInside(body, pointOutside))
    }

    @Test
    fun `test restitution calculation`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0)
        bodyA.restitution = 0.5
        bodyB.restitution = 0.8
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        arbiter.narrowPhase()
        
        // Should use the minimum restitution value
        assertEquals(0.5, arbiter.restitution, 0.001)
    }

    @Test
    fun `test friction calculation`() {
        val world = World()
        val bodyA = Body(Circle(10.0), 0.0, 0.0)
        val bodyB = Body(Circle(10.0), 15.0, 0.0)
        bodyA.staticFriction = 0.3
        bodyB.staticFriction = 0.7
        bodyA.dynamicFriction = 0.2
        bodyB.dynamicFriction = 0.6
        world.addBody(bodyA)
        world.addBody(bodyB)
        
        val arbiter = Arbiter(bodyA, bodyB)
        // Friction is calculated in constructor
        arbiter.narrowPhase()
        
        // Test that solve method works with calculated friction
        arbiter.solve()
        assertEquals(1, arbiter.contactCount)
    }
}
