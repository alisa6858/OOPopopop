package ru.ssau.tk.pepper.oopopopop.concurrent;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class IntegralTest {
    private final double DELTA = 1e-10;

    // f(x) = 2 * x + 1
    // F(x) = x * x + x
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    @Test
    void calculate() throws ExecutionException, InterruptedException {
        // Интеграл этой функции на заданном интервале будет равен
        // (5 * 5 + 5) - (1 * 1 + 1) = 30 - 2 = 28
        TabulatedFunction f = new ArrayTabulatedFunction(X1, Y1);
        Integral integral = new Integral(f, 1);
        assertEquals(28, integral.calculate(), DELTA);
    }
}