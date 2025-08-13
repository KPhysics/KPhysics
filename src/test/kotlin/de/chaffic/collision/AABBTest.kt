package de.chaffic.collision

import de.chaffic.math.Vec2
import de.chaffic.dynamics.Body
import de.chaffic.geometry.Circle
import org.junit.Test
import kotlin.test.*

class AABBTest {
    
    @Test
    fun `test set method`() {
        val b = AxisAlignedBoundingBox()
        val a = AxisAlignedBoundingBox(Vec2(10.0, 10.0), Vec2(20.0, 20.0))
        b.set(a)
        val val_ = Vec2(10.0, 10.0)
        assertEquals(val_.x, b.min.x, 0.0)
        assertEquals(val_.y, b.min.y, 0.0)
        assertEquals(Vec2(20.0, 20.0).x, b.max.x, 0.0)
        assertEquals(Vec2(20.0, 20.0).y, b.max.y, 0.0)
    }

    @Test
    fun `test getMin method`() {
        val b = AxisAlignedBoundingBox()
        val a = AxisAlignedBoundingBox(Vec2(10.0, 10.0), Vec2(20.0, 20.0))
        b.set(a)
        val val_ = Vec2(10.0, 10.0)
        assertEquals(val_.x, b.min.x, 0.0)
        assertEquals(val_.y, b.min.y, 0.0)
    }

    @Test
    fun `test getMax method`() {
        val b = AxisAlignedBoundingBox()
        val a = AxisAlignedBoundingBox(Vec2(10.0, 10.0), Vec2(20.0, 20.0))
        b.set(a)
        assertEquals(Vec2(20.0, 20.0).x, b.max.x, 0.0)
        assertEquals(Vec2(20.0, 20.0).y, b.max.y, 0.0)
    }

    @Test
    fun `test isValid method`() {
        var a = AxisAlignedBoundingBox(Vec2(100.0, 100.0), Vec2(300.0, 300.0))
        assertTrue(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(0.0, 0.0))
        assertTrue(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(Double.POSITIVE_INFINITY, 0.0), Vec2(300.0, 300.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(Double.POSITIVE_INFINITY, 300.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(1.0, Double.POSITIVE_INFINITY))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, Double.POSITIVE_INFINITY), Vec2(1.0, 1.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, -Double.POSITIVE_INFINITY), Vec2(1.0, 1.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, Double.NaN), Vec2(1.0, 1.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(0.0, Double.NaN), Vec2(Double.NaN, 1.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(-0.0, -0.0), Vec2(-0.0, -0.0))
        assertTrue(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(-10.0, -10.0), Vec2(-0.0, -0.0))
        assertTrue(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(-0.0, -0.0), Vec2(-10.0, -10.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(-10.0, -0.0), Vec2(-10.0, -10.0))
        assertFalse(a.isValid)
        
        a = AxisAlignedBoundingBox(Vec2(-10.0, -0.0), Vec2(-10.0, -10.0))
        assertFalse(a.isValid)
    }

    @Test
    fun `test AABB overlap`() {
        // Corner overlaps - top right
        var a = AxisAlignedBoundingBox(Vec2(100.0, 100.0), Vec2(300.0, 300.0))
        var b = AxisAlignedBoundingBox(Vec2(200.0, 200.0), Vec2(400.0, 400.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Corner overlaps - top left
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(200.0, 200.0))
        b = AxisAlignedBoundingBox(Vec2(-100.0, 100.0), Vec2(100.0, 300.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Corner overlaps - bottom left
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(200.0, 200.0))
        b = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Corner overlaps - bottom right
        a = AxisAlignedBoundingBox(Vec2(0.0, 0.0), Vec2(200.0, 200.0))
        b = AxisAlignedBoundingBox(Vec2(100.0, -100.0), Vec2(300.0, 100.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - middle left
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-150.0, -50.0), Vec2(50.0, 50.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - middle right
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(50.0, -50.0), Vec2(150.0, 50.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - middle
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-150.0, -50.0), Vec2(150.0, 50.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - top
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, -50.0), Vec2(50.0, 150.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - bottom
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, -150.0), Vec2(50.0, 50.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Middle overlaps - bottom
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, -150.0), Vec2(50.0, 150.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Within
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, -50.0), Vec2(50.0, 50.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 1
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-200.0, 200.0), Vec2(100.0, 500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 2
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, 200.0), Vec2(50.0, 500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 3
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(500.0, 200.0), Vec2(570.0, 500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 4
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-200.0, -50.0), Vec2(-150.0, 50.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 6
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(200.0, -50.0), Vec2(250.0, 50.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 7
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-200.0, -2000.0), Vec2(-100.0, -500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 8
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(-80.0, -800.0), Vec2(50.0, -500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))

        // Quadrant 9
        a = AxisAlignedBoundingBox(Vec2(-100.0, -100.0), Vec2(100.0, 100.0))
        b = AxisAlignedBoundingBox(Vec2(500.0, -700.0), Vec2(570.0, -500.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))
    }

    @Test
    fun `test AABB overlap with point`() {
        val b = AxisAlignedBoundingBox(Vec2(100.0, 300.0), Vec2(300.0, 100.0))
        var point = Vec2(100.0, 100.0)
        assertTrue(b.aabbOverlap(point))
        
        // Checks if it's inside
        point = Vec2(150.0, 120.0)
        assertTrue(b.aabbOverlap(point))

        point = Vec2(100.0, 100.0)
        assertTrue(b.aabbOverlap(point))
        point = Vec2(100.0, 300.0)
        assertTrue(b.aabbOverlap(point))

        // Checks if it's outside
        point = Vec2(50.0, 100.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(50.0, 50.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(150.0, 50.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(350.0, 50.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(350.0, 200.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(350.0, 500.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(200.0, 500.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(50.0, 500.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(50.0, 200.0)
        assertFalse(b.aabbOverlap(point))

        point = Vec2(100.0, 500.0)
        assertFalse(b.aabbOverlap(point))
        point = Vec2(500.0, 100.0)
        assertFalse(b.aabbOverlap(point))
    }

    @Test
    fun `test copy method`() {
        val a = AxisAlignedBoundingBox(Vec2(-10.0, 10.0), Vec2(10.0, 10.0))
        val b = a.copy()

        assertNotSame(b, a)
        assertEquals(a.min.x, b.min.x)
        assertEquals(a.min.y, b.min.y)
        assertEquals(a.max.x, b.max.x)
        assertEquals(a.max.y, b.max.y)
    }

    @Test
    fun `test addOffset method`() {
        val a = AxisAlignedBoundingBox(Vec2(-10.0, 10.0), Vec2(10.0, 10.0))
        a.addOffset(Vec2(10.0, 10.0))
        assertEquals(0.0, a.min.x)
        assertEquals(20.0, a.min.y)
        assertEquals(20.0, a.max.x)
        assertEquals(20.0, a.max.y)
    }

    @Test
    fun `test body overlap`() {
        val a = Body(Circle(20.0), 0.0, 0.0)
        val b = Body(Circle(20.0), 0.0, 0.0)
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))
        
        a.position.add(Vec2(41.0, 0.0))
        assertFalse(AxisAlignedBoundingBox.aabbOverlap(a, b))
        
        a.position.add(Vec2(-6.0, 10.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))
        
        a.position.add(Vec2(-34.0, -38.0))
        assertTrue(AxisAlignedBoundingBox.aabbOverlap(a, b))
    }
}
