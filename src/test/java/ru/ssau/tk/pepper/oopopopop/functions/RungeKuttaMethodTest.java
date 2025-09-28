package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RungeKuttaMethodTest {

    @Test
    void apply1() {
        // y = 4x
        // y' = 4
        MathFunction f = new ConstantFunction(4);
        double x0 = 0;
        double y0 = 0;
        double h = 0.1;

        MathFunction rk = new RungeKuttaMethod(f, x0, y0, h);

        assertEquals(4.0, rk.apply(1), 1e-5);
        assertEquals(8.0, rk.apply(2), 1e-5);
    }

    @Test
    void apply2() {
        // y = log(x)
        // y' = 1 / x
        MathFunction f = new ReciprocalFunction();
        double x0 = 1;
        double y0 = 0;
        double h = 0.1;

        MathFunction rk = new RungeKuttaMethod(f, x0, y0, h);

        assertEquals(Math.log(2), rk.apply(2), 1e-5);
        assertEquals(Math.log(3), rk.apply(3), 1e-5);
    }
}