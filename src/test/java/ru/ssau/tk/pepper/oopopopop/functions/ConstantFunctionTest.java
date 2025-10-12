package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantFunctionTest {

    @Test
    void testApplyWithDifferentConstants() {
        ConstantFunction func1 = new ConstantFunction(5.0);
        ConstantFunction func2 = new ConstantFunction(-3.5);
        ConstantFunction func3 = new ConstantFunction(0.0);

        assertEquals(5.0, func1.apply(10.0), 1e-10);
        assertEquals(5.0, func1.apply(-5.0), 1e-10);
        assertEquals(5.0, func1.apply(0.0), 1e-10);

        assertEquals(-3.5, func2.apply(100.0), 1e-10);
        assertEquals(0.0, func3.apply(1.0), 1e-10);
    }

    @Test
    void testGetConstant() {
        ConstantFunction func = new ConstantFunction(7.8);
        assertEquals(7.8, func.getConstant(), 1e-10);
    }
}