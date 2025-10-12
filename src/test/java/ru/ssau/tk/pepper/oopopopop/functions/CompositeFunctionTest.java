package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionTest {
    private final double DELTA = 1e-10;

    @Test
    void andThen1() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new LogFunction();
        CompositeFunction cf = f1.andThen(f2);
        assertEquals(f1, cf.getFirstFunction());
        assertEquals(f2, cf.getSecondFunction());

        double x = 4;
        double y = Math.log(Math.sqrt(x));
        assertEquals(y, cf.apply(x), DELTA);
    }

    @Test
    void andThen2() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f3 = new ConstantFunction(4);
        CompositeFunction cf = f3.andThen(f2).andThen(f1);

        assertEquals(f1, cf.getSecondFunction());

        double x = Double.NaN;
        double y = 0.5;
        assertEquals(y, cf.apply(x), DELTA);
    }

    @Test
    void andThen3() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f3 = new LogFunction();
        CompositeFunction cf = f1.andThen(f2).andThen(f3);

        assertEquals(f3, cf.getSecondFunction());

        double x = 4;
        double y = Math.log(1.0 / Math.sqrt(x));;
        assertEquals(y, cf.apply(x), DELTA);
    }

    @Test
    void apply0() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f1, f2);

        double x = 0.25;
        double y = 2;
        assertEquals(y, f.apply(x), DELTA);
    }

    @Test
    void apply1() {
        MathFunction f1 = new SqrFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f2, f1);

        double x = 0.25;
        double y = 2;
        assertEquals(y, f.apply(x), DELTA);
    }

    @Test
    void apply2() {
        MathFunction f1 = new ReciprocalFunction();
        MathFunction f2 = new ReciprocalFunction();
        MathFunction f = new CompositeFunction(f1, f2);

        double x = 2;
        assertEquals(x, f.apply(x), DELTA);
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
        assertEquals(y, cf2.apply(x), DELTA);
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
        assertEquals(y, cf3.apply(x), DELTA);
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