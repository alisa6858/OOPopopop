package ru.ssau.tk.pepper.oopopopop.functions;

import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.StrictTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;

public class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new ArrayTabulatedFunction(xValues, yValues);
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
