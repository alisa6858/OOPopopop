package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionTest {

    @Test
    void apply0() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f1, f2);

        double x = 0.25;
        double y = 2;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply1() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f2, f1);

        double x = 0.25;
        double y = 2;
        double delta = 1e-10;
        assertEquals(y, f.apply(x), delta);
    }

    @Test
    void apply2() {
        MathFunction f1 = new ReciprocalFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f1, f2);

        double x = 2;
        double delta = 1e-10;
        assertEquals(x, f.apply(x), delta);
    }

    @Test
    void apply3() {
        MathFunction f1 = new ReciprocalFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f3 = new ReciprocalFunction();
        MathFunction cf1 = new CompositeFunction(f1, f2); // 1 / (1 / x) = x
        MathFunction cf2 = new CompositeFunction(cf1, f3); // 1 / (1 / (1 / x))) = 1 / x

        double x = 2;
        double y = 0.5;
        double delta = 1e-10;
        assertEquals(y, cf2.apply(x), delta);
    }

    @Test
    void apply4() {
        MathFunction f1 = new ReciprocalFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f3 = new ReciprocalFunction();
        MathFunction cf1 = new CompositeFunction(f1, f2); // 1 / (1 / x) = x
        MathFunction cf2 = new CompositeFunction(cf1, f3); // 1 / (1 / (1 / x))) = 1 / x
        MathFunction cf3 = new CompositeFunction(cf1, cf2); // 1 / (1 / (1 / (1 / (1 / x))))) = 1 / x

        double x = 2;
        double y = 0.5;
        double delta = 1e-10;
        assertEquals(y, cf3.apply(x), delta);
    }

    @Test
    void getFirstFunction() {
        MathFunction f1 = new IdentityFunction();
        MathFunction f2 = new ReciprocalFunction();
        CompositeFunction f = new CompositeFunction(f1, f2);
        assertEquals(f1, f.getFirstFunction());
    }

    @Test
    void getSecondFunction() {
        MathFunction f1 = new IdentityFunction();
        MathFunction f2 = new ReciprocalFunction();
        CompositeFunction f = new CompositeFunction(f1, f2);
        assertEquals(f2, f.getSecondFunction());
    }
}