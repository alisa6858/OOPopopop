package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {
    TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] pts = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point pt: tabulatedFunction) {
            pts[i++] = pt;
        }
        return pts;
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, Double::sum);
    }

    public TabulatedFunction sub(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }

    public TabulatedFunction mul(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u * v);
    }

    public TabulatedFunction div(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u / v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException();
        }

        Point[] pA = asPoints(a);
        Point[] pB = asPoints(b);

        double[] xValues = new double[pA.length];
        double[] yValues = new double[pA.length];

        for (int i = 0; i < pA.length; ++i) {
            if (pA[i].x != pB[i].x) {
                throw new InconsistentFunctionsException();
            }
            xValues[i] = pA[i].x;
            yValues[i] = operation.apply(pA[i].y, pB[i].y);
        }

        return factory.create(xValues, yValues);
    }

    private interface BiOperation {
        double apply(double u, double v);
    }
}
