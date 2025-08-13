package de.chaffic.explosions

import de.chaffic.dynamics.World
import de.chaffic.dynamics.Body
import de.chaffic.geometry.Circle
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class ProximityExplosionTest {

    @Test
    fun `test constructor with epicentre and proximity`() {
        val epicentre = Vec2(100.0, 200.0)
        val proximity = 50
        
        val explosion = ProximityExplosion(epicentre, proximity)
        
        assertNotNull(explosion)
        assertEquals(epicentre, explosion.getEpicentre())
        assertEquals(proximity, explosion.proximity)
    }

    @Test
    fun `test apply blast impulse method`() {
        val epicentre = Vec2(50.0, 75.0)
        val proximity = 30
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        // Add bodies within explosion proximity
        val body1 = Body(Circle(10.0), 60.0, 80.0)
        val body2 = Body(Circle(10.0), 40.0, 70.0)
        val body3 = Body(Circle(10.0), 100.0, 100.0) // Outside proximity
        
        world.addBody(body1)
        world.addBody(body2)
        world.addBody(body3)
        
        // Update the explosion to detect bodies
        explosion.update(world.bodies)
        
        try {
            explosion.applyBlastImpulse(100.0)
            // Method should not crash
        } catch (e: Exception) {
            fail("applyBlastImpulse method should not crash: ${e.message}")
        }
    }

    @Test
    fun `test apply blast impulse with empty world`() {
        val epicentre = Vec2(0.0, 0.0)
        val proximity = 25
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        // Update the explosion to detect bodies (empty)
        explosion.update(world.bodies)
        
        try {
            explosion.applyBlastImpulse(50.0)
            // Method should not crash even with empty world
        } catch (e: Exception) {
            fail("applyBlastImpulse method should not crash with empty world: ${e.message}")
        }
    }

    @Test
    fun `test update method`() {
        val epicentre = Vec2(100.0, 100.0)
        val proximity = 40
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        // Add bodies within explosion proximity
        val body1 = Body(Circle(10.0), 110.0, 110.0)
        val body2 = Body(Circle(10.0), 90.0, 90.0)
        val body3 = Body(Circle(10.0), 150.0, 150.0) // Outside proximity
        
        world.addBody(body1)
        world.addBody(body2)
        world.addBody(body3)
        
        try {
            explosion.update(world.bodies)
            // Method should not crash
        } catch (e: Exception) {
            fail("update method should not crash: ${e.message}")
        }
    }

    @Test
    fun `test set epicentre method`() {
        val epicentre = Vec2(75.0, 125.0)
        val proximity = 25
        val explosion = ProximityExplosion(epicentre, proximity)
        
        assertEquals(epicentre, explosion.getEpicentre())
        
        // Test that epicentre can be modified
        val newEpicentre = Vec2(150.0, 250.0)
        explosion.setEpicentre(newEpicentre)
        assertEquals(newEpicentre, explosion.getEpicentre())
    }

    @Test
    fun `test proximity property`() {
        val epicentre = Vec2(0.0, 0.0)
        val proximity = 35
        val explosion = ProximityExplosion(epicentre, proximity)
        
        assertEquals(proximity, explosion.proximity)
    }

    @Test
    fun `test proximity explosion with zero proximity`() {
        val epicentre = Vec2(0.0, 0.0)
        val proximity = 0
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        explosion.update(world.bodies)
        
        try {
            explosion.applyBlastImpulse(100.0)
            // Method should not crash
        } catch (e: Exception) {
            fail("applyBlastImpulse method should not crash with zero proximity: ${e.message}")
        }
    }

    @Test
    fun `test proximity explosion with negative proximity`() {
        val epicentre = Vec2(0.0, 0.0)
        val proximity = -10
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        explosion.update(world.bodies)
        
        try {
            explosion.applyBlastImpulse(100.0)
            // Method should not crash
        } catch (e: Exception) {
            fail("applyBlastImpulse method should not crash with negative proximity: ${e.message}")
        }
    }

    @Test
    fun `test proximity explosion with very large proximity`() {
        val epicentre = Vec2(0.0, 0.0)
        val proximity = 1000
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        explosion.update(world.bodies)
        
        try {
            explosion.applyBlastImpulse(100.0)
            // Method should not crash
        } catch (e: Exception) {
            fail("applyBlastImpulse method should not crash with large proximity: ${e.message}")
        }
    }

    @Test
    fun `test update lines to body method`() {
        val epicentre = Vec2(200.0, 300.0)
        val proximity = 45
        val explosion = ProximityExplosion(epicentre, proximity)
        val world = World(Vec2(0.0, -9.81))
        
        // Add a body and update
        val body = Body(Circle(10.0), 210.0, 310.0)
        world.addBody(body)
        explosion.update(world.bodies)
        
        try {
            explosion.updateLinesToBody()
            // Method should not crash
        } catch (e: Exception) {
            fail("updateLinesToBody method should not crash: ${e.message}")
        }
    }
}
