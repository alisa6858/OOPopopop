package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.PrintWriter;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.*;

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

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.printf("%d\n", function.getCount());
        for (Point p: function) {
            printWriter.printf("%f %f\n", p.x, p.y);
        }
        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream stream = new DataOutputStream(outputStream);
        stream.writeInt(function.getCount());
        for (Point p: function) {
            stream.writeDouble(p.x);
            stream.writeDouble(p.y);
        }
        stream.flush();
    }
}
