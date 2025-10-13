package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    protected double step;

    protected SteppingDifferentialOperator(double step) {
        setStep(step);
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        if (step <= 0 || Double.isNaN(step) || step == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException();
        }
        this.step = step;
    }
}
