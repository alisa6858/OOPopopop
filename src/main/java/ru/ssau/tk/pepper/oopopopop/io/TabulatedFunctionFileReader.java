package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TabulatedFunctionFileReader {
    public static void main(String[] args) {

        try (FileReader in1 = new FileReader("input/function.txt");
             FileReader in2 = new FileReader ("input/function.txt")) {
            ArrayTabulatedFunctionFactory ff1 = new ArrayTabulatedFunctionFactory();
            LinkedListTabulatedFunctionFactory ff2 = new LinkedListTabulatedFunctionFactory();

            TabulatedFunction f1 = FunctionsIO.readTabulatedFunction(new BufferedReader(in1), ff1);
            System.out.println(f1);

            TabulatedFunction f2 = FunctionsIO.readTabulatedFunction(new BufferedReader(in2), ff2);
            System.out.println(f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
