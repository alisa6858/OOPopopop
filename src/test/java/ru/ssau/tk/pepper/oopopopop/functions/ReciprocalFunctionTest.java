package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReciprocalFunctionTest {
    @Test
    void apply0() {
        MathFunction f = new ReciprocalFunction();
        double x = 0;
        double y = Double.POSITIVE_INFINITY;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply1() {
        MathFunction f = new ReciprocalFunction();
        double x = 1;
        double y = 1;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply2() {
        MathFunction f = new ReciprocalFunction();
        double x = 0.123456789;
        double y = 1 / x;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply3() {
        MathFunction f = new ReciprocalFunction();
        double x = 1.23456789;
        double y = 1 / x;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply4() {
        MathFunction f = new ReciprocalFunction();
        double x = Double.MAX_VALUE;
        double y = 1 / x;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply5() {
        MathFunction f = new ReciprocalFunction();
        double x = Double.MIN_VALUE;
        double y = 1 / x;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply6() {
        MathFunction f = new ReciprocalFunction();
        double x = Double.NaN;
        double y = Double.NaN;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply7() {
        MathFunction f = new ReciprocalFunction();
        double x = Double.POSITIVE_INFINITY;
        double y = 0;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply8() {
        MathFunction f = new ReciprocalFunction();
        double x = Double.NEGATIVE_INFINITY;
        double y = 0;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply9() {
        MathFunction f = new ReciprocalFunction();
        double x = -1.23456789;
        double y = 1 / x;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }
}