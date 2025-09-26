package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentityFunctionTest {

    @Test
    void apply0() {
        MathFunction f = new IdentityFunction();
        double x = 0;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply1() {
        MathFunction f = new IdentityFunction();
        double x = 1.23456789;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply2() {
        MathFunction f = new IdentityFunction();
        double x = -1.23456789;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply3() {
        MathFunction f = new IdentityFunction();
        double x = Double.MAX_VALUE;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply4() {
        MathFunction f = new IdentityFunction();
        double x = Double.MIN_VALUE;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply5() {
        MathFunction f = new IdentityFunction();
        double x = -Double.MAX_VALUE;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply6() {
        MathFunction f = new IdentityFunction();
        double x = -Double.MIN_VALUE;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply7() {
        MathFunction f = new IdentityFunction();
        double x = Double.NaN;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply8() {
        MathFunction f = new IdentityFunction();
        double x = Double.POSITIVE_INFINITY;
        assertEquals(x, f.apply(x));
    }

    @Test
    void apply9() {
        MathFunction f = new IdentityFunction();
        double x = Double.NEGATIVE_INFINITY;
        assertEquals(x, f.apply(x));
    }
}