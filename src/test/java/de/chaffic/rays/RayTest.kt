package de.chaffic.rays

import de.chaffic.dynamics.Body
import de.chaffic.dynamics.World
import de.chaffic.geometry.Circle
import de.chaffic.geometry.Polygon
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class RayTest {

    @Test
    fun `test polygon right`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 300.0, 0.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.RIGHT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(250.0, 0.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon left`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), -300.0, 0.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.LEFT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(-250.0, 0.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon up`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 0.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.UP, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(0.0, 200.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon down`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 0.0, -300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.DOWN, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(0.0, -200.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon right offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 300.0), Vec2.RIGHT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(250.0, 300.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon left offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), -300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 300.0), Vec2.LEFT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(-250.0, 300.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon up offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(300.0, 0.0), Vec2.UP, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(300.0, 200.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon down offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 300.0, -300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(300.0, 0.0), Vec2.DOWN, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
        assertEquals(Vec2(300.0, -200.0), ray.rayInformation?.coordinates)
    }

    @Test
    fun `test polygon diagonal`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Polygon(50.0, 100.0), 0.0, -300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(300.0, 0.0), Vec2(1.0, 1.0), 800.0)
        ray.updateProjection(world.bodies)

        assertNull(ray.rayInformation)
    }

    @Test
    fun `test circle right`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 300.0, 0.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.RIGHT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle left`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), -300.0, 0.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.LEFT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle up`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 0.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.UP, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle down`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 0.0, -300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 0.0), Vec2.DOWN, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle right offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 300.0), Vec2.RIGHT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle left offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), -300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(0.0, 300.0), Vec2.LEFT, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle up offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 300.0, 300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(300.0, 0.0), Vec2.UP, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }

    @Test
    fun `test circle down offset`() {
        val world = World(Vec2(0.0, -9.81))

        val platform = Body(Circle(50.0), 300.0, -300.0)
        platform.density = 0.0

        world.addBody(platform)

        val ray = Ray(Vec2(300.0, 0.0), Vec2.DOWN, 800.0)
        ray.updateProjection(world.bodies)

        assertEquals(platform, ray.rayInformation?.b)
    }
}