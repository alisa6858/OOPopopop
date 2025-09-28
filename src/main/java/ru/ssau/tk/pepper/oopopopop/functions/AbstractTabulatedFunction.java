package ru.ssau.tk.pepper.oopopopop.functions;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {
    protected int count = 0;

    protected abstract int floorIndexOfX(double x);

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) / (rightX - leftX) * (x - leftX);
    }

    protected boolean isSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; ++i) {
            if (xValues[i - 1] > xValues[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        }

        if (x > rightBound()) {
            return extrapolateRight(x);
        }

        int index = indexOfX(x);
        if (index == -1) {
            index = floorIndexOfX(x);
            return interpolate(x, index);
        }

        return getY(index);
    }

    @Override
    public int getCount() {
        return count;
    }
}
