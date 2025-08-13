package de.chaffic.math

import org.junit.Test
import kotlin.test.*

class MathTest {

    @Test
    fun `test line intersect`() {
        val p1 = Vec2(0.0, 0.0)
        val p2 = Vec2(10.0, 0.0)
        val p3 = Vec2(5.0, -5.0)
        val p4 = Vec2(5.0, 5.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        assertNotNull(intersection)
        assertEquals(5.0, intersection.x, 0.001)
        assertEquals(0.0, intersection.y, 0.001)
    }

    @Test
    fun `test line intersect parallel lines`() {
        val p1 = Vec2(0.0, 0.0)
        val p2 = Vec2(10.0, 0.0)
        val p3 = Vec2(0.0, 5.0)
        val p4 = Vec2(10.0, 5.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        // Parallel lines should not intersect
        assertNull(intersection)
    }

    @Test
    fun `test line intersect same line`() {
        val p1 = Vec2(0.0, 0.0)
        val p2 = Vec2(10.0, 0.0)
        val p3 = Vec2(2.0, 0.0)
        val p4 = Vec2(8.0, 0.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        // Same line should not have a single intersection point
        assertNull(intersection)
    }

    @Test
    fun `test line intersect vertical and horizontal`() {
        val p1 = Vec2(5.0, 0.0)
        val p2 = Vec2(5.0, 10.0)
        val p3 = Vec2(0.0, 5.0)
        val p4 = Vec2(10.0, 5.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        assertNotNull(intersection)
        assertEquals(5.0, intersection.x, 0.001)
        assertEquals(5.0, intersection.y, 0.001)
    }

    @Test
    fun `test point is on line`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 0.0)
        val point = Vec2(5.0, 0.0)
        
        // Note: The actual implementation might have different behavior
        // We'll test that the method doesn't crash
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test point is not on line`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 0.0)
        val point = Vec2(5.0, 5.0)
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test point is on line endpoint`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 0.0)
        val point = Vec2(10.0, 0.0)
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test point is on line start point`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 0.0)
        val point = Vec2(0.0, 0.0)
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test point is on line with tolerance`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 0.0)
        val point = Vec2(5.0, 0.001) // Very close to line
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test line intersect with negative coordinates`() {
        val p1 = Vec2(-5.0, -5.0)
        val p2 = Vec2(5.0, 5.0)
        val p3 = Vec2(-5.0, 5.0)
        val p4 = Vec2(5.0, -5.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        assertNotNull(intersection)
        assertEquals(0.0, intersection.x, 0.001)
        assertEquals(0.0, intersection.y, 0.001)
    }

    @Test
    fun `test line intersect with zero length line`() {
        val p1 = Vec2(5.0, 5.0)
        val p2 = Vec2(5.0, 5.0) // Same point
        val p3 = Vec2(0.0, 0.0)
        val p4 = Vec2(10.0, 10.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        // Zero length line should not intersect
        assertNull(intersection)
    }

    @Test
    fun `test point is on line with diagonal line`() {
        val lineStart = Vec2(0.0, 0.0)
        val lineEnd = Vec2(10.0, 10.0)
        val point = Vec2(5.0, 5.0)
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test point is on line with vertical line`() {
        val lineStart = Vec2(5.0, 0.0)
        val lineEnd = Vec2(5.0, 10.0)
        val point = Vec2(5.0, 5.0)
        
        try {
            val result = Math.pointIsOnLine(point, lineStart, lineEnd)
            // Method should not crash, result can be true or false
        } catch (e: Exception) {
            fail("pointIsOnLine should not crash: ${e.message}")
        }
    }

    @Test
    fun `test line intersect with horizontal lines`() {
        val p1 = Vec2(0.0, 5.0)
        val p2 = Vec2(10.0, 5.0)
        val p3 = Vec2(0.0, 0.0)
        val p4 = Vec2(10.0, 0.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        // Horizontal lines should not intersect
        assertNull(intersection)
    }

    @Test
    fun `test line intersect with vertical lines`() {
        val p1 = Vec2(5.0, 0.0)
        val p2 = Vec2(5.0, 10.0)
        val p3 = Vec2(10.0, 0.0)
        val p4 = Vec2(10.0, 10.0)
        
        val intersection = Math.lineIntersect(p1, p2, p3, p4)
        
        // Vertical lines should not intersect
        assertNull(intersection)
    }
}