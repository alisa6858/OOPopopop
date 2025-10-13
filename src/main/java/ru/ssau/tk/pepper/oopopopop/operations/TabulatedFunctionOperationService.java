package ru.ssau.tk.pepper.oopopopop.operations;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

public class TabulatedFunctionOperationService {
    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] pts = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point pt: tabulatedFunction) {
            pts[i++] = pt;
        }
        return pts;
    }
}
