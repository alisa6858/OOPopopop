package ru.ssau.tk.pepper.oopopopop.functions;

public class LogFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.log(x);
    }
}
