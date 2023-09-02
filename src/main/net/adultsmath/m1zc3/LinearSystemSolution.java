package main.net.adultsmath.m1zc3;

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

    public SolutionType getSolutionType() {
        switch (this.solutionType) {
            case INFINITE, SINGLE, NONE -> {
                return solutionType;
            }
            default -> throw new UndeclaredSolutionTypeException();
        }
    }

    public Vector getValue() {
        if (Objects.equals(this.getSolutionType(), SolutionType.NONE)) throw new NoSolutionException();
        return value;
    }

    public VectorSet getSolutionSet() {
        switch (this.getSolutionType()) {
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
        if (this.getSolutionType() == SolutionType.INFINITE || this.getSolutionType() == SolutionType.SINGLE) {
            stringBuilder.append("(");
            for (int i = 0; i < this.getValue().getSize(); i++) {
                if (i != this.getValue().getSize() - 1) {
                    stringBuilder.append("x" + (i + 1) + ", ");
                } else stringBuilder.append("x" + (i + 1) + ")");
            }
            stringBuilder.append(" =\n");
        }
        switch (this.getSolutionType()) {
            case SINGLE -> {
                stringBuilder.append(this.getValue());
            }
            case INFINITE -> {
                stringBuilder.append(this.getValue() + " + " + this.getSolutionSet());
            }
            case NONE -> {
                return "{NONE}";
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof LinearSystemSolution) {
            LinearSystemSolution solution = (LinearSystemSolution) obj;
            if (this.getSolutionType().equals(solution.getSolutionType())) {
                if (this.getSolutionType() == SolutionType.NONE) {
                    return true;
                } else if (this.getSolutionType() == SolutionType.INFINITE) {
                    return this.getValue().equals(solution.getValue()) &&
                            this.getSolutionSet().equals(solution.getSolutionSet());
                } else return this.getValue().equals(solution.getValue());
            }
            return false;
        }
        return false;
    }

    public static class NoSolutionException extends RuntimeException {}

    public static class NoArbitraryUnknownsException extends RuntimeException {}

    public static class UndeclaredSolutionTypeException extends RuntimeException {}
}
