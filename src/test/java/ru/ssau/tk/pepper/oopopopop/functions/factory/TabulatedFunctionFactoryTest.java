package ru.ssau.tk.pepper.oopopopop.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.*;

import ru.ssau.tk.pepper.oopopopop.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;


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

    @Test
    void createStrict1() {
        TabulatedFunction f = new ArrayTabulatedFunctionFactory().createStrict(X1, Y1);

        assertInstanceOf(StrictTabulatedFunction.class, f);
        assertDoesNotThrow(() -> {
            StrictTabulatedFunction ff = (StrictTabulatedFunction) f;
            assertInstanceOf(ArrayTabulatedFunction.class, ff.getFunction());
        });
    }

    @Test
    void createStrict2() {
        TabulatedFunction f = new LinkedListTabulatedFunctionFactory().createStrict(X1, Y1);
        assertInstanceOf(StrictTabulatedFunction.class, f);
        assertInstanceOf(StrictTabulatedFunction.class, f);
        assertDoesNotThrow(() -> {
            StrictTabulatedFunction ff = (StrictTabulatedFunction) f;
            assertInstanceOf(LinkedListTabulatedFunction.class, ff.getFunction());
        });
    }

    @Test
    void createUnmodifiable1() {
        TabulatedFunction f = new ArrayTabulatedFunctionFactory().createUnmodifiable(X1, Y1);
        assertInstanceOf(UnmodifiableTabulatedFunction.class, f);
        assertDoesNotThrow(() -> {
            UnmodifiableTabulatedFunction ff = (UnmodifiableTabulatedFunction) f;
            assertInstanceOf(ArrayTabulatedFunction.class, ff.getFunction());
        });
    }

    @Test
    void createUnmodifiable2() {
        TabulatedFunction f = new LinkedListTabulatedFunctionFactory().createUnmodifiable(X1, Y1);
        assertInstanceOf(UnmodifiableTabulatedFunction.class, f);
        assertDoesNotThrow(() -> {
            UnmodifiableTabulatedFunction ff = (UnmodifiableTabulatedFunction) f;
            assertInstanceOf(LinkedListTabulatedFunction.class, ff.getFunction());
        });
    }
}