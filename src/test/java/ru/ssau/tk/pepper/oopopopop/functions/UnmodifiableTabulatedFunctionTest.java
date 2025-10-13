package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnmodifiableTabulatedFunctionTest {
    private final double DELTA = 1e-10;

    // y = 2 * x + 1
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};
    private final MathFunction F1 = x -> 2.0 * x + 1;

    @Test
    void getCount() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(X1.length, f1.getCount());
        assertEquals(X1.length, f2.getCount());
    }

    @Test
    void getX() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(X1[0], f1.getX(0), DELTA);
        assertEquals(X1[X1.length - 1], f1.getX(X1.length - 1), DELTA);

        assertEquals(X1[0], f2.getX(0), DELTA);
        assertEquals(X1[X1.length - 1], f2.getX(X1.length - 1), DELTA);
    }

    @Test
    void getY() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(Y1[0], f1.getY(0), DELTA);
        assertEquals(Y1[Y1.length - 1], f1.getY(Y1.length - 1), DELTA);

        assertEquals(Y1[0], f2.getY(0), DELTA);
        assertEquals(Y1[Y1.length - 1], f2.getY(Y1.length - 1), DELTA);
    }

    @Test
    void setY() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertThrows(UnsupportedOperationException.class, () -> f1.setY(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> f2.setY(0, 0));
    }

    @Test
    void indexOfX() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(0, f1.indexOfX(X1[0]));
        assertEquals(1, f1.indexOfX(X1[1]));
        assertEquals(2, f1.indexOfX(X1[2]));
        assertEquals(3, f1.indexOfX(X1[3]));
        assertEquals(4, f1.indexOfX(X1[4]));
        assertEquals(-1, f1.indexOfX((X1[0] + X1[1]) / 2));
        assertEquals(-1, f1.indexOfX(X1[0] - 1));
        assertEquals(-1, f1.indexOfX(X1[X1.length - 1] + 1));

        assertEquals(0, f2.indexOfX(X1[0]));
        assertEquals(1, f2.indexOfX(X1[1]));
        assertEquals(2, f2.indexOfX(X1[2]));
        assertEquals(3, f2.indexOfX(X1[3]));
        assertEquals(4, f2.indexOfX(X1[4]));
        assertEquals(-1, f2.indexOfX((X1[0] + X1[1]) / 2));
        assertEquals(-1, f2.indexOfX(X1[0] - 1));
        assertEquals(-1, f2.indexOfX(X1[X1.length - 1] + 1));
    }

    @Test
    void indexOfY() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(0, f1.indexOfY(Y1[0]));
        assertEquals(1, f1.indexOfY(Y1[1]));
        assertEquals(2, f1.indexOfY(Y1[2]));
        assertEquals(3, f1.indexOfY(Y1[3]));
        assertEquals(4, f1.indexOfY(Y1[4]));
        assertEquals(-1, f1.indexOfY((Y1[0] + Y1[1]) / 2));
        assertEquals(-1, f1.indexOfY(Y1[0] - 1));
        assertEquals(-1, f1.indexOfY(Y1[Y1.length - 1] + 1));

        assertEquals(0, f2.indexOfY(Y1[0]));
        assertEquals(1, f2.indexOfY(Y1[1]));
        assertEquals(2, f2.indexOfY(Y1[2]));
        assertEquals(3, f2.indexOfY(Y1[3]));
        assertEquals(4, f2.indexOfY(Y1[4]));
        assertEquals(-1, f2.indexOfY((Y1[0] + Y1[1]) / 2));
        assertEquals(-1, f2.indexOfY(Y1[0] - 1));
        assertEquals(-1, f2.indexOfY(Y1[Y1.length - 1] + 1));
    }

    @Test
    void leftBound() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(X1[0], f1.leftBound(), DELTA);
        assertEquals(X1[0], f2.leftBound(), DELTA);
    }

    @Test
    void rightBound() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        assertEquals(X1[X1.length - 1], f1.rightBound(), DELTA);
        assertEquals(X1[X1.length - 1], f2.rightBound(), DELTA);
    }

    @Test
    void apply() {
        TabulatedFunction f1 = new UnmodifiableTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new UnmodifiableTabulatedFunction(new LinkedListTabulatedFunction(X1, Y1));

        // case 1: exact match
        assertEquals(Y1[2], f1.apply(X1[2]), DELTA);
        assertEquals(Y1[2], f2.apply(X1[2]), DELTA);

        // case2: interpolation
        double x = (X1[1] + X1[2]) / 2;
        double y = F1.apply(x);
        assertEquals(y, f1.apply(x), DELTA);
        assertEquals(y, f2.apply(x), DELTA);

        // case3: extrapolate left
        x = X1[0] - 1;
        y = F1.apply(x);
        assertEquals(y, f1.apply(x), DELTA);
        assertEquals(y, f2.apply(x), DELTA);

        // case4: extrapolate right
        x = X1[X1.length - 1] + 1;
        y = F1.apply(x);
        assertEquals(y, f1.apply(x), DELTA);
        assertEquals(y, f2.apply(x), DELTA);
    }
}