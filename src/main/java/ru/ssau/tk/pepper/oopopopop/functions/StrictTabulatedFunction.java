package ru.ssau.tk.pepper.oopopopop.functions;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Создать класс StrictTabulatedFunction, реализующий интерфейс TabulatedFunction.
 * Задачей класса является введение запрета на интерполяцию для табулированных функций.
 * Было бы неплохо накладывать такой запрет на уже имеющиеся табулированные функции,
 * чтобы не создавать новые. Но, поскольку у нас имеется уже две различных реализации,
 * наследование здесь не подойдёт. Для динамического подключения дополнительного поведения
 * к объекту без наследования используется паттерн проектирования Декоратор (Обёртка).
 */
public class StrictTabulatedFunction implements TabulatedFunction{
    private final TabulatedFunction function;

    public TabulatedFunction getFunction() {
        return function;
    }

    public StrictTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public int getCount() {
        return function.getCount();
    }

    @Override
    public double getX(int index) {
        return function.getX(index);
    }

    @Override
    public double getY(int index) {
        return function.getY(index);
    }

    @Override
    public void setY(int index, double value) {
        function.setY(index, value);
    }

    @Override
    public int indexOfX(double x) {
        return function.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return function.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return function.leftBound();
    }

    @Override
    public double rightBound() {
        return function.rightBound();
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        return function.iterator();
    }

    @Override
    public double apply(double x) {
        int index = indexOfX(x);
        if (index != -1) {
            return getY(index);
        }
        throw new UnsupportedOperationException();
    }
}
