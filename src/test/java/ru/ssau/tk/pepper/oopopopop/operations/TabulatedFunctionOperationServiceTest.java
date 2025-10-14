package ru.ssau.tk.pepper.oopopopop.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.pepper.oopopopop.functions.*;

import ru.ssau.tk.pepper.oopopopop.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;


import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionOperationServiceTest {
    private final double DELTA = 1e-10;

    // y = 2 * x + 1
    private final MathFunction F1 = x -> 2.0 * x + 1;
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    // y = 3 * x - 1
    private final MathFunction F2 = x -> 3.0 * x - 1;
    private final double[] X2 = {1, 2, 3, 4, 5};
    private final double[] Y2 = {
            F2.apply(X2[0]),
            F2.apply(X2[1]),
            F2.apply(X2[2]),
            F2.apply(X2[3]),
            F2.apply(X2[4]),
    };

    private final double[] X3 = {1, 2, 3, 4};
    private final double[] Y3 = {
            F2.apply(X3[0]),
            F2.apply(X3[1]),
            F2.apply(X3[2]),
            F2.apply(X3[3]),
    };

    private final double[] X4 = {0, 1, 2, 3, 4};
    private final double[] Y4 = {
            F2.apply(X4[0]),
            F2.apply(X4[1]),
            F2.apply(X4[2]),
            F2.apply(X4[3]),
            F2.apply(X4[4]),
    };

    @Test
    void asPoints() {
        ArrayTabulatedFunction f = new ArrayTabulatedFunction(X1, Y1);

        Point[] pts = TabulatedFunctionOperationService.asPoints(f);

        assertEquals(f.getCount(), pts.length);
        for (int i = 0; i < pts.length; ++i) {
            assertEquals(X1[i], pts[i].x, DELTA);
            assertEquals(Y1[i], pts[i].y, DELTA);
        }
    }



    @Test
    void setFactory() {
        TabulatedFunctionFactory f1 = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService s1 = new TabulatedFunctionOperationService();
        s1.setFactory(f1);
        assertEquals(f1, s1.getFactory());
    }

    @Test
    void add() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction b = new LinkedListTabulatedFunction(X2, Y2);
        TabulatedFunction c = s.add(a, b);

        assertInstanceOf(ArrayTabulatedFunction.class, c);
        assertEquals(a.getCount(), c.getCount());
        Point[] p = TabulatedFunctionOperationService.asPoints(c);
        for (int i = 0; i < X1.length; ++i) {
            assertEquals(X1[i], p[i].x, DELTA);
            assertEquals(Y1[i] + Y2[i], p[i].y, DELTA);
        }
    }

    @Test
    void sub() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction a = new LinkedListTabulatedFunction(X1, Y1);
        TabulatedFunction b = new ArrayTabulatedFunction(X2, Y2);
        TabulatedFunction c = s.sub(a, b);

        assertInstanceOf(LinkedListTabulatedFunction.class, c);
        assertEquals(a.getCount(), c.getCount());
        Point[] p = TabulatedFunctionOperationService.asPoints(c);
        for (int i = 0; i < X1.length; ++i) {
            assertEquals(X1[i], p[i].x, DELTA);
            assertEquals(Y1[i] - Y2[i], p[i].y, DELTA);
        }
    }

    @Test
    void inconsistent1() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction b = new LinkedListTabulatedFunction(X3, Y3);
        assertThrows(InconsistentFunctionsException.class, () -> s.add(a, b));
    }

    @Test
    void inconsistent2() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction b = new LinkedListTabulatedFunction(X4, Y4);
        assertThrows(InconsistentFunctionsException.class, () -> s.add(a, b));
    }

    @Test
    void mul() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService();
        TabulatedFunction a = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction b = new LinkedListTabulatedFunction(X2, Y2);
        TabulatedFunction c = s.mul(a, b);

        assertInstanceOf(ArrayTabulatedFunction.class, c);
        assertEquals(a.getCount(), c.getCount());
        Point[] p = TabulatedFunctionOperationService.asPoints(c);
        for (int i = 0; i < X1.length; ++i) {
            assertEquals(X1[i], p[i].x, DELTA);
            assertEquals(Y1[i] * Y2[i], p[i].y, DELTA);
        }
    }

    @Test
    void div() {
        TabulatedFunctionOperationService s = new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction a = new LinkedListTabulatedFunction(X1, Y1);
        TabulatedFunction b = new ArrayTabulatedFunction(X2, Y2);
        TabulatedFunction c = s.div(a, b);

        assertInstanceOf(LinkedListTabulatedFunction.class, c);
        assertEquals(a.getCount(), c.getCount());
        Point[] p = TabulatedFunctionOperationService.asPoints(c);
        for (int i = 0; i < X1.length; ++i) {
            assertEquals(X1[i], p[i].x, DELTA);
            assertEquals(Y1[i] / Y2[i], p[i].y, DELTA);
        }
    }
}