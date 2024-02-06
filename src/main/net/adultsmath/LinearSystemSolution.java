package main.net.adultsmath;

import java.util.Objects;

public record LinearSystemSolution (SolutionType solutionType, Vector value, VectorSet solutionSet) {
    /*
        When a linear system has only one solution, the solution is represented by a single VectorRn

        When a linear system has infinitely many solutions, the solution is represented by taking any linear
        combination of a list of vectors
     */
    enum SolutionType {
        SINGLE,
        INFINITE,
        NONE
    }

    public LinearSystemSolution {
        if (solutionType == SolutionType.INFINITE && solutionSet.getVectorSize() != value.getSize())
            throw new ValueSolutionSetUnequalSizeException();
    }

    public LinearSystemSolution(Vector value) {
        this(SolutionType.SINGLE, value, null);
    }

    public LinearSystemSolution(Vector value, VectorSet solutionSet) {
        this(SolutionType.INFINITE, value, solutionSet);
    }

    public LinearSystemSolution(VectorSet solutionSet) {
        this(SolutionType.INFINITE, Vector.createZeroVector(solutionSet.getVectorSize()), solutionSet);
    }

    public LinearSystemSolution() {
        this(SolutionType.NONE, null, null);
    }

    public SolutionType solutionType() {
        switch (this.solutionType) {
            case INFINITE, SINGLE, NONE -> {
                return solutionType;
            }
            default -> throw new UndeclaredSolutionTypeException();
        }
    }

    public Vector value() {
        if (Objects.equals(this.solutionType, SolutionType.NONE)) throw new NoSolutionException();
        return value;
    }

    public VectorSet solutionSet() {
        switch (this.solutionType) {
            case NONE -> throw new NoSolutionException();
            case SINGLE -> throw new NoArbitraryUnknownsException();
            case INFINITE -> {
                return solutionSet;
            }
            default -> throw new UndeclaredSolutionTypeException();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.solutionType == SolutionType.INFINITE || this.solutionType == SolutionType.SINGLE) {
            stringBuilder.append("(");
            for (int i = 0; i < this.value.getSize(); i++) {
                if (i != this.value.getSize() - 1) {
                    stringBuilder.append("x" + (i + 1) + ", ");
                } else stringBuilder.append("x" + (i + 1) + ")");
            }
            stringBuilder.append(" =\n");
        }
        switch (this.solutionType) {
            case SINGLE -> stringBuilder.append(this.value);
            case INFINITE -> stringBuilder.append(this.value + " + " + this.solutionSet);
            case NONE -> {
                return "{NONE}";
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof LinearSystemSolution solution) {
            if (this.solutionType.equals(solution.solutionType)) {
                switch (this.solutionType) {
                    case NONE -> {
                        return true;
                    }
                    case SINGLE -> {
                        return this.value.equals(solution.value);
                    }
                    case INFINITE -> {
                        return this.value.equals(solution.value) &&
                                this.solutionSet.equals(solution.solutionSet);
                    }
                    default -> throw new UndeclaredSolutionTypeException();
                }
            }
            return false;
        }
        return false;
    }

    public static class NoSolutionException extends RuntimeException {}

    public static class NoArbitraryUnknownsException extends RuntimeException {}

    public static class UndeclaredSolutionTypeException extends RuntimeException {}

    public static class ValueSolutionSetUnequalSizeException extends RuntimeException {}
}
