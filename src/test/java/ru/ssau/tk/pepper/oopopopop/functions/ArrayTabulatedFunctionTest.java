package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTabulatedFunctionTest {

    @Test
    void testConstructorFromArrays() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.leftBound(), 1e-10);
        assertEquals(4.0, function.rightBound(), 1e-10);
    }

    @Test
    void testConstructorFromArraysThrowsExceptionForInvalidInput() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0}; // Разные длины

        assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });

        double[] xValues2 = {1.0};
        double[] yValues2 = {1.0}; // Меньше 2 точек

        assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues2, yValues2);
        });

        double[] xValues3 = {2.0, 1.0}; // Не упорядочены
        double[] yValues3 = {1.0, 2.0};

        assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues3, yValues3);
        });
    }

    @Test
    void testConstructorFromFunction() {
        MathFunction sqr = new SqrFunction();
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(sqr, 0.0, 4.0, 5);

        assertEquals(5, function.getCount());
        assertEquals(0.0, function.leftBound(), 1e-10);
        assertEquals(4.0, function.rightBound(), 1e-10);

        // Проверяем значения
        assertEquals(0.0, function.getY(0), 1e-10);  // 0² = 0
        assertEquals(1.0, function.getY(1), 1e-10);  // 1² = 1
        assertEquals(4.0, function.getY(2), 1e-10);  // 2² = 4
        assertEquals(9.0, function.getY(3), 1e-10);  // 3² = 9
        assertEquals(16.0, function.getY(4), 1e-10); // 4² = 16
    }

    @Test
    void testConstructorFromFunctionWithEqualBounds() {
        MathFunction constant = new ConstantFunction(5.0);
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(constant, 2.0, 2.0, 3);

        assertEquals(3, function.getCount());
        assertEquals(2.0, function.leftBound(), 1e-10);
        assertEquals(2.0, function.rightBound(), 1e-10);

        // Все значения должны быть одинаковыми
        for (int i = 0; i < function.getCount(); i++) {
            assertEquals(2.0, function.getX(i), 1e-10);
            assertEquals(5.0, function.getY(i), 1e-10);
        }
    }

    @Test
    void testConstructorFromFunctionWithReversedBounds() {
        MathFunction sqr = new SqrFunction();
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(sqr, 4.0, 0.0, 5);

        assertEquals(5, function.getCount());
        assertEquals(0.0, function.leftBound(), 1e-10); // Должен автоматически отсортировать
        assertEquals(4.0, function.rightBound(), 1e-10);
    }

    @Test
    void testGetXGetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(2.0, function.getX(1), 1e-10);
        assertEquals(3.0, function.getX(2), 1e-10);

        assertEquals(10.0, function.getY(0), 1e-10);
        assertEquals(20.0, function.getY(1), 1e-10);
        assertEquals(30.0, function.getY(2), 1e-10);

        // Проверка исключений для неверных индексов
        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> function.getX(3));
        assertThrows(IndexOutOfBoundsException.class, () -> function.getY(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> function.getY(3));
    }

    @Test
    void testSetY() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.setY(1, 25.0);
        assertEquals(25.0, function.getY(1), 1e-10);

        // Проверка исключения для неверного индекса
        assertThrows(IndexOutOfBoundsException.class, () -> function.setY(5, 100.0));
    }

    @Test
    void testIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 2.0, 3.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfX(1.0));
        assertEquals(2, function.indexOfX(3.0));
        assertEquals(-1, function.indexOfX(5.0)); // Не существует
        assertEquals(-1, function.indexOfX(1.5)); // Не существует
    }

    @Test
    void testIndexOfY() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 20.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(0, function.indexOfY(10.0));
        assertEquals(1, function.indexOfY(20.0)); // Первое вхождение
        assertEquals(-1, function.indexOfY(50.0)); // Не существует
    }

    @Test
    void testFloorIndexOfX() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 2.0, 3.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(0, function.floorIndexOfX(0.5));  // Меньше минимального
        assertEquals(0, function.floorIndexOfX(1.0));  // Равно первому
        assertEquals(0, function.floorIndexOfX(1.5));  // Между 1 и 2
        assertEquals(1, function.floorIndexOfX(2.0));  // Равно второму
        assertEquals(1, function.floorIndexOfX(2.5));  // Между 2 и 3
        assertEquals(3, function.floorIndexOfX(4.0));  // Равно последнему
        assertEquals(4, function.floorIndexOfX(5.0));  // Больше максимального
    }

    @Test
    void testExtrapolateLeft() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция слева: x = 0.0
        double result = function.apply(0.0);
        double expected = 1.0 + (4.0 - 1.0) * (0.0 - 1.0) / (2.0 - 1.0); // = -2.0
        assertEquals(expected, result, 1e-10);

        // Тест с одной точкой
        double[] xSingle = {2.0};
        double[] ySingle = {5.0};
        ArrayTabulatedFunction singlePointFunction = new ArrayTabulatedFunction(xSingle, ySingle);
        assertEquals(5.0, singlePointFunction.apply(0.0), 1e-10); // Всегда возвращает 5.0
    }

    @Test
    void testExtrapolateRight() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Экстраполяция справа: x = 4.0
        double result = function.apply(4.0);
        double expected = 4.0 + (9.0 - 4.0) * (4.0 - 2.0) / (3.0 - 2.0); // = 14.0
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testInterpolate() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Интерполяция между 1 и 2: x = 1.5
        double result = function.apply(1.5);
        double expected = 1.0 + (4.0 - 1.0) * (1.5 - 1.0) / (2.0 - 1.0); // = 2.5
        assertEquals(expected, result, 1e-10);

        // Интерполяция между 2 и 3: x = 2.5
        result = function.apply(2.5);
        expected = 4.0 + (9.0 - 4.0) * (2.5 - 2.0) / (3.0 - 2.0); // = 6.5
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testApplyWithExactXValues() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Точные значения из таблицы
        assertEquals(10.0, function.apply(1.0), 1e-10);
        assertEquals(20.0, function.apply(2.0), 1e-10);
        assertEquals(30.0, function.apply(3.0), 1e-10);
    }

    @Test
    void testToString() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        String result = function.toString();
        assertTrue(result.contains("(1.0, 10.0)"));
        assertTrue(result.contains("(2.0, 20.0)"));
        assertTrue(result.startsWith("ArrayTabulatedFunction["));
    }

    @Test
    void testArrayProtection() {
        // Проверяем, что внешние изменения не влияют на внутренние массивы
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 2.0, 3.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Меняем исходные массивы
        xValues[0] = 100.0;
        yValues[0] = 100.0;

        // Внутренние массивы не должны измениться
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getY(0), 1e-10);
    }
}