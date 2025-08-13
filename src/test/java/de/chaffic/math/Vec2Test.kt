package de.chaffic.math

import org.junit.Test
import kotlin.test.*

class Vec2Test {
    
    @Test
    fun `test set using double parameters`() {
        val vec = Vec2(4.0, 2.0)
        vec.set(33.0, 7.0)
        assertEquals(33.0, vec.x, 0.0)
        assertEquals(7.0, vec.y, 0.0)
    }

    @Test
    fun `test set to vector`() {
        val vec = Vec2(103.2, -2489273423.2)
        vec.set(Vec2(-42.4, 92.1))
        assertEquals(-42.4, vec.x, 0.0)
        assertEquals(92.1, vec.y, 0.0)
    }

    @Test
    fun `test copy`() {
        val vec1 = Vec2(1.0, 1.0)
        val vec2 = vec1.copy()
        assertEquals(vec1.x, vec2.x, 0.0)
        assertEquals(vec1.y, vec2.y, 0.0)
        assertNotSame(vec1, vec2)
    }

    @Test
    fun `test negative`() {
        val vec = Vec2(5.0, -7.0)
        vec.unaryMinus()
        assertEquals(-5.0, vec.x, 0.0)
        assertEquals(7.0, vec.y, 0.0)

        val vec1 = vec.unaryMinus()
        assertEquals(5.0, vec1.x, 0.0)
        assertEquals(-7.0, vec1.y, 0.0)
        assertEquals(5.0, vec.x, 0.0)
        assertEquals(-7.0, vec.y, 0.0)
    }

    @Test
    fun `test negative vector`() {
        val vec1 = Vec2(5.0, 1.0)
        val vec2 = vec1.copyNegative()
        assertEquals(5.0, vec1.x, 0.0)
        assertEquals(1.0, vec1.y, 0.0)
        assertEquals(-5.0, vec2.x, 0.0)
        assertEquals(-1.0, vec2.y, 0.0)
    }

    @Test
    fun `test add`() {
        val vec1 = Vec2(5.0, 2.0)
        val vec2 = Vec2(7.0, 1.0)
        vec1.add(vec2)
        assertEquals(12.0, vec1.x, 0.0)
        assertEquals(3.0, vec1.y, 0.0)
    }

    @Test
    fun `test add immutable`() {
        val vec1 = Vec2(5.0, 2.0)
        val vec2 = Vec2(7.0, 1.0)
        val result = vec1.plus(vec2)
        assertEquals(5.0, vec1.x, 0.0)
        assertEquals(2.0, vec1.y, 0.0)
        assertEquals(12.0, result.x, 0.0)
        assertEquals(3.0, result.y, 0.0)
    }

    @Test
    fun `test normal`() {
        val vec1 = Vec2(0.0, 1.0)
        val val_ = vec1.normal()
        assertEquals(-1.0, val_.x, 0.0)
        assertEquals(0.0, val_.y, 0.0)
    }

    @Test
    fun `test normalize`() {
        val vec1 = Vec2(-345.34, 745.0)
        vec1.normalize()
        assertEquals(1.0, vec1.length(), 0.0)
        assertEquals(-0.4205573495355269, vec1.x, 0.0)
        assertEquals(0.9072659564602061, vec1.y, 0.0)
    }

    @Test
    fun `test get normalized`() {
        val vec1 = Vec2(1.0, 7.0)
        val val_ = vec1.normalized
        assertEquals(0.1414213562373095, val_.x, 0.0)
        assertEquals(0.9899494936611665, val_.y, 0.0)

        assertEquals(1.0, vec1.x, 0.0)
        assertEquals(7.0, vec1.y, 0.0)
    }

    @Test
    fun `test distance`() {
        val vec1 = Vec2(5.0, 2.0)
        val vec2 = Vec2(7.0, 1.0)
        val dist = vec1.distance(vec2)
        // square root of 5
        assertEquals(2.2361, dist, 0.0001)
    }

    @Test
    fun `test subtract`() {
        val vec1 = Vec2(5.0, 2.0)
        val vec2 = Vec2(7.0, 1.0)
        val result = vec1.minus(vec2)
        assertEquals(-2.0, result.x, 0.0)
        assertEquals(1.0, result.y, 0.0)
    }

    @Test
    fun `test vector cross product`() {
        val vec1 = Vec2(2.0, 3.0)
        val vec2 = Vec2(5.0, 6.0)
        val i = vec1.cross(vec2)
        assertEquals(-3.0, i, 0.0)
    }

    @Test
    fun `test scalar cross product`() {
        val vec1 = Vec2(2.0, 3.0)
        val cross = vec1.cross(4.0)
        assertEquals(2.0, vec1.x, 0.0)
        assertEquals(3.0, vec1.y, 0.0)

        assertEquals(-12.0, cross.x, 0.0)
        assertEquals(8.0, cross.y, 0.0)
    }

    @Test
    fun `test scalar`() {
        val vec1 = Vec2(5.0, 2.0)
        val vec2 = vec1.scalar(4.0)
        assertEquals(5.0, vec1.x, 0.0)
        assertEquals(2.0, vec1.y, 0.0)
        assertEquals(20.0, vec2.x, 0.0)
        assertEquals(8.0, vec2.y, 0.0)
    }

    @Test
    fun `test dot product`() {
        val vec1 = Vec2(5.0, 1.0)
        val vec2 = Vec2(15.0, 10.0)
        val i = vec1.dot(vec2)
        assertEquals(85.0, i, 0.0)
    }

    @Test
    fun `test length`() {
        val vec1 = Vec2(0.0, 7.0)
        val val_ = vec1.length()
        assertEquals(7.0, val_, 0.0)
    }

    @Test
    fun `test cross1`() {
        var vec1 = Vec2(5.03, 1.30)
        var w = Vec2.cross(6.0, vec1)
        // Vec2.cross(scalar, vector) returns (-scalar * vector.y, scalar * vector.x)
        assertEquals(-7.8, w.x, 1e-15)
        assertEquals(30.18, w.y, 1e-15)

        vec1 = Vec2(-3.75, 9.34)
        w = Vec2.cross(1003.4, vec1)
        assertEquals(-9371.756, w.x, 1e-15)
        assertEquals(-3762.75, w.y, 1e-15)
    }

    @Test
    fun `test cross2`() {
        var vec1 = Vec2(5.03, 1.30)
        var w = Vec2.cross(6.0, vec1)
        assertEquals(-7.8, w.x, 1e-15)
        assertEquals(30.18, w.y, 0.0)

        vec1 = Vec2(-3.75, 9.34)
        w = Vec2.cross(1003.4, vec1)
        assertEquals(-9371.756, w.x, 1e-15)
        assertEquals(-3762.75, w.y, 1e-15)
    }

    @Test
    fun `test is valid`() {
        assertTrue(Vec2(-34234.234234324, -324954.5).isValid)
        assertTrue(Vec2(32454543.0, 543543534.6).isValid)
        assertTrue(Vec2(Double.MAX_VALUE, -324954.5).isValid)
        assertTrue(Vec2(32454543.0, -Double.MAX_VALUE).isValid)
        assertFalse(Vec2(32454543.0, Double.POSITIVE_INFINITY).isValid)
        assertFalse(Vec2(32454543.0, -Double.POSITIVE_INFINITY).isValid)

        assertFalse(Vec2(Double.POSITIVE_INFINITY, 234.534).isValid)
        assertFalse(Vec2(-Double.POSITIVE_INFINITY, 234.534324).isValid)

        // Has to be double inputs not integer or doesn't detect NAN
        assertFalse(Vec2(Double.NaN, 234.534).isValid)
        assertFalse(Vec2(34255234.4, Double.NaN).isValid)
    }

    @Test
    fun `test is zero`() {
        val v = Vec2()

        assertTrue(v.isZero)

        v.set(1.0, 0.0)
        assertFalse(v.isZero)

        v.set(1.0, 1.0)
        assertFalse(v.isZero)

        v.set(0.0, 1.0)
        assertFalse(v.isZero)
    }

    @Test
    fun `test direction constructor`() {
        val v = Vec2(2.0, 3.0)
        v.add(Vec2(-1.2, -5.4))
        assertEquals(0.8, v.x, 0.0)
        assertEquals(-2.4, v.y, 1e-15)
    }
}

