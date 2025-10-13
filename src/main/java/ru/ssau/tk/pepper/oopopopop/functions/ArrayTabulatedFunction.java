package ru.ssau.tk.pepper.oopopopop.functions;

import ru.ssau.tk.pepper.oopopopop.exceptions.InterpolationException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    @Serial
    private static final long serialVersionUID = 4057149733720959757L;

    private double[] xValues;
    private double[] yValues;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException();
        }

        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
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
        double step = (xTo - xFrom) / (count - 1);
        for (int i = 0; i < count; ++i) {
            xValues[i] = xFrom + step * i;
            yValues[i] = source.apply(xValues[i]);
        }
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (count == 0) {
            throw new IllegalStateException();
        }
        if (x < leftBound()) {
            throw new IllegalArgumentException();
        }
        for (int i = count - 1; i >= 0; --i) {
            if (x >= xValues[i]) {
                return i;
            }
        }
        throw new IllegalStateException(); // unreachable
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count < 2) {
            throw new IllegalStateException();
        }
        if (x < xValues[floorIndex] || x > xValues[floorIndex + 1]) {
            throw new InterpolationException();
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
        }
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= getCount()) {
            throw new IllegalArgumentException();
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
        if (count == 0) {
            throw new IllegalStateException();
        }
        return xValues[0];
    }

    @Override
    public double rightBound() {
        if (count == 0) {
            throw new IllegalStateException();
        }
        return xValues[count - 1];
    }

    @Override
    public void insert(double x, double y) {
        int idx = indexOfX(x);
        if (idx != -1) {
            yValues[idx] = y;
        } else {
            idx = 0;
            try {
                idx = 1 + floorIndexOfX(x);
            } catch (IllegalArgumentException ignored) {
            }
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

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < count;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Point p = new Point(xValues[i], yValues[i]);
                ++i;
                return p;
            }
        };
    }
}
