package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.PrintWriter;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.printf("%d\n", function.getCount());
        for (Point p: function) {
            printWriter.printf("%f %f\n", p.x, p.y);
        }
        printWriter.flush();
    }
}
