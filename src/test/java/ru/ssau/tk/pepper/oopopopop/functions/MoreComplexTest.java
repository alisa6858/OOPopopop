package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoreComplexTest {
    private final double DELTA = 1e-10;

    // y = 2 * x + 1
    private final MathFunction F1 = x -> 2.0 * x + 1;
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    // y = 3 * x - 1
    private final MathFunction F2 = x -> 3.0 * x - 1;
    private final double[] X2 = {1, 2, 3, 4};
    private final double[] Y2 = {F2.apply(X2[0]), F2.apply(X2[1]), F2.apply(X2[2]), F2.apply(X2[3])};

    @Test
    void test1() {
        MathFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        MathFunction f2 = new LinkedListTabulatedFunction(F2, X2[0], X2[X2.length - 1], X2.length);
        MathFunction cf = f1.andThen(f2);

        // Так как мы задали 2 линейные табличные функции,
        // для которых у нас есть и нетабличные варианты,
        // то для проверки результата можно воспользоваться
        // нетабличными вариантами.
        // Для нелинейных функций это не сработает,
        // так как результат линейной апроксимации
        // не будет совпадать с фактическим значением.
        double x = 0;
        double y = F2.apply(F1.apply(x));
        assertEquals(y, cf.apply(x), DELTA);

        x = 10;
        y = F2.apply(F1.apply(x));
        assertEquals(y, cf.apply(x), DELTA);
    }

    @Test
    void test2() {
        MathFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        MathFunction f2 = new LinkedListTabulatedFunction(F2, X2[0], X2[X2.length - 1], X2.length);
        MathFunction f3 = x -> 2.0 / x + x * x;
        MathFunction cf = f1.andThen(f2).andThen(f3);


        double x = 0;
        double y = f3.apply(F2.apply(F1.apply(x)));
        assertEquals(y, cf.apply(x), DELTA);

        x = 10;
        y = f3.apply(F2.apply(F1.apply(x)));
        assertEquals(y, cf.apply(x), DELTA);
    }
}
