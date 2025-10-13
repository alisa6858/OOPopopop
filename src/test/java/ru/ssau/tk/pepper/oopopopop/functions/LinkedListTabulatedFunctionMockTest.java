package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionMockTest {
    private final double DELTA = 1e-10;

    // y = 2 * x + 1
    private final double[] X1 = {1, 2, 3, 4, 5};
    private final double[] Y1 = {3, 5, 7, 9, 11};

    @Test
    void floorIndexOfX() {
        LinkedListTabulatedFunctionMock f = new LinkedListTabulatedFunctionMock(X1, Y1);

        assertThrows(IllegalArgumentException.class, () -> f.floorIndexOfX(X1[0] - 1));

        assertEquals(0, f.floorIndexOfX(X1[0]));
        assertEquals(0, f.floorIndexOfX((X1[0] + X1[1]) / 2));

        assertEquals(1, f.floorIndexOfX(X1[1]));
        assertEquals(1, f.floorIndexOfX((X1[1] + X1[2]) / 2));

        assertEquals(2, f.floorIndexOfX(X1[2]));
        assertEquals(2, f.floorIndexOfX((X1[2] + X1[3]) / 2));

        assertEquals(3, f.floorIndexOfX(X1[3]));
        assertEquals(3, f.floorIndexOfX((X1[3] + X1[4]) / 2));

        assertEquals(4, f.floorIndexOfX(X1[4]));
        assertEquals(4, f.floorIndexOfX(X1[4] + 1));
    }

    @Test
    void empty() {
        LinkedListTabulatedFunctionMock f = new LinkedListTabulatedFunctionMock(X1, Y1);
        while (f.getCount() > 0) {
            f.remove(0);
        }

        assertThrows(IllegalStateException.class, () -> f.floorIndexOfX(0));
        assertThrows(IllegalArgumentException.class, () -> f.getX(0));
        assertThrows(IllegalArgumentException.class, () -> f.getY(0));
        assertThrows(IllegalArgumentException.class, () -> f.setY(0, 0));
        assertEquals(-1, f.indexOfX(0));
        assertEquals(-1, f.indexOfY(0));
        assertThrows(IllegalStateException.class, f::leftBound);
        assertThrows(IllegalStateException.class, f::rightBound);
    }
}