package de.chaffic.math

import org.junit.Test
import kotlin.test.*

class MathTest {

    @Test
    fun `test line intersect`() {
        assertNull(Math.lineIntersect(
            Vec2(174.23332548150782, 441.2614632173741), 
            Vec2(130.89315298214362, 448.1258721822548),
            Vec2(0.0, 0.0), 
            Vec2(-0.5999999640000007, -999.9998200000055)
        ))
    }

    @Test
    fun `test point is on line`() {
        assertFalse(Math.pointIsOnLine(Vec2(0.0, 0.0), Vec2(1.0, 1.0), Vec2(0.0, 1.0)))
        assertTrue(Math.pointIsOnLine(Vec2(920.0, 480.0), Vec2(-880.0, 480.0), Vec2(0.288, 480.0)))
        assertFalse(Math.pointIsOnLine(Vec2(0.0, 0.00), Vec2(-0.59, -999.999), Vec2(0.288, 480.0)))
    }
}