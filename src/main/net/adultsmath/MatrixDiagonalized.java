package main.net.adultsmath;

import java.util.*;

public class MatrixDiagonalized {
    private final Map<VectorSet, Double> eigenMap;
    private final int size;

    public MatrixDiagonalized (Map<VectorSet, Double> eigenMap) {
        int previousVectorLength = 0;
        int currentVectorLength = 0;
        for (Map.Entry<VectorSet, Double> entry : eigenMap.entrySet()) {
            if (entry.getKey().getDimension() != 1)
                throw new IllegalArgumentException("Each vector set can only contain one vectors");
            currentVectorLength = entry.getKey().getVectorSize();
            if (previousVectorLength == 0) previousVectorLength = entry.getKey().getVectorSize();
            if (previousVectorLength != currentVectorLength)
                throw new IllegalArgumentException("The eigenvectors cannot have unequal lengths");
        }

        if (currentVectorLength != eigenMap.size())
            throw new IllegalArgumentException(
                    "The number of eigenvectors and the lengths of the eigenvectors are not equal");

        this.eigenMap = new HashMap<>(eigenMap);
        this.size = eigenMap.size();
    }

    public Map<VectorSet, Double> eigenMap() {
        return eigenMap;
    }

    public int size() {
        return size;
    }

    public Set<Map.Entry<VectorSet, Double>> entrySet() {
        return this.eigenMap().entrySet();
    }

    public Set<VectorSet> vectorsSet() {
        return this.eigenMap().keySet();
    }

    public List<VectorSet> vectorsList() {
        return new ArrayList<>(this.vectorsSet());
    }

    public VectorSet vectorsList(int index) {
        return this.vectorsList().get(index);
    }

    public List<Matrix> toPD() {
        List<Vector> pColumns = new ArrayList<>();
        List<Vector> dColumns = new ArrayList<>();
        List<Double> dCurrentColumn;
        int columnIndex = 0;
        for (Map.Entry<VectorSet, Double> entry : this.eigenMap().entrySet()) {
            pColumns.addAll(entry.getKey().vectors());
            dCurrentColumn = new ArrayList<>();
            for (int i = 0; i < entry.getKey().getVectorSize(); i++) {
                if (i == columnIndex) dCurrentColumn.add(entry.getValue());
                else dCurrentColumn.add(0.0);
            }
            dColumns.add(new Vector(dCurrentColumn));
            columnIndex++;
        }
        List<Matrix> PD = new ArrayList<>();
        PD.add(new Matrix(pColumns).getTransposeMatrix());
        PD.add(new Matrix(dColumns));
        return PD;
    }

    public Matrix matrixP() {
        return this.toPD().get(0);
    }

    public Matrix matrixD() {
        return this.toPD().get(0);
    }

    public boolean contains(VectorSet eigenvector, double eigenvalue) {
        for (Map.Entry<VectorSet, Double> entry : this.entrySet()) {
            if (entry.getKey().equals(eigenvector) && entry.getValue() == eigenvalue) return true;
        }
        return false;
    }

    public boolean contains(Map.Entry<VectorSet, Double> entry) {
        return this.contains(entry.getKey(), entry.getValue());
    }

    @Override
    public String toString() {
        return "P:\n" + matrixP() + "\nD:\n" + matrixD();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof MatrixDiagonalized diagonalized && diagonalized.size() == this.size()) {
            for (Map.Entry<VectorSet, Double> entry : diagonalized.entrySet()) {
                if (!this.contains(entry)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static class Builder {
        private final Map<VectorSet, Double> map = new HashMap<>();

        public Builder put(VectorSet eigenvector, double eigenvalue) {
            this.map.put(eigenvector, eigenvalue);
            return this;
        }

        public MatrixDiagonalized build() {
            return new MatrixDiagonalized(this.map);
        }
    }
}
