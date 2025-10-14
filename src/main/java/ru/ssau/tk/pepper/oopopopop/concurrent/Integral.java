package ru.ssau.tk.pepper.oopopopop.concurrent;

import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.operations.TabulatedFunctionOperationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Integral {
    private final TabulatedFunction function;

    private final int nPoolSize;


    public Integral(TabulatedFunction function, int nPoolSize) {
        this.function = function;
        this.nPoolSize = nPoolSize;
    }

    // Вычисление интеграла методом трапеций
    public double calculate() throws InterruptedException, ExecutionException {
        // Получаем все точки функции (иначе для LinkedList-реализации будет медленно)
        Point[] pts = TabulatedFunctionOperationService.asPoints(function);

        // Список задач
        List<Callable<Double>> tasks = new ArrayList<>();
        // Создаем отдельную задачу для каждого интервала
        // и добавляем в список задач
        for (int i = 1; i < function.getCount(); ++i) {
            double x0 = pts[i - 1].x;
            double x1 = pts[i].x;
            double y0 = pts[i - 1].y;
            double y1 = pts[i].y;
            tasks.add(new IntegralTask(x0, x1, y0, y1));
        }

        // Создаем фиксированный пул потоков
        ExecutorService executorService = Executors.newFixedThreadPool(nPoolSize);
        // Назначаем созданные задачи для выполнения
        List<Future<Double>> results = executorService.invokeAll(tasks);

        // Подсчитываем результат
        double sum = 0;
        for (Future<Double> result : results) {
            sum += result.get();
        }
        return sum;
    }


    record IntegralTask(double x0, double x1, double y0, double y1) implements Callable<Double> {
        @Override
        public Double call() {
            double w = x1 - x0;
            double h = (Math.abs(y0) + Math.abs(y1)) / 2;
            return w * h;
        }
    }
}
