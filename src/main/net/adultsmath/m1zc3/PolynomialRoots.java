package main.net.adultsmath.m1zc3;

import java.util.*;

public record PolynomialRoots(Map<Double, Integer> rootsMap) {

    public PolynomialRoots() {
        this(new HashMap<>());
    }

    public List<Map.Entry<Double, Integer>> getEntryList() {
        return new ArrayList<>(this.rootsMap.entrySet());
    }

    public Set<Map.Entry<Double, Integer>> getEntrySet() {
        return this.rootsMap.entrySet();
    }

    public Set<Double> getRootsSet() {
        return this.rootsMap().keySet();
    }

    public List<Double> getRootsList() {
        return new ArrayList<>(this.getRootsSet());
    }

    public Map.Entry<Double, Integer> getRoot() {
        if (this.getNumRoots() == 0) throw new NoRootsException();
        else if (this.getNumRoots() != 1) throw new MoreThanOneRootException();
        return this.getEntryList().get(0);
    }

    public int getMultiplicity(double inputRoot) {
        for (double root : this.getRootsSet()) {
            if (Math.abs(root - inputRoot) < 1E-5) {
                return this.rootsMap().get(root);
            }
        }
        throw new NoSuchRootException();
    }

    public int getNumRoots() {
        return this.rootsMap().size();
    }

    public boolean containsRoot(double source) {
        for (double root : this.getRootsSet()) {
            if (Math.abs(root - source) < 1E-5) return true;
        }
        return false;
    }

    public boolean containsEntry(double inputRoot, int multiplicity) {
        for (Map.Entry<Double, Integer> entry : this.getEntrySet()) {
            if (Math.abs(entry.getKey() - inputRoot) < 1E-5 && entry.getValue() == multiplicity) return true;
        }
        return false;
    }

    public boolean containsEntry(Map.Entry<Double, Integer> entry) {
        return this.containsEntry(entry.getKey(), entry.getValue());
    }

    public void put(double inputRoot, int multiplicity) {
        if (Math.abs(inputRoot) < 1E-7) inputRoot = 0.0;
        if (this.containsRoot(inputRoot)) {
            for (double root : this.getRootsSet()) {
                if (Math.abs(inputRoot - root) < 1E-5) {
                    this.rootsMap.put(root, multiplicity);
                }
            }
        }
        else this.rootsMap.put(inputRoot, multiplicity);
    }

    public void addRoot(double root, int multiplicity) {
        if (Math.abs(root) < 1E-5) root = 0.0;
        if (this.containsRoot(root)) {
            this.put(root, this.getMultiplicity(root) + multiplicity);
        }
        else {
            this.put(root, multiplicity);
        }
    }

    public void addRoot(double root) {
        this.addRoot(root, 1);
    }

    public void addRoot(Map.Entry<Double, Integer> entry) {
        this.addRoot(entry.getKey(), entry.getValue());
    }

    public void clear() {
        this.rootsMap.clear();
    }

    public static PolynomialRoots copyOf(PolynomialRoots polynomialRoots) {
        return new PolynomialRoots(polynomialRoots.rootsMap());
    }

    public static PolynomialRoots combine(List<PolynomialRoots> rootsList) {
        PolynomialRoots polynomialRoots = new PolynomialRoots();
        for (PolynomialRoots roots : rootsList) {
            for (Map.Entry<Double, Integer> root : roots.getEntrySet()) {
                polynomialRoots.addRoot(root);
            }
        }
        return polynomialRoots;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof PolynomialRoots roots && roots.getNumRoots() == this.getNumRoots()) {
            for (Map.Entry<Double, Integer> entry : this.getEntrySet()) {
                if (!roots.containsEntry(entry)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Roots{");
        for (int i = 0; i < this.rootsMap.size(); i++) {
            sb.append("(" + this.getEntryList().get(i).getKey() +
                    " -- " + this.getEntryList().get(i).getValue() + ")");
            if (i + 1 < this.rootsMap.size()) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public static class NoRootsException extends RuntimeException {}

    public static class MoreThanOneRootException extends RuntimeException {}

    public static class NoSuchRootException extends RuntimeException {}
}
