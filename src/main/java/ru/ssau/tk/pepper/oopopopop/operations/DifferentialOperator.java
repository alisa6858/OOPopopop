package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;

public interface DifferentialOperator<T extends MathFunction> {
    T derive(T function);
}
