package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    @Test
    void constructor() {
        Point p = new Point(123.456, 456.789);
        assertEquals(123.456, p.x);
        assertEquals(456.789, p.y);
    }
}