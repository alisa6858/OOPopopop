package ru.ssau.tk.pepper.oopopopop.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.pepper.oopopopop.functions.*;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsIOTest {
    static final String TEMP_DIR = "temp";

    private static final double[] X1 = {0.0, 0.1, 0.2};
    private static final double[] Y1 = {-0.15, -1.25, 1.75};

    // Очищаем директорию после каждого теста
    @AfterEach
    void clearTempDir() {
        // Удаление всех файлов в директории
        // https://stackoverflow.com/questions/13195797/delete-all-files-in-directory-but-not-directory-one-liner-solution
        Arrays.stream(new File(TEMP_DIR).listFiles()).forEach(File::delete);
    }

    @Test
    void writeTabulatedFunction1() throws IOException {
        final String filename1 = TEMP_DIR + "/f1.txt";
        final String filename2 = TEMP_DIR + "/f2.txt";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileWriter writer1 = new FileWriter(filename1);
             FileWriter writer2 = new FileWriter(filename2)) {
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer1), f1);
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());
    }

    @Test
    void writeTabulatedFunction2() throws IOException {
        final String filename1 = TEMP_DIR + "/f1.bin";
        final String filename2 = TEMP_DIR + "/f2.bin";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileOutputStream out1 = new FileOutputStream (filename1);
             FileOutputStream out2 = new FileOutputStream (filename2)) {
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out1), f1);
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());
    }

    @Test
    void readTabulatedFunction1() throws IOException {
        final String filename1 = TEMP_DIR + "/f1.txt";
        final String filename2 = TEMP_DIR + "/f2.txt";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileWriter writer1 = new FileWriter(filename1);
             FileWriter writer2 = new FileWriter(filename2)) {
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer1), f1);
            FunctionsIO.writeTabulatedFunction(new BufferedWriter(writer2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());

        try (FileReader in1 = new FileReader(filename1);
             FileReader in2 = new FileReader (filename2)) {
            ArrayTabulatedFunctionFactory ff1 = new ArrayTabulatedFunctionFactory();
            LinkedListTabulatedFunctionFactory ff2 = new LinkedListTabulatedFunctionFactory();

            TabulatedFunction f3 = FunctionsIO.readTabulatedFunction(new BufferedReader(in1), ff1);
            assertEquals(f1.toString(), f3.toString());

            TabulatedFunction f4 = FunctionsIO.readTabulatedFunction(new BufferedReader(in2), ff2);
            assertEquals(f2.toString(), f4.toString());
        }
    }

    @Test
    void readTabulatedFunction2() throws IOException {
        final String filename1 = TEMP_DIR + "/f1.bin";
        final String filename2 = TEMP_DIR + "/f2.bin";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileOutputStream out1 = new FileOutputStream (filename1);
             FileOutputStream out2 = new FileOutputStream (filename2)) {
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out1), f1);
            FunctionsIO.writeTabulatedFunction(new BufferedOutputStream(out2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());

        try (FileInputStream in1 = new FileInputStream (filename1);
             FileInputStream  in2 = new FileInputStream  (filename2)) {
            ArrayTabulatedFunctionFactory ff1 = new ArrayTabulatedFunctionFactory();
            LinkedListTabulatedFunctionFactory ff2 = new LinkedListTabulatedFunctionFactory();

            TabulatedFunction f3 = FunctionsIO.readTabulatedFunction(new BufferedInputStream(in1), ff1);
            assertEquals(f1.toString(), f3.toString());

            TabulatedFunction f4 = FunctionsIO.readTabulatedFunction(new BufferedInputStream(in2), ff2);
            assertEquals(f2.toString(), f4.toString());
        }
    }

    @Test
    void serialize() throws IOException {
        final String filename1 = TEMP_DIR + "/f1.bin";
        final String filename2 = TEMP_DIR + "/f2.bin";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileOutputStream out1 = new FileOutputStream (filename1);
             FileOutputStream out2 = new FileOutputStream (filename2)) {
            FunctionsIO.serialize(new BufferedOutputStream(out1), f1);
            FunctionsIO.serialize(new BufferedOutputStream(out2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());
    }

    @Test
    void deserialize() throws IOException, ClassNotFoundException {
        final String filename1 = TEMP_DIR + "/f1.bin";
        final String filename2 = TEMP_DIR + "/f2.bin";

        TabulatedFunction f1 = new ArrayTabulatedFunction(X1, Y1);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(X1, Y1);

        try (FileOutputStream out1 = new FileOutputStream (filename1);
             FileOutputStream out2 = new FileOutputStream (filename2)) {
            FunctionsIO.serialize(new BufferedOutputStream(out1), f1);
            FunctionsIO.serialize(new BufferedOutputStream(out2), f2);
        }

        assertTrue(new File(filename1).exists());
        assertTrue(new File(filename2).exists());

        try (FileInputStream in1 = new FileInputStream (filename1);
             FileInputStream  in2 = new FileInputStream  (filename2)) {

            TabulatedFunction f3 = FunctionsIO.deserialize(new BufferedInputStream(in1));
            assertEquals(f1.toString(), f3.toString());

            TabulatedFunction f4 = FunctionsIO.deserialize(new BufferedInputStream(in2));
            assertEquals(f2.toString(), f4.toString());

        }
    }
}