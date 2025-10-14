package ru.ssau.tk.pepper.oopopopop.concurrent;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;
import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedTabulatedFunctionTest {
    private final double DELTA = 1e-10;

    // y = 2 * x + 1
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    private final MathFunction F1 = x -> 2.0 * x + 1;

    private final double[] X2 = {1};
    private final double[] Y2 = {1};
    private final double[] X3 = {1, 2};
    private final double[] Y3 = {1, 2, 3};

    private final double[] X4 = {2, 1};
    private final double[] Y4 = {1, 2};

    @Test
    void getCount() {
        TabulatedFunction f1 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(X1.length, f1.getCount());
        assertEquals(X1.length, f2.getCount());
    }

    @Test
    void apply() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        // case 1: exact match
        assertEquals(Y1[2], f.apply(X1[2]), DELTA);

        // case2: interpolation
        double x = (X1[1] + X1[2]) / 2;
        double y = F1.apply(x);
        assertEquals(y, f.apply(x), DELTA);

        // case3: extrapolate left
        x = X1[0] - 1;
        y = F1.apply(x);
        assertEquals(y, f.apply(x), DELTA);

        // case4: extrapolate right
        x = X1[X1.length - 1] + 1;
        y = F1.apply(x);
        assertEquals(y, f.apply(x), DELTA);
    }

    @Test
    void getX() {
        TabulatedFunction f1 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(X1[0], f1.getX(0), DELTA);
        assertEquals(X1[X1.length - 1], f1.getX(X1.length - 1), DELTA);

        assertEquals(X1[0], f2.getX(0), DELTA);
        assertEquals(X1[X1.length - 1], f2.getX(X1.length - 1), DELTA);
    }

    @Test
    void getY() {
        TabulatedFunction f1 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        TabulatedFunction f2 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(Y1[0], f1.getY(0), DELTA);
        assertEquals(Y1[Y1.length - 1], f1.getY(Y1.length - 1), DELTA);

        assertEquals(Y1[0], f2.getY(0), DELTA);
        assertEquals(Y1[Y1.length - 1], f2.getY(Y1.length - 1), DELTA);
    }

    @Test
    void setY() {
        TabulatedFunction f1 = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));

        int idx = 2;
        double y = -100;
        f1.setY(idx, y);
        assertEquals(y, f1.getY(idx), DELTA);
    }

    @Test
    void indexOfX() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(0, f.indexOfX(X1[0]));
        assertEquals(1, f.indexOfX(X1[1]));
        assertEquals(2, f.indexOfX(X1[2]));
        assertEquals(3, f.indexOfX(X1[3]));
        assertEquals(4, f.indexOfX(X1[4]));

        assertEquals(-1, f.indexOfX((X1[0] + X1[1]) / 2));
        assertEquals(-1, f.indexOfX(X1[0] - 1));
        assertEquals(-1, f.indexOfX(X1[X1.length - 1] + 1));
    }

    @Test
    void indexOfY() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(0, f.indexOfY(Y1[0]));
        assertEquals(1, f.indexOfY(Y1[1]));
        assertEquals(2, f.indexOfY(Y1[2]));
        assertEquals(3, f.indexOfY(Y1[3]));
        assertEquals(4, f.indexOfY(Y1[4]));

        assertEquals(-1, f.indexOfY((Y1[0] + Y1[1]) / 2));
        assertEquals(-1, f.indexOfY(Y1[0] - 1));
        assertEquals(-1, f.indexOfY(Y1[Y1.length - 1] + 1));
    }

    @Test
    void leftBound() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(X1[0], f.leftBound(), DELTA);
    }

    @Test
    void rightBound() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        assertEquals(X1[X1.length - 1], f.rightBound(), DELTA);
    }

    @Test
    void iterator1() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));
        var it = f.iterator();
        for (int i = 0; i < X1.length; ++i) {
            assertTrue(it.hasNext());
            final int finalI = i;
            assertDoesNotThrow(() -> {
                Point p = it.next();
                assertEquals(X1[finalI], p.x, DELTA);
                assertEquals(Y1[finalI], p.y, DELTA);
            });
        }
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator2() {
        TabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(F1, X1[0], X1[X1.length - 1], X1.length));

        int i = 0;
        for (Point p: f) {
            final int finalI = i;
            assertDoesNotThrow(() -> {
                assertEquals(X1[finalI], p.x, DELTA);
                assertEquals(Y1[finalI], p.y, DELTA);
            });
            ++i;
        }

    }

    @Test
    void doSynchronously1() {
        SynchronizedTabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        f.doSynchronously((SynchronizedTabulatedFunction.Operation<Void>) f1 -> {
            f1.setY(0, f1.getY(0) * 2);
            return null;
        });
        assertEquals(Y1[0] * 2, f.getY(0), DELTA);
    }

    @Test
    void doSynchronously2() {
        SynchronizedTabulatedFunction f = new SynchronizedTabulatedFunction(new ArrayTabulatedFunction(X1, Y1));
        double y = f.doSynchronously(f1 -> {
            f1.setY(0, f1.getY(0) * 2);
            return f1.getY(0);
        });
        assertEquals(Y1[0] * 2, y, DELTA);
    }
}