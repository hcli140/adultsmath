package test.net.adultsmath.m1zc3;

import main.net.adultsmath.m1zc3.Polynomial;
import main.net.adultsmath.m1zc3.PolynomialRoots;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    //  quadraticMethod
    @Test
    void testQuadraticMethod02() {
        int start = -100;
        int end = 100;
        PolynomialRoots roots;
        for (int root = start; root < end; root++) {
            roots = new PolynomialRoots();
            roots.addRoot(root, 2);
            assertEquals(roots, Polynomial.createFromLinearFactors(root, root).getRoots());
        }
    }

    @Test
    void testQuadraticMethodRandom() {
        Random random = new Random();
        PolynomialRoots roots = new PolynomialRoots();
        int count = 100;
        for (int i = 0; i < count; i++) {
            roots.clear();
            List<Double> factors = new ArrayList<>();
            double randomInteger;
            for (int j = 0; j < 2; j++) {
                randomInteger = random.nextInt(100);
                factors.add(randomInteger);
            }
            roots.addRoot(factors.get(0));
            roots.addRoot(factors.get(1));
            assertEquals(roots, Polynomial.createFromLinearFactors(factors.get(0), factors.get(1)).getRoots());
        }
    }

    //  cardanosMethod

    @Test
    void testCardanosMethodRandom() {
        Random random = new Random();
        PolynomialRoots roots = new PolynomialRoots();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            roots.clear();
            List<Double> factors = new ArrayList<>();
            double randomInteger;
            for (int j = 0; j < 3; j++) {
                randomInteger = random.nextInt(100);
                factors.add(randomInteger);
            }
            roots.addRoot(factors.get(0));
            roots.addRoot(factors.get(1));
            roots.addRoot(factors.get(2));
            assertEquals(roots,
                    Polynomial.createFromLinearFactors(factors.get(0), factors.get(1), factors.get(2)).getRoots());
        }
    }

    @Test
    void testCardanosMethod003() {
        PolynomialRoots roots = new PolynomialRoots();
        int start = -100;
        int end = 100;
        for (int root = start; root < end; root++) {
            roots.clear();
            roots.addRoot(root, 3);
            assertEquals(roots, Polynomial.createFromLinearFactors(root, root, root).getRoots());
        }
    }

    @Test
    void testCardanosMethod012() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(-2, 1);
        roots.addRoot(1, 2);
        assertEquals(roots, Polynomial.createFromLinearFactors(-2, 1, 1).getRoots());
    }

    @Test
    void testCardanosMethod111() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(1);
        roots.addRoot(2);
        roots.addRoot(3);
        assertEquals(roots, Polynomial.createFromLinearFactors(1, 2, 3).getRoots());
    }

    //  ferrarisMethod
    @Test
    void testFerrarisMethodRandom() {
        PolynomialRoots roots = new PolynomialRoots();
        Random random = new Random();
        double randomInteger;
        int count = 1000;
        for (int i = 0; i < count; i++) {
            List<Double> factors = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                randomInteger = random.nextInt(100);
                factors.add(randomInteger);
            }
            roots.clear();
            roots.addRoot(factors.get(0));
            roots.addRoot(factors.get(1));
            roots.addRoot(factors.get(2));
            roots.addRoot(factors.get(3));
            try {
                Polynomial.createFromLinearFactors(
                        factors.get(0), factors.get(1), factors.get(2), factors.get(3)).getRoots();
            } catch (PolynomialRoots.MoreThanOneRootException e) {
                System.out.println(factors);
                throw e;
            }
            assertEquals(roots, Polynomial.createFromLinearFactors(
                    factors.get(0), factors.get(1), factors.get(2), factors.get(3)).getRoots());
        }
    }

    @Test
    void testFerrarisMethod1111() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(1);
        roots.addRoot(2);
        roots.addRoot(3);
        roots.addRoot(4);
        assertEquals(roots, Polynomial.createFromLinearFactors(1, 2, 3, 4).getRoots());
    }

    @Test
    void testFerrarisMethod0112() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(1, 2);
        roots.addRoot(2);
        roots.addRoot(3);
        assertEquals(roots, Polynomial.createFromLinearFactors(1, 2, 3, 1).getRoots());
    }

    @Test
    void testFerrarisMethod0022() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(1, 2);
        roots.addRoot(2, 2);
        assertEquals(roots, Polynomial.createFromLinearFactors(1, 2, 1, 2).getRoots());
    }

    @Test
    void testFerrarisMethod0013() {
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot(1, 3);
        roots.addRoot(2, 1);
        assertEquals(roots, Polynomial.createFromLinearFactors(1, 1, 1, 2).getRoots());
    }

    @Test
    void testFerrarisMethod0004() {
        PolynomialRoots roots = new PolynomialRoots();
        int start = -100;
        int end = 100;
        for (int root = start; root < end; root++) {
            roots.clear();
            roots.addRoot(root, 4);
            assertEquals(roots, Polynomial.createFromLinearFactors(root, root, root, root).getRoots());
        }
    }
}