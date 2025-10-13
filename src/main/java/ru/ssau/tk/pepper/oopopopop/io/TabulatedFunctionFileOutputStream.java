package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.*;

public class TabulatedFunctionFileOutputStream {
    private static final double[] X1 = {0.0, 0.1, 0.2};
    private static final double[] Y1 = {-0.15, -1.25, 1.75};

    public static void main(String[] args) {
        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileOutputStream out1 = new FileOutputStream ("output/array function.bin");
             FileOutputStream out2 = new FileOutputStream ("output/linked list function.bin")) {
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out1), f1);
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out2), f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
