package ru.ssau.tk.pepper.oopopopop.functions.factory;

import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.StrictTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.UnmodifiableTabulatedFunction;

public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }

    @Override
    public TabulatedFunction createStrict(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(create(xValues, yValues));
    }

    @Override
    public TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues) {
        return new UnmodifiableTabulatedFunction(create(xValues, yValues));
    }

    @Override
    public TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues) {
        return new StrictTabulatedFunction(createUnmodifiable(xValues, yValues));
    }
}
