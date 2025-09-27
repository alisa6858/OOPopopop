package ru.ssau.tk.pepper.oopopopop.functions;

public class NytonFunction implements MathFunction {
    private MathFunction function;
    private MathFunction derivative;    // Производная
    private double tolerance;           // Допустимая погрешность
    private int maxIterations;          // Максимальное число итераций

    public NytonFunction(MathFunction function, MathFunction derivative,
                          double tolerance, int maxIterations) {
        this.function = function;
        this.derivative = derivative;
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    @Override
    public CompositeFunction andThen(MathFunction afterFunction) {
        return MathFunction.super.andThen(afterFunction);
    }

    @Override
    public double apply(double startPosition) {
        double current = startPosition;

        for (int i = 0; i < maxIterations; i++) {
            double fx = function.apply(current);
            double fpx = derivative.apply(current);

            // Проверка на нулевую производную с более точным условием
            if (Math.abs(fpx) < 1e-10) {
                // Если производная почти нулевая, пробуем немного сместить точку
                if (Math.abs(fx) < tolerance) {
                    return current; // Мы уже в корне
                }
            }

            double next = current - fx / fpx;

            if (Math.abs(next - current) < tolerance) {
                return next; // Возвращаем найденный корень
            }
            current = next;
        }
        return 0;
    }
}