package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {
    private TabulatedFunctionFactory factory;

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedDifferentialOperator() {
        this(new ArrayTabulatedFunctionFactory());
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        Point[] pts = TabulatedFunctionOperationService.asPoints(function);
        double[] xValues = new double[pts.length];
        double[] yValues = new double[pts.length];
        for (int i = 0; i < pts.length; ++i) {
            xValues[i] = pts[i].x;
        }
        for (int k = 0; k < pts.length - 1; ++k) {
            yValues[k] = (pts[k + 1].y - pts[k].y) / (pts[k + 1].x - pts[k].x);
        }
        yValues[pts.length - 1] = yValues[pts.length - 2];
        return factory.create(xValues, yValues);
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
}
