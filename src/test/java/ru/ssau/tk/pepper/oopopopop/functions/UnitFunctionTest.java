package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitFunctionTest {

    @Test
    void testApplyAlwaysReturnsOne() {
        UnitFunction unit = new UnitFunction();

        assertEquals(1.0, unit.apply(0.0), 1e-10);
        assertEquals(1.0, unit.apply(1.0), 1e-10);
        assertEquals(1.0, unit.apply(-1.0), 1e-10);
        assertEquals(1.0, unit.apply(100.0), 1e-10);
        assertEquals(1.0, unit.apply(-50.0), 1e-10);
        assertEquals(1.0, unit.apply(3.14), 1e-10);
    }
}
