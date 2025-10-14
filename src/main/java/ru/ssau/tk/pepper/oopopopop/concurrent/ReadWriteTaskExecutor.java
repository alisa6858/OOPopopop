package ru.ssau.tk.pepper.oopopopop.concurrent;

import ru.ssau.tk.pepper.oopopopop.functions.ConstantFunction;
import ru.ssau.tk.pepper.oopopopop.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.MathFunction;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        MathFunction c = new ConstantFunction(-1);
        TabulatedFunction f = new LinkedListTabulatedFunction(c, 1, 1000, 1000);

        Thread readThread = new Thread(new ReadTask(f));
        Thread writeThread = new Thread(new WriteTask(f, 0.5));

        readThread.start();
        writeThread.start();

    }
}
