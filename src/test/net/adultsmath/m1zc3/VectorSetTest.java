package test.net.adultsmath.m1zc3;

import main.net.adultsmath.m1zc3.Vector;
import main.net.adultsmath.m1zc3.VectorSet;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VectorSetTest {
    @Test
    void testIsLinearCombinationTrue() {
        VectorSet vectorSet = new VectorSet(1, 2, 3);
        assertTrue(vectorSet.containsLinearCombination(new Vector(2, 4, 6)));
    }
}