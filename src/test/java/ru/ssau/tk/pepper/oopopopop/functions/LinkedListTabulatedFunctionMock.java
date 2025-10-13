package ru.ssau.tk.pepper.oopopopop.functions;

public class LinkedListTabulatedFunctionMock extends LinkedListTabulatedFunction {
    public LinkedListTabulatedFunctionMock(double[] xValues, double[] yValues) {
        super(xValues, yValues);
    }

    public LinkedListTabulatedFunctionMock(MathFunction source, double xFrom, double xTo, int count) {
        super(source, xFrom, xTo, count);
    }

    @Override
    public int floorIndexOfX(double x) {
        return super.floorIndexOfX(x);
    }
}
