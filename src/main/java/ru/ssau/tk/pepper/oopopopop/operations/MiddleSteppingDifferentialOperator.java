package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;

public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {
    protected MiddleSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return x -> (function.apply(x + step) - function.apply(x - step)) / (2.0 * step);
    }
}
