package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionTest {
    private final double[] X1 = {0.0, 0.1, 0.2};
    private final double[] Y1 = {-0.15, -1.25, 1.75};

    @Test
    void testToString() {
        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        String SA = "ArrayTabulatedFunction size = 3\n[0.0; -0.15]\n[0.1; -1.25]\n[0.2; 1.75]";
        assertEquals(SA, f1.toString());
        String SL = "LinkedListTabulatedFunction size = 3\n[0.0; -0.15]\n[0.1; -1.25]\n[0.2; 1.75]";
        assertEquals(SL, f2.toString());
    }
}