package ru.ssau.tk.pepper.oopopopop.functions;

public class RungeKuttaMethod implements MathFunction {
    private final MathFunction f;
    private final double x0;
    private final double y0;
    private final double h;

    public RungeKuttaMethod(MathFunction f, double x0, double y0, double h) {
        if (h <= 0) {
            throw new IllegalArgumentException();
        }

        this.f = f;
        this.x0 = x0;
        this.y0 = y0;
        this.h = h;
    }
    
    @Override
    public double apply(double x) {
        if (x < x0) {
            throw new IllegalArgumentException();
        }

        int count = (int) ((x - x0) / h) + 1;
        double[] xTable = new double[count];
        double[] yTable = new double[count];
        xTable[0] = x0;
        yTable[0] = y0;

        for (int i = 1; i < count; ++i) {
            double xn = xTable[i - 1];
            xTable[i] = x0 + h * i;

            double k1 = f.apply(xn);
            double k2 = f.apply(xn + h/2);
            double k3 = f.apply(xn + h/2);
            double k4 = f.apply(xn + h);

            yTable[i] = yTable[i - 1] + h * (k1 + k2 * 2 + k3 * 2 + k4) / 6;
        }

        return yTable[count - 1];
    }
}
