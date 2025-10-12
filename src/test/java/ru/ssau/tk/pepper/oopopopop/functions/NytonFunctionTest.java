package ru.ssau.tk.pepper.oopopopop.functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NytonFunctionTest {
    @Test

    public void testApply() {
        MathFunction quadratic = x -> x * x - 4;
        MathFunction quadraticDerivative = x -> 2 * x;

        NytonFunction Nyton1 = new NytonFunction(quadratic, quadraticDerivative, 1e-6, 100);
        double root1 = Nyton1.apply(3.0); // Начальное приближение 3.0

        //x = ±2
        assertEquals(2, root1, 0.0001);


        MathFunction sin = Math::sin;
        MathFunction sinDerivative = Math::cos;

        NytonFunction Nyton2 = new NytonFunction(sin, sinDerivative, 1e-6, 100);
        double root2 = Nyton2.apply(3.0);

        // x = kπ, где k ∈ Z
        assertEquals(3.14159, root2, 0.001);

        MathFunction exp = x -> Math.exp(x) - 2;
        MathFunction expDerivative = Math::exp;

        NytonFunction Nyton3 = new NytonFunction(exp, expDerivative, 1e-6, 100);
        double root3 = Nyton3.apply(1.0);

        //x = ln(2)
        assertEquals(0.693, root3, 0.001);
    }
}