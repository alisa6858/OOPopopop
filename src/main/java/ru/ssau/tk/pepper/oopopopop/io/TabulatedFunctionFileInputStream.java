package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {

        try (FileInputStream in1 = new FileInputStream ("input/function.bin");
             FileInputStream  in2 = new FileInputStream  ("input/function.bin")) {
            ArrayTabulatedFunctionFactory ff1 = new ArrayTabulatedFunctionFactory();
            LinkedListTabulatedFunctionFactory ff2 = new LinkedListTabulatedFunctionFactory();

            TabulatedFunction f1 = FunctionsIO.readTabulatedFunction(new BufferedInputStream(in1), ff1);
            System.out.println(f1);

            TabulatedFunction f2 = FunctionsIO.readTabulatedFunction(new BufferedInputStream(in2), ff2);
            System.out.println(f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
