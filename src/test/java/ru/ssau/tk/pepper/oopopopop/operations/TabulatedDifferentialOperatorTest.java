package ru.ssau.tk.pepper.oopopopop.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.*;
import ru.ssau.tk.pepper.oopopopop.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedDifferentialOperatorTest {
    private final double DELTA = 1e-10;

    // y = 3 * x * x + 2 * x + 1
    //private final MathFunction F1 = x -> 3 * x * x + 2 * x + 1;
    private final MathFunction F1 = x -> 2 * x + 1;

    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {
            F1.apply(X1[0]),
            F1.apply(X1[1]),
            F1.apply(X1[2]),
            F1.apply(X1[3]),
            F1.apply(X1[4])};

    // y = 3 * x * x + 2 * x + 1
    //private final MathFunction DF1 = x -> 6 * x + 2;
    private final MathFunction DF1 = x -> 2;

    private final double[] DY1 = {
            DF1.apply(X1[0]),
            DF1.apply(X1[1]),
            DF1.apply(X1[2]),
            DF1.apply(X1[3]),
            DF1.apply(X1[4])};

    @Test
    void derive1() {
        /// Дает хороший результат только на линейных функциях.
        /// Очевидно, что для нелинейных точность результата будет
        /// тем выше, чем меньше шаг дифференцирования,
        /// т.е., в данном случае, чем меньше расстояния между точками X.

        TabulatedFunction f = new LinkedListTabulatedFunction(X1, Y1);
        TabulatedDifferentialOperator op = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
        TabulatedFunction df = op.derive(f);

        var it = df.iterator();
        for (int i = 0; i < f.getCount() - 1; ++i) {
            Point p = it.next();
            assertEquals(DY1[i], p.y, DELTA);
        }
    }

    @Test
    void derive2() {
        TabulatedFunction f = new ArrayTabulatedFunction(X1, Y1);
        TabulatedDifferentialOperator op = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction df = op.derive(f);

        var it = df.iterator();
        for (int i = 0; i < f.getCount() - 1; ++i) {
            Point p = it.next();
            assertEquals(DY1[i], p.y, DELTA);
        }
    }

    @Test
    void getFactory() {
        TabulatedFunctionFactory f1 = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator op1 = new TabulatedDifferentialOperator(f1);
        assertEquals(f1, op1.getFactory());

        TabulatedFunctionFactory f2 = new ArrayTabulatedFunctionFactory();
        TabulatedDifferentialOperator op2 = new TabulatedDifferentialOperator(f2);
        assertEquals(f2, op2.getFactory());
    }

    @Test
    void setFactory() {
        TabulatedFunctionFactory f1 = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator op1 = new TabulatedDifferentialOperator();
        op1.setFactory(f1);
        assertEquals(f1, op1.getFactory());
    }

    @Test
    void deriveSynchronously() {
        TabulatedFunction f = new LinkedListTabulatedFunction(X1, Y1);
        TabulatedDifferentialOperator op = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
        TabulatedFunction df = op.deriveSynchronously(f);

        var it = df.iterator();
        for (int i = 0; i < f.getCount() - 1; ++i) {
            Point p = it.next();
            assertEquals(DY1[i], p.y, DELTA);
        }
    }
}