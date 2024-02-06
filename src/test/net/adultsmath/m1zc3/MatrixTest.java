package test.net.adultsmath.m1zc3;

import main.net.adultsmath.*;
import main.net.adultsmath.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    //  solve() ------------------------------------------------------

    @Test
    void testSolveSingle() {
        Assertions.assertEquals(new LinearSystemSolution(
                        new Vector(
                                0.884615384615385,
                                1.1538461538461537,
                                0.48076923076923056)),
                new Matrix(new double[][]{
                        {1, 7, -2, 8},
                        {-1, 0, 6, 2},
                        {5, -3, -2, 0}
                }).getSolution());
    }

    @Test
    void testSolveInfiniteSolutions_1ImpOn3_23Arb() {
        Set<Vector> vectors = new HashSet<>();
        vectors.add(new Vector(0, 1, 0));
        vectors.add(new Vector(-1, 0, 1));
        assertEquals(new LinearSystemSolution(
                        Vector.createZeroVector(3), new VectorSet(vectors)),
                new Matrix(new double[][]{
                        {2, 0, 2, 0},
                        {-1, 0, -1, 0},
                        {-1, 0, -1, 0}}
                ).getSolution());
    }

    @Test
    void testSolveInfiniteSolutions_1ImpOn34_2Exp_3Arb() {
        Set<Vector> vectors = new HashSet<>();
        vectors.add(new Vector(-1, 0, 1, 0));
        vectors.add(new Vector(-3, 0, 0, 1));
        assertEquals(new LinearSystemSolution(
                        new Vector(5, 2, 0, 0), new VectorSet(vectors)),
                new Matrix(new double[][]{
                        {1, 0, 1, 3, 5},
                        {0, 1, 0, 0, 2},
                        {0, 0, 0, 0, 0}
                }).getSolution());
    }

    @Test
    void testSolve4X4Inconsistent() {
        assertEquals(new LinearSystemSolution(),
                new Matrix(new double[][]{
                        {1, 0, 0, 3},
                        {0, 1, 0, 2},
                        {0, 0, 1, -1},
                        {0, 1, 0, 3}
                }).getSolution());
    }


    @Test
    void testSolve2X3Inconsistent() {
        assertEquals(new LinearSystemSolution(),
                new Matrix(new double[][]{
                        {1, 0, 3},
                        {0, 0, -1}
                }).getSolution());
    }

    //  eig() ------------------------------------------------------

    @Test
    void testEigI3() {
        assertEquals(new MatrixDiagonalized.Builder()
                        .put(new VectorSet(1, 0, 0), 1.0)
                        .put(new VectorSet(0, 1, 0), 1.0)
                        .put(new VectorSet(0, 0, 1), 1.0)
                        .build(),
                Matrix.createIdentityMatrix(3).getDiagonalized());
    }

    @Test
    void testEig3X3() {
        assertEquals(new MatrixDiagonalized.Builder()
                        .put(new VectorSet(-1, 0, 1), 2.0)
                        .put(new VectorSet(0, 1, 0), 2.0)
                        .put(new VectorSet(-2, 1, 1), 1.0)
                        .build(),
                new Matrix(new double[][]{
                        {0, 0, -2},
                        {1, 2, 1},
                        {1, 0, 3}
                }).getDiagonalized());
    }

    @Test
    void testEig2X2() {
        assertEquals(new MatrixDiagonalized.Builder()
                        .put(new VectorSet(1, -3), -5.0)
                        .put(new VectorSet(2, 1), 2.0)
                        .build(),
                new Matrix(new double[][]{
                        {1, 2},
                        {3, -4}
                }).getDiagonalized());
    }

    @Test
    void testNotDiagonalizeableEig() {
        Matrix matrix = new Matrix(new double[][]{
                {2, 1, 1},
                {0, 1, 0},
                {1, -1, 2}
        });

        assertThrows(Matrix.NotDiagonalizeableException.class, matrix::getDiagonalized);
    }
}