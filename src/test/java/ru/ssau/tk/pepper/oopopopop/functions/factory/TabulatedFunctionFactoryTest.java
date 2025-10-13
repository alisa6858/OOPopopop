package ru.ssau.tk.pepper.oopopopop.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedFunctionFactoryTest {
    // y = 2 * x + 1
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    @Test
    void create1() {
        TabulatedFunction f = new ArrayTabulatedFunctionFactory().create(X1, Y1);
        assertInstanceOf(ArrayTabulatedFunction.class, f);
    }

    @Test
    void create2() {
        TabulatedFunction f = new LinkedListTabulatedFunctionFactory().create(X1, Y1);
        assertInstanceOf(LinkedListTabulatedFunction.class, f);
    }
}