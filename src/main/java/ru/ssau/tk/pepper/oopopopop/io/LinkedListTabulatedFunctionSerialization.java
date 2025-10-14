package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.operations.TabulatedDifferentialOperator;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    private static final double[] X1 = {0.0, 0.1, 0.2};
    private static final double[] Y1 = {-0.15, -1.25, 1.75};

    public static void main(String[] args) {
        try (FileOutputStream s = new FileOutputStream("output/serialized linked list functions.bin")) {
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction f = new LinkedListTabulatedFunction(X1, Y1);
            TabulatedFunction df = operator.derive(f);
            TabulatedFunction ddf = operator.derive(df);
            BufferedOutputStream ss = new BufferedOutputStream(s);
            FunctionsIO.serialize(ss, f);
            FunctionsIO.serialize(ss, df);
            FunctionsIO.serialize(ss, ddf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream s = new FileInputStream("output/serialized linked list functions.bin")) {
            BufferedInputStream ss = new BufferedInputStream(s);

            TabulatedFunction f = FunctionsIO.deserialize(ss);
            System.out.println(f);

            TabulatedFunction df = FunctionsIO.deserialize(ss);
            System.out.println(df);

            TabulatedFunction ddf = FunctionsIO.deserialize(ss);
            System.out.println(ddf);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
