package de.chaffic.explosions

import de.chaffic.dynamics.World
import de.chaffic.geometry.Circle
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class ParticleExplosionTest {

    @Test
    fun `test constructor`() {
        val epicentre = Vec2(100.0, 200.0)
        val noOfParticles = 50
        val lifespan = 5.0
        
        val explosion = ParticleExplosion(epicentre, noOfParticles, lifespan)
        
        assertEquals(noOfParticles, explosion.particles.size)
    }

    @Test
    fun `test particles initialization`() {
        val explosion = ParticleExplosion(Vec2(), 10, 5.0)
        
        assertEquals(10, explosion.particles.size)
        explosion.particles.forEach { particle ->
            assertNotNull(particle)
            assertTrue(particle.shape is Circle)
        }
    }

    @Test
    fun `test create particles`() {
        val world = World()
        val epicentre = Vec2(0.0, 0.0)
        val explosion = ParticleExplosion(epicentre, 8, 5.0)
        val size = 2.0
        val density = 1
        val radius = 100
        
        explosion.createParticles(size, density, radius, world)
        
        // Should create particles in the world
        assertEquals(8, world.bodies.size)
        
        // Check that particles are positioned around epicentre
        explosion.particles.forEach { particle ->
            val distance = particle.position.distance(epicentre)
            assertTrue(distance <= radius + size)
            assertTrue(distance >= radius - size)
        }
    }

    @Test
    fun `test particle properties after creation`() {
        val world = World()
        val explosion = ParticleExplosion(Vec2(), 4, 5.0)
        val size = 1.0
        val density = 2
        val radius = 50
        
        explosion.createParticles(size, density, radius, world)
        
        explosion.particles.forEach { particle ->
            assertEquals(size, (particle.shape as Circle).radius, 0.0)
            assertEquals(density.toDouble(), particle.density, 0.0)
            assertEquals(1.0, particle.restitution, 0.0)
            assertEquals(0.0, particle.staticFriction, 0.0)
            assertEquals(0.0, particle.dynamicFriction, 0.0)
            assertFalse(particle.affectedByGravity)
            assertTrue(particle.particle)
            assertEquals(0.0, particle.linearDampening, 0.0)
        }
    }

    @Test
    fun `test apply blast impulse`() {
        val world = World()
        val epicentre = Vec2(0.0, 0.0)
        val explosion = ParticleExplosion(epicentre, 4, 5.0)
        explosion.createParticles(1.0, 1, 50, world)
        
        val blastPower = 10.0
        val originalVelocities = explosion.particles.map { it.velocity.copy() }
        
        explosion.applyBlastImpulse(blastPower)
        
        // All particles should have velocity away from epicentre
        explosion.particles.forEachIndexed { index, particle ->
            val originalVel = originalVelocities[index]
            assertNotEquals(originalVel, particle.velocity)
            
            // Velocity should point away from epicentre
            val direction = particle.position.minus(epicentre)
            val velocityDirection = particle.velocity
            val dotProduct = direction.dot(velocityDirection)
            assertTrue(dotProduct > 0, "Particle velocity should point away from epicentre")
        }
    }

    @Test
    fun `test apply blast impulse with zero power`() {
        val world = World()
        val explosion = ParticleExplosion(Vec2(), 4, 5.0)
        explosion.createParticles(1.0, 1, 50, world)
        
        val originalVelocities = explosion.particles.map { it.velocity.copy() }
        
        explosion.applyBlastImpulse(0.0)
        
        // Velocities should remain unchanged
        explosion.particles.forEachIndexed { index, particle ->
            assertEquals(originalVelocities[index], particle.velocity)
        }
    }

    @Test
    fun `test apply blast impulse with negative power`() {
        val world = World()
        val epicentre = Vec2(0.0, 0.0)
        val explosion = ParticleExplosion(epicentre, 4, 5.0)
        explosion.createParticles(1.0, 1, 50, world)
        
        val blastPower = -5.0
        val originalVelocities = explosion.particles.map { it.velocity.copy() }
        
        explosion.applyBlastImpulse(blastPower)
        
        // All particles should have velocity toward epicentre
        explosion.particles.forEachIndexed { index, particle ->
            val originalVel = originalVelocities[index]
            assertNotEquals(originalVel, particle.velocity)
            
            // Velocity should point toward epicentre
            val direction = particle.position.minus(epicentre)
            val velocityDirection = particle.velocity
            val dotProduct = direction.dot(velocityDirection)
            assertTrue(dotProduct < 0, "Particle velocity should point toward epicentre")
        }
    }

    @Test
    fun `test particle distribution around epicentre`() {
        val world = World()
        val epicentre = Vec2(100.0, 200.0)
        val explosion = ParticleExplosion(epicentre, 12, 5.0)
        val radius = 75
        
        explosion.createParticles(1.0, 1, radius, world)
        
        // Check that particles are distributed in a circle around epicentre
        val angles = mutableSetOf<Double>()
        explosion.particles.forEach { particle ->
            val direction = particle.position.minus(epicentre)
            val angle = Math.atan2(direction.y, direction.x)
            angles.add(angle)
        }
        
        // Should have particles at different angles (not all at same angle)
        assertTrue(angles.size > 1)
    }

    @Test
    fun `test particle creation with different parameters`() {
        val world = World()
        val explosion = ParticleExplosion(Vec2(), 6, 3.0)
        
        // Test with different sizes
        explosion.createParticles(0.5, 1, 25, world)
        explosion.particles.forEach { particle ->
            assertEquals(0.5, (particle.shape as Circle).radius, 0.0)
        }
        
        // Test with different densities
        world.clearWorld()
        explosion.createParticles(1.0, 5, 30, world)
        explosion.particles.forEach { particle ->
            assertEquals(5.0, particle.density, 0.0)
        }
        
        // Test with different radii
        world.clearWorld()
        explosion.createParticles(1.0, 1, 100, world)
        explosion.particles.forEach { particle ->
            val distance = particle.position.distance(Vec2())
            assertTrue(distance <= 100 + 1.0)
            assertTrue(distance >= 100 - 1.0)
        }
    }
}
