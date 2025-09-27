package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompositeFunctionTest {
    @Test
    public void testApply() {
        MathFunction sqr = new SqrFunction();
        MathFunction constant = new ConstantFunction(2);
        CompositeFunction composite = new CompositeFunction(sqr, constant);

        assertEquals(2, composite.apply(5), 1e-10);
        assertEquals(2, composite.apply(-3), 1e-10);
    }

    @Test
    public void testAndThen() {
        MathFunction sqr = new SqrFunction();
        MathFunction constant = new ConstantFunction(3);

        MathFunction composite = sqr.andThen(constant);
        assertEquals(3, composite.apply(10), 1e-10);
        assertEquals(3, composite.apply(-4), 1e-10);
    }
}