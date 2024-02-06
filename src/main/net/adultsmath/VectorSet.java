package main.net.adultsmath;

import java.util.*;

public record VectorSet (Set<Vector> vectors) {

    public VectorSet(Set<Vector> vectors) {
        List<Vector> vectorList = new ArrayList<>(vectors);
        int vectorSize = vectorList.get(0).getSize();
        if (vectors.size() != 0) {
            int dimension = vectors.size();
            vectors.forEach(vector -> {
                if (vector.getSize() != vectorSize)
                    throw new UnequalVectorSizeException();
            });

            for (int i = 0; i < dimension; i++) {
                for (int j = i + 1; j < dimension; j++) {
                    if (vectorList.get(i).isScalarMultiple(vectorList.get(j)))
                        throw new LinearlyDependentVectorsException();
                }
            }

            this.vectors = new HashSet<>(vectors);
        }
        else {
            this.vectors = new HashSet<>();
        }
    }

    public VectorSet(List<Vector> vectors) {
        this(new HashSet<>(vectors));
    }

    public VectorSet(Vector... vectors) {
        this(List.of(vectors));
    }

    public VectorSet(double... comps) {
        this(List.of(new Vector(comps)));
    }

    public VectorSet() {
        this(new HashSet<>());
    }

    public List<Vector> getVectorsList() {
        if (Objects.isNull(this.vectors)) throw new EmptyVectorSetException();
        return new ArrayList<>(this.vectors);
    }

    public int getDimension() {
        return this.vectors.size();
    }

    public int getVectorSize() {
        return this.getVectorsList().get(0).getSize();
    }

    public boolean contains(Vector vector) {
        for (Vector v : this.vectors) {
            if (vector.isScalarMultiple(v)) return true;
        }
        return false;
    }

    public boolean containsLinearCombination(Vector vector) {
        List<Vector> vectorList = this.getVectorsList();
        vectorList.add(vector);
        Matrix matrix = Matrix.createFromColumnVectors(vectorList);
        return matrix.getSolution().solutionType() == LinearSystemSolution.SolutionType.SINGLE;
    }

    public VectorSet getUnitVectorSet() {
        Set<Vector> newVectors = new HashSet<>();
        for (Vector vector : this.vectors) {
            newVectors.add(vector.getUnitVector());
        }
        return new VectorSet(newVectors);
    }

    @Override
    public String toString() {
        List<Vector> vectorsList = new ArrayList<>(this.vectors);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i < this.getDimension(); i++) {
            stringBuilder.append(vectorsList.get(i));
            if (i != this.getDimension() - 1) stringBuilder.append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof VectorSet vectorSet
                && this.getDimension() == vectorSet.getDimension()
                && this.getVectorSize() == vectorSet.getVectorSize()) {
            for (Vector v : this.vectors) {
                if (!vectorSet.contains(v)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vectors);
    }

    public static class UnequalVectorSizeException extends RuntimeException {}

    public static class LinearlyDependentVectorsException extends RuntimeException {}

    public static class EmptyVectorSetException extends RuntimeException {}
}
