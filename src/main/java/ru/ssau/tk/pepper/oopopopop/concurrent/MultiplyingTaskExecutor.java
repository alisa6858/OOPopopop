package ru.ssau.tk.pepper.oopopopop.concurrent;

import ru.ssau.tk.pepper.oopopopop.functions.*;

import java.util.ArrayList;
import java.util.List;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) throws InterruptedException {
        final int N = 10;
        MathFunction u = new UnitFunction();
        TabulatedFunction f = new LinkedListTabulatedFunction(u, 1, 1000, 1000);

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < N; ++i) {
            threadList.add(new Thread(new MultiplyingTask(f)));
        }

        for (Thread t: threadList) {
            t.start();
        }

        // Ожидание главным потоком остальных фиксированное время – не лучшая идея.
        // Описанное в задании решение тоже не является идеальным.
        // Зачем такие сложности, если есть Thread.join().
        for (Thread t: threadList) {
            t.join();
        }

        System.out.println(f);
    }
}
