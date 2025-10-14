package ru.ssau.tk.pepper.oopopopop.io;

import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

import java.io.*;

public class ArrayTabulatedFunctionSerializationXml {
    private static final double[] X1 = {0.0, 0.1, 0.2};
    private static final double[] Y1 = {-0.15, -1.25, 1.75};

    public static void main(String[] args) {
        final String filename = "output/xml serialized array functions.xml";

        try (FileWriter writer1 = new FileWriter(filename)) {
            TabulatedFunction f = new ArrayTabulatedFunction(X1, Y1);
            FunctionsIO.serializeXml(new BufferedWriter(writer1), f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileReader in1 = new FileReader(filename)) {
            ArrayTabulatedFunction f = FunctionsIO.deserializeXml(new BufferedReader(in1));
            System.out.println(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
