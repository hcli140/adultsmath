package test.net.adultsmath.Expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructionTest {
    @Test
    void testNull() {
        assertThrows(Expression.OperationOnNullExpressionException.class,
                () -> {Expression expression = Expression.add(null, null);});
    }
    @Test
    void testNum() {
        Expression expression = new Expression(3);
        assertEquals(3, expression.value());
        assertTrue(expression.isNum());
    }
}
