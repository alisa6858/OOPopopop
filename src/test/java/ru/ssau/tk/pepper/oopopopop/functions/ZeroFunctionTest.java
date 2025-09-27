package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZeroFunctionTest {

    @Test
    void testApplyAlwaysReturnsZero() {
        ZeroFunction zero = new ZeroFunction();

        assertEquals(0.0, zero.apply(0.0), 1e-10);
        assertEquals(0.0, zero.apply(1.0), 1e-10);
        assertEquals(0.0, zero.apply(-1.0), 1e-10);
        assertEquals(0.0, zero.apply(100.0), 1e-10);
        assertEquals(0.0, zero.apply(-50.0), 1e-10);
        assertEquals(0.0, zero.apply(3.14), 1e-10);
    }
}