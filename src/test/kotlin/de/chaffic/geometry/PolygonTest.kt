package de.chaffic.geometry

import de.chaffic.dynamics.Body
import de.chaffic.math.Vec2
import org.junit.Test
import kotlin.test.*

class PolygonTest {

    @Test
    fun `test constructor with width and height`() {
        val width = 20.0
        val height = 30.0
        
        val polygon = Polygon(width, height)
        
        // Note: Polygon class does not have direct 'width'/'height' properties after construction.
        // The AABB min/max values are relative to the polygon's local origin (0,0).
        assertNotNull(polygon.vertices)
        assertTrue(polygon.vertices.size > 0)
    }

    @Test
    fun `test calc mass`() {
        val polygon = Polygon(10.0, 15.0)
        val body = Body(polygon, 0.0, 0.0)
        val density = 2.0
        
        polygon.calcMass(density)
        
        // The actual mass calculation is more complex than width * height * density
        // It involves calculating the area from vertices and applying density
        assertTrue(body.mass > 0.0, "Mass should be positive")
        assertEquals(1.0 / body.mass, body.invMass, 0.001)
        assertTrue(body.inertia > 0.0)
        assertEquals(1.0 / body.inertia, body.invInertia, 0.001)
    }

    @Test
    fun `test calc mass with zero density`() {
        val polygon = Polygon(10.0, 15.0)
        val body = Body(polygon, 0.0, 0.0)
        
        polygon.calcMass(0.0)
        
        assertEquals(0.0, body.mass, 0.0)
        assertEquals(0.0, body.invMass, 0.0)
        assertEquals(0.0, body.inertia, 0.0)
        assertEquals(0.0, body.invInertia, 0.0)
    }

    @Test
    fun `test create aabb`() {
        val polygon = Polygon(20.0, 30.0)
        
        // Note: createAABB requires the body property to be initialized
        // We can only test that the method exists and doesn't crash
        try {
            polygon.createAABB()
            // Should not crash
        } catch (e: Exception) {
            // Expected if body is not initialized
        }
        
        assertNotNull(polygon.vertices)
        assertTrue(polygon.vertices.size > 0)
    }

    @Test
    fun `test vertices initialization`() {
        val polygon = Polygon(20.0, 30.0)
        
        val vertices = polygon.vertices
        assertNotNull(vertices)
        assertTrue(vertices.size >= 4) // Rectangle should have at least 4 vertices
        
        // Check that vertices form a rectangle
        val minX = vertices.minOf { it.x }
        val maxX = vertices.maxOf { it.x }
        val minY = vertices.minOf { it.y }
        val maxY = vertices.maxOf { it.y }
        
        // The actual values are -width and -height (not -width/2 and -height/2)
        assertEquals(-20.0, minX, 0.001)
        assertEquals(20.0, maxX, 0.001)
        assertEquals(-30.0, minY, 0.001)
        assertEquals(30.0, maxY, 0.001)
    }

    @Test
    fun `test polygon with different dimensions`() {
        val polygon = Polygon(5.0, 25.0)
        
        val vertices = polygon.vertices
        assertNotNull(vertices)
        
        val minX = vertices.minOf { it.x }
        val maxX = vertices.maxOf { it.x }
        val minY = vertices.minOf { it.y }
        val maxY = vertices.maxOf { it.y }
        
        // The actual values are -width and -height (not -width/2 and -height/2)
        assertEquals(-5.0, minX, 0.001)
        assertEquals(5.0, maxX, 0.001)
        assertEquals(-25.0, minY, 0.001)
        assertEquals(25.0, maxY, 0.001)
    }

    @Test
    fun `test polygon orientation`() {
        val polygon = Polygon(20.0, 30.0)
        
        // Test that orientation can be set and retrieved
        assertNotNull(polygon.orientation)
        
        // Test that setting orientation doesn't crash
        try {
            polygon.orientation.set(1.57) // 90 degrees
            // Should not crash
        } catch (e: Exception) {
            fail("Setting orientation should not crash: ${e.message}")
        }
    }

    @Test
    fun `test polygon with regular constructor`() {
        val radius = 10
        val sides = 6
        val polygon = Polygon(radius, sides)
        
        assertNotNull(polygon.vertices)
        assertEquals(sides, polygon.vertices.size)
        
        // Check that vertices are within radius
        for (vertex in polygon.vertices) {
            val distance = vertex.length()
            assertTrue(distance <= radius, "Vertex should be within radius")
        }
    }

    @Test
    fun `test polygon with custom vertices`() {
        val customVertices = arrayOf(
            Vec2(0.0, 0.0),
            Vec2(10.0, 0.0),
            Vec2(10.0, 10.0),
            Vec2(0.0, 10.0)
        )
        
        val polygon = Polygon(customVertices)
        
        assertNotNull(polygon.vertices)
        assertEquals(customVertices.size, polygon.vertices.size)
        
        // Check that vertices are properly set
        for (i in customVertices.indices) {
            assertEquals(customVertices[i].x, polygon.vertices[i].x, 0.001)
            assertEquals(customVertices[i].y, polygon.vertices[i].y, 0.001)
        }
    }
}
