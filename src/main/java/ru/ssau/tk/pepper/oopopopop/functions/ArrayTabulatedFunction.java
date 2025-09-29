package ru.ssau.tk.pepper.oopopopop.functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    private double[] xValues;
    private double[] yValues;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException();
        }
        if (xValues.length == 0) {
            throw new IllegalArgumentException();
        }
        if (!isSorted(xValues)) {
            throw new IllegalArgumentException();
        }
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 1) {
            throw new IllegalArgumentException();
        }
        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];
        if (xFrom > xTo) {
            double t = xFrom;
            xFrom = xTo;
            xTo = t;
        }
        if (count == 1) {
            xValues[0] = xFrom;
            yValues[0] = source.apply(xValues[0]);
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; ++i) {
                xValues[i] = xFrom + step * i;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    @Override
    protected int floorIndexOfX(double x) {
        int index = Arrays.binarySearch(xValues, x);
        return index < 0 ? -(index + 1) : index;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (getCount() == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (getCount() == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (getCount() == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        int index = Arrays.binarySearch(xValues, x);
        return index < 0 ? -1 : index;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; ++i) {
            if (yValues[i] == y) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    public void insert(double x, double y) {
        int idx = indexOfX(x);
        if (idx != -1) {
            yValues[idx] = y;
        } else {
            idx = floorIndexOfX(x);
            double[] newXValues = new double[count + 1];
            double[] newYValues = new double[count + 1];

            System.arraycopy(xValues, 0, newXValues, 0, idx);
            System.arraycopy(yValues, 0, newYValues, 0, idx);
            System.arraycopy(xValues, idx, newXValues, idx + 1, count - idx);
            System.arraycopy(yValues, idx, newYValues, idx + 1, count - idx);

            newXValues[idx] = x;
            newYValues[idx] = y;
            xValues = newXValues;
            yValues = newYValues;

            ++count;
        }
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException();
        }
        double[] newXValues = new double[count - 1];
        double[] newYValues = new double[count - 1];

        System.arraycopy(xValues, 0, newXValues, 0, index);
        System.arraycopy(yValues, 0, newYValues, 0, index);
        System.arraycopy(xValues, index + 1, newXValues, index, count - index - 1);
        System.arraycopy(yValues, index + 1, newYValues, index, count - index - 1);

        xValues = newXValues;
        yValues = newYValues;

        --count;
    }
}
