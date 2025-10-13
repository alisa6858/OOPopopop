package ru.ssau.tk.pepper.oopopopop.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;

import static org.junit.jupiter.api.Assertions.*;

class MiddleSteppingDifferentialOperatorTest {
    private final MathFunction F = Math::log;
    private final MathFunction DF = x -> 1.0 / x;

    private final double STEP = 0.01;

    @Test
    void derive() {
        MiddleSteppingDifferentialOperator op = new MiddleSteppingDifferentialOperator(STEP);
        MathFunction df = op.derive(F);

        double x = 1;
        double dy = df.apply(x);
        assertEquals(DF.apply(x), dy, STEP);

        x = 2;
        dy = df.apply(x);
        assertEquals(DF.apply(x), dy, STEP);

        x = 3;
        dy = df.apply(x);
        assertEquals(DF.apply(x), dy, STEP);
    }
}