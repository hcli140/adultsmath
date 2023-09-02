package main.net.adultsmath.m1zc3;

import java.util.*;

public class VectorSet {
    private final Set<Vector> vectors;
    private final int dimension;
    private final int vectorSize;

    public VectorSet(Set<Vector> vectors) {
        this.vectorSize = new ArrayList<>(vectors).get(0).getSize();
        vectors.forEach(vector -> {
            if (vector.getSize() != this.getVectorSize())
                throw new IllegalArgumentException("The vectors have different lengths");
        });
        this.vectors = new HashSet<>(vectors);
        this.dimension = this.vectors.size();
    }

    public VectorSet(List<Vector> vectors) {
        this(new HashSet<>(vectors));
    }

    public VectorSet(Vector... vectors) {
        this(List.of(vectors));
    }

    public VectorSet(double... comps) {
        Vector vector = new Vector(comps);
        this.vectors = new HashSet<>(List.of(vector));
        this.vectorSize = vector.getSize();
        this.dimension = 1;
    }

    public Set<Vector> getVectorsSet() {
        return vectors;
    }

    public List<Vector> getVectorsList() {
        return new ArrayList<>(this.getVectorsSet());
    }

    public Vector getVectorsList(int index) {
        return this.getVectorsList().get(index);
    }

    public int getDimension() {
        return dimension;
    }

    public int getVectorSize() {
        return vectorSize;
    }

    public boolean contains(Vector vector) {
        for (Vector v : this.getVectorsSet()) {
            if (vector.isScalarMultiple(vector)) return true;
        }
        return false;
    }

    public VectorSet getUnitVectorSet() {
        Set<Vector> newVectors = new HashSet<>();
        for (Vector vector : this.getVectorsSet()) {
            newVectors.add(vector.getUnitVector());
        }
        return new VectorSet(newVectors);
    }

    @Override
    public String toString() {
        List<Vector> vectorsList = new ArrayList<>(this.getVectorsSet());
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
            for (Vector v : this.getVectorsSet()) {
                if (!vectorSet.contains(v)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vectors, dimension, vectorSize);
    }
}
