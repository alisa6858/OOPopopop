package ru.ssau.tk.pepper.oopopopop.functions;

public class ArrayTabulatedFunctionMock extends ArrayTabulatedFunction {
    public ArrayTabulatedFunctionMock(double[] xValues, double[] yValues) {
        super(xValues, yValues);
    }

    public ArrayTabulatedFunctionMock(MathFunction source, double xFrom, double xTo, int count) {
        super(source, xFrom, xTo, count);
    }

    @Override
    public int floorIndexOfX(double x) {
        return super.floorIndexOfX(x);
    }
}
