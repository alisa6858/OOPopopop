package ru.ssau.tk.pepper.oopopopop.functions;

import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create(double[] xValues, double[] yValues);

    TabulatedFunction createStrict(double[] xValues, double[] yValues);

    TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues);

    TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues);
}
