package test.net.adultsmath.m1zc3;

import main.net.adultsmath.MatrixDiagonalized;
import main.net.adultsmath.Vector;
import main.net.adultsmath.VectorSet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MatrixDiagonalizedTest {

    @Test
    void toPD() {
        Map<VectorSet, Double> PDMap = new HashMap<>();
        PDMap.put(new VectorSet(1, 0, 1), 2.0);
        PDMap.put(new VectorSet(0, -1, 2), -1.0);
        PDMap.put(new VectorSet(0, -1, -1), 2.0);
        MatrixDiagonalized diagonalized = new MatrixDiagonalized(PDMap);
        Set<Vector> eigenvectors = new HashSet<>(diagonalized.matrixP().getTransposeMatrix().getEntries());
        Set<Vector> expected = new HashSet<>();
        expected.add(new Vector(1, 0, 1));
        expected.add(new Vector(0, -1, 2));
        expected.add(new Vector(0, -1, -1));
        assertEquals(expected, eigenvectors);
    }
}