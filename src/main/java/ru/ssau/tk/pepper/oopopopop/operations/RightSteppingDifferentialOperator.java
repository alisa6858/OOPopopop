package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;

public class RightSteppingDifferentialOperator extends SteppingDifferentialOperator {
    protected RightSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return x -> (function.apply(x + step) - function.apply(x)) / step;
    }
}
