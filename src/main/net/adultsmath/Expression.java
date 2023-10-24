package main.net.adultsmath;


import main.net.adultsmath.m1zc3.FunctionInputs;

import java.util.*;

//  can either hold a number, a variable, an operator on two expressions
public record Expression(Type type, Double value, Variable var, int tier, List<Expression> operands,
                         Set<Variable> containedVars, Set<Type> containedOperations) {

    /*
        ADD: op1 + op2
        SUBTRACT: op1 - op2
        MULTIPLY: op1 * op2
        DIVIDE: op1 / op2
        POWER: op1 ^ op2
        LOG: log(op1) -> base op2
     */
    enum Type {
        NUM, VAR,
        ADD, SUBTRACT,
        MULTIPLY, DIVIDE,
        POWER, LOG,
        SIN, COS, TAN,
        ASIN, ACOS, ATAN,
        ABS
    }

    public Expression(Type type,
                      Double value,
                      Variable var,
                      int tier,
                      List<Expression> operands,
                      Set<Variable> containedVars,
                      Set<Type> containedOperations) {
        this.type = type;
        this.tier = tier;
        switch (type) {
            case ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER, LOG -> {
                if (Objects.isNull(operands)) throw new NullOperandConstructionException();
                else if (operands.size() != 2) throw new InvalidNumberOfOperandsConstructionException();
                for (Expression expression : operands) {
                    if (Objects.isNull(expression)) throw new InvalidNumberOfOperandsConstructionException();
                }
                this.value = null;
                this.var = null;
                this.operands = operands;
                this.containedVars = containedVars;
                this.containedOperations = containedOperations;
            }
            case SIN, COS, TAN, ASIN, ACOS, ATAN, ABS -> {
                if (Objects.isNull(operands)) throw new NullOperandConstructionException();
                else if (operands.size() != 1) throw new InvalidNumberOfOperandsConstructionException();
                for (Expression expression : operands) {
                    if (Objects.isNull(expression)) throw new InvalidNumberOfOperandsConstructionException();
                }
                this.value = null;
                this.var = null;
                this.operands = operands;
                this.containedVars = containedVars;
                this.containedOperations = containedOperations;
            }
            case NUM -> {
                if (Objects.isNull(value)) throw new NullValueConstructionException();
                this.value = value;
                this.var = null;
                this.operands = new ArrayList<>();
                this.containedVars = new HashSet<>();
                this.containedOperations = new HashSet<>();
            }
            case VAR -> {
                if (Objects.isNull(var)) throw new NullVarConstructionException();
                this.value = null;
                this.var = var;
                this.operands = new ArrayList<>();
                this.containedVars = new HashSet<>();
                this.containedOperations = new HashSet<>();
            }
            default -> {
                throw new NullTypeConstructionException();
            }
        }
    }

    public Expression(double a) {
        this(Type.NUM, a, null, 0, new ArrayList<>(), new HashSet<>(), new HashSet<>());
    }

    public Expression(Variable var) {
        this(Type.VAR, null, var, 0, new ArrayList<>(), new HashSet<>(), new HashSet<>());
    }

    private static Expression operateOn(Type operation, Expression op1, Expression op2) {
        if (Objects.isNull(op1) || Objects.isNull(op2)) throw new OperationOnNullExpressionException();
        if(op1.isNum() && op2.isNum()) {
            switch (operation) {
                case ADD -> {
                    return new Expression(op1.value() + op2.value());
                }
                case SUBTRACT -> {
                    return new Expression(op1.value() - op2.value());
                }
                case MULTIPLY -> {
                    return new Expression(op1.value() * op2.value());
                }
                case DIVIDE -> {
                    return new Expression(op1.value() / op2.value());
                }
                case POWER -> {
                    return new Expression(Math.pow(op1.value(), op2.value()));
                }
                case LOG -> {
                    return new Expression(Math.log(op1.value()) / Math.log(op2.value()));
                }
            }
        }
        return new Expression(operation, null, null,
                registerTier(op1, op2),
                Arrays.asList(op1, op2),
                registerContainedVars(op1, op2),
                registerContainedOperations(op1, op2));
    }

    private static Expression operateOn(Type operation, Expression op) {
        if (Objects.isNull(op)) throw new OperationOnNullExpressionException();
        if (op.isNum()) {
            switch (operation) {
                case SIN -> {
                    return new Expression(Math.sin(op.value()));
                }
                case COS -> {
                    return new Expression(Math.cos(op.value()));
                }
                case TAN -> {
                    return new Expression(Math.tan(op.value()));
                }
                case ASIN -> {
                    return new Expression(Math.asin(op.value()));
                }
                case ACOS -> {
                    return new Expression(Math.acos(op.value()));
                }
                case ATAN -> {
                    return new Expression(Math.atan(op.value()));
                }
                case ABS -> {
                    return new Expression(Math.abs(op.value()));
                }
            }
        }
        return new Expression(operation, null, null,
                registerTier(op),
                List.of(op),
                registerContainedVars(op),
                registerContainedOperations(op));
    }

    public static Expression add(Expression op1, Expression op2) {
        return operateOn(Type.ADD, op1, op2);
    }

    public static Expression subtract(Expression op1, Expression op2) {
        return operateOn(Type.SUBTRACT, op1, op2);
    }

    public static Expression multiply(Expression op1, Expression op2) {
        return operateOn(Type.MULTIPLY, op1, op2);
    }

    public static Expression divide(Expression op1, Expression op2) {
        return operateOn(Type.MULTIPLY, op1, op2);
    }

    public static Expression power(Expression op1, Expression op2) {
        return operateOn(Type.POWER, op1, op2);
    }

    public static Expression log(Expression op1, Expression op2) {
        return operateOn(Type.LOG, op1, op2);
    }

    public static Expression sin(Expression op) {
        return operateOn(Type.SIN, op);
    }

    public static Expression cos(Expression op) {
        return operateOn(Type.COS, op);
    }

    public static Expression tan(Expression op) {
        return operateOn(Type.TAN, op);
    }

    public static Expression asin(Expression op) {
        return operateOn(Type.ASIN, op);
    }

    public static Expression acos(Expression op) {
        return operateOn(Type.ACOS, op);
    }

    public static Expression atan(Expression op) {
        return operateOn(Type.ATAN, op);
    }

    public static Expression abs(Expression op) {
        return operateOn(Type.ABS, op);
    }

    private static Set<Variable> registerContainedVars(Expression... expressions) {
        Set<Variable> variables = new HashSet<>();
        for (Expression expression : expressions) {
            if (Objects.isNull(expression)) throw new OperationOnNullExpressionException();
            if (expression.isVar()) variables.add(expression.var());
            variables.addAll(expression.containedVars);
        }
        return variables;
    }

    private static Set<Type> registerContainedOperations(Expression... expressions) {
        Set<Type> types = new HashSet<>();
        for (Expression expression : expressions) {
            if (Objects.isNull(expression)) throw new OperationOnNullExpressionException();
            if (expression.isOperator()) {
                types.add(expression.type());
            }
            types.addAll(expression.containedOperations());
        }
        return types;
    }

    private static int registerTier(Expression... expressions) {
        int currentMax = -1;
        for (Expression expression : expressions) {
            if (Objects.isNull(expression)) throw new OperationOnNullExpressionException();
            currentMax = Math.max(expression.tier(), currentMax);
        }
        return currentMax + 1;
    }


    public Expression evaluate(FunctionInputs inputs) {
        switch (this.type()) {
            case NUM -> {
                return this;
            }
            case VAR -> {
                if (inputs.contains(this.var())) return new Expression(inputs.get(this.var()));
                return this;
            }
            case ADD -> {
                return add(Objects.requireNonNull(this.operands().get(0).evaluate(inputs)),
                        this.operands().get(1).evaluate(inputs));
            }
            case SUBTRACT -> {
                return subtract(Objects.requireNonNull(this.operands().get(0).evaluate(inputs)),
                        this.operands().get(1).evaluate(inputs));
            }
            case MULTIPLY -> {
                return multiply(Objects.requireNonNull(this.operands().get(0).evaluate(inputs)),
                        this.operands().get(1).evaluate(inputs));
            }
            case DIVIDE -> {
                return divide(Objects.requireNonNull(this.operands().get(0).evaluate(inputs)),
                        this.operands().get(1).evaluate(inputs));
            }
            case POWER -> {
                return power(Objects.requireNonNull(this.operands().get(0).evaluate(inputs)),
                        this.operands().get(1).evaluate(inputs));
            }
            case LOG -> {
                return log(this.operands().get(0).evaluate(inputs),
                        this.operands.get(1).evaluate(inputs));
            }
            case SIN -> {
                return sin(this.operands.get(0).evaluate(inputs));
            }
            case COS -> {
                return cos(this.operands.get(0).evaluate(inputs));
            }
            case TAN -> {
                return tan(this.operands.get(0).evaluate(inputs));
            }
            case ASIN -> {
                return asin(this.operands.get(0).evaluate(inputs));
            }
            case ACOS -> {
                return acos(this.operands.get(0).evaluate(inputs));
            }
            case ATAN -> {
                return atan(this.operands.get(0).evaluate(inputs));
            }
            case ABS -> {
                return abs(this.operands.get(0).evaluate(inputs));
            }
            default -> throw new TypeUndefinedException();
        }
    }


    @Override
    public Double value() {
        if (this.type != Type.NUM) throw new NoValueException();
        return value;
    }

    @Override
    public Variable var() {
        if (this.type != Type.VAR) throw new NoVarException();
        return var;
    }

    public boolean isNumOrVar() {
        return isNum() || isVar();
    }

    public boolean isNum() {
        return this.type == Type.NUM;
    }

    public boolean isVar() {
        return this.type == Type.VAR;
    }

    public boolean isOperator() {
        return isAdd() || isSubtract() || isMultiply() || isDivide() || isPower();
    }

    public boolean isAdd() {
        return this.type == Type.ADD;
    }

    public boolean isSubtract() {
        return this.type == Type.SUBTRACT;
    }

    public boolean isMultiply() {
        return this.type == Type.MULTIPLY;
    }

    public boolean isDivide() {
        return this.type == Type.DIVIDE;
    }

    public boolean isPower() {
        return this.type == Type.POWER;
    }

    public static class NullTypeConstructionException extends RuntimeException {}

    public static class NullValueConstructionException extends RuntimeException {}

    public static class NullVarConstructionException extends RuntimeException {}

    public static class NullOperandConstructionException extends RuntimeException {}

    public static class InvalidNumberOfOperandsConstructionException extends RuntimeException {}

    public static class OperationOnNullExpressionException extends RuntimeException {}

    public static class NoValueException extends RuntimeException {}

    public static class NoVarException extends RuntimeException {}

    public static class TypeUndefinedException extends RuntimeException {}
}
