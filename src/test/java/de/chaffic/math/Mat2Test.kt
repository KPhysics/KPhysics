package de.chaffic.math

import org.junit.Test
import kotlin.test.*

class Mat2Test {
    
    @Test
    fun `test set using radians`() {
        val m = Mat2()
        m.set(1.0)
        assertEquals(0.5403023058681398, m.row1.x, 0.0)
        assertEquals(0.8414709848078965, m.row2.x, 0.0)
        assertEquals(-0.8414709848078965, m.row1.y, 0.0)
        assertEquals(0.5403023058681398, m.row2.y, 0.0)
    }

    @Test
    fun `test set using matrix`() {
        val m = Mat2()
        m.set(1.0)
        val u = Mat2()
        u.set(m)
        assertEquals(m.row1.x, u.row1.x, 0.0)
        assertEquals(m.row2.x, u.row2.x, 0.0)
        assertEquals(m.row1.y, u.row1.y, 0.0)
        assertEquals(m.row2.y, u.row2.y, 0.0)
    }

    @Test
    fun `test transpose`() {
        val m = Mat2()
        m.set(1.0)
        val u = m.transpose()
        assertEquals(m.row1.x, u.row1.x, 0.0)
        assertEquals(m.row1.y, u.row2.x, 0.0)
        assertEquals(m.row2.x, u.row1.y, 0.0)
        assertEquals(m.row2.y, u.row2.y, 0.0)
    }

    @Test
    fun `test mul with single vector`() {
        val m = Mat2()
        m.set(1.0)
        val v = Vec2(1.0, 0.0)
        m.mul(v)
        assertEquals(0.5403023058681398, v.x, 0.0)
        assertEquals(0.8414709848078965, v.y, 0.0)
    }

    @Test
    fun `test mul with output vector`() {
        val m = Mat2()
        m.set(1.0)
        val v = Vec2(1.0, 0.0)
        val q = Vec2(10.0, 0.0)
        m.mul(v, q)
        assertEquals(0.5403023058681398, q.x, 0.0)
        assertEquals(0.8414709848078965, q.y, 0.0)
        assertEquals(1.0, v.x, 0.0)
        assertEquals(0.0, v.y, 0.0)
    }
}
