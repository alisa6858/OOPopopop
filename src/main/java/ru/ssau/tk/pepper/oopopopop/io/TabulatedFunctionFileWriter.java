package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    private static final double[] X1 = {0.0, 0.1, 0.2};
    private static final double[] Y1 = {-0.15, -1.25, 1.75};

    public static void main(String[] args) {
        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileWriter writer = new FileWriter("output/array function.txt")) {
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer), f1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("output/linked list function.txt")) {
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer), f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
