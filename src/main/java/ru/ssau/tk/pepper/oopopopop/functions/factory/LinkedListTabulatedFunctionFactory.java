package ru.ssau.tk.pepper.oopopopop.functions.factory;

import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues) {
        return new LinkedListTabulatedFunction(xValues, yValues);
    }
}
