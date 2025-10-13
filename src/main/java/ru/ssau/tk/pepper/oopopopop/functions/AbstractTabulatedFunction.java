package ru.ssau.tk.pepper.oopopopop.functions;

import ru.ssau.tk.pepper.oopopopop.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.pepper.oopopopop.exceptions.DifferentLengthOfArraysException;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractTabulatedFunction implements TabulatedFunction, Serializable {
    // Так как здесь хранится count, то для успешной сериализации/десериализации
    // бвзовый класс также должен быть Serializable
    @Serial
    private static final long serialVersionUID = -6113226241446244071L;

    protected int count = 0;

    protected abstract int floorIndexOfX(double x);

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) / (rightX - leftX) * (x - leftX);
    }

    @Override
    public double apply(double x) {
        // Хотя конструкторы не позволяют создать
        // объекты с count == 0, но с помощью
        // remove() это можно будет сделать,
        // и в этом случае объект нельзя будет использовать
        // по назначению.
        if (count == 0) {
            throw new IllegalStateException("Table is empty.");
        }

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

    static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLengthOfArraysException();
        }
    }

    static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; ++i) {
            if (xValues[i - 1] > xValues[i]) {
                throw new ArrayIsNotSortedException();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(" size = ").append(count).append("\n");
        var it = iterator();
        if (it.hasNext()) {
            Point p = it.next();
            sb.append("[").append(p.x).append("; ").append(p.y).append("]");
            while (it.hasNext()) {
                p = it.next();
                sb.append("\n[").append(p.x).append("; ").append(p.y).append("]");
            }
        }
        return sb.toString();
    }
}
