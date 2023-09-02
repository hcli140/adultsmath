package main.net.adultsmath.m1zc3;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vector {
    private final List<Double> comps;
    private final int size;


    // --------------------------------------------------------------------------------------


    //  CONSTRUCTORS AND GETTERS
    public Vector(double... values) {
        size = values.length;
        comps = new ArrayList<>();
        for (double value : values) {
            comps.add(value);
        }
    }

    public Vector(List<Double> values) {
        this.size = values.size();
        this.comps = new ArrayList<>(values);
    }

    public static Vector createZeroVector(int size) {
        List<Double> comps = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            comps.add(0.0);
        }
        return new Vector(comps);
    }

    public List<Double> getComps() {
        return comps;
    }

    public double getComps(int index) {
        return comps.get(index);
    }

    public int getSize() {
        return size;
    }


    // --------------------------------------------------------------------------------------


    //  MANIPULATIONS
    //  add 0's if newLength > spaceR
    public Vector getTruncated(int newLength) {
        double[] newComps = new double[newLength];
        for (int i = 0; i < newComps.length; i++) {
            try {
                newComps[i] = this.getComps(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                newComps[i] = 0;
            }
        }
        return new Vector(newComps);
    }

    public Vector createCopy() {
        return this.getTruncated(this.getSize());
    }


    //  replace a component
    public Vector getReplace(int index, double a) {
        double[] newComps = new double[size];
        for (int i = 0; i < newComps.length; i++) {
            if (i == index) {
                newComps[i] = a;
            } else {
                newComps[i] = this.getComps(i);
            }
        }
        return new Vector(newComps);
    }

    public Vector getWithInsert(int index, double a) {
        double[] newComps;
        if (index <= size) {
            newComps = new double[size + 1];
            for (int i = 0; i < newComps.length; i++) {
                newComps[i] = this.getComps(i);
            }
        } else {
            newComps = new double[index + 1];
            for (int i = 0; i < newComps.length; i++) {
                try {
                    newComps[i] = this.getComps(i);
                } catch (ArrayIndexOutOfBoundsException e) {
                    newComps[i] = 0;
                }
            }
        }
        newComps[index] = a;
        return new Vector(newComps);
    }

    public Vector getSection(int lower, int upper) {
        double[] newComps = new double[upper - lower];
        int i = 0;
        for (int j = lower; j < upper; j++) {
            newComps[i] = this.getComps(j);
            i++;
        }
        return new Vector(newComps);
    }

    //  remove a component
    public Vector getWithoutIndex(int index) {
        List<Double> newComps = new ArrayList<>();
        int i = 0;
        for (int j = 0; j < this.getSize() - 1; j++) {
            if (i == index) {
                j--;
            } else {
                newComps.add(this.getComps(i));
            }
            i++;
        }
        return new Vector(newComps);
    }

    //  check if the vector is a null vector
    public boolean isZero() {
        for (int i = 0; i < size; i++) {
            if (this.getComps(i) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isScalarMultiple(Vector v) {
        return this.isSameDirection(v) || this.getUnitVector().equals(v.getUnitVector().getNegativeVector());
    }

    public boolean isSameDirection(Vector v) {
        return this.getUnitVector().equals(v.getUnitVector());
    }


    // --------------------------------------------------------------------------------------


    //  VECTOR OPERATIONS
    //  norm ||v||
    public double getNorm() {
        double sumOfSquares = 0;
        for (int i = 0; i < this.getSize(); i++) {
            sumOfSquares += this.getComps(i) * this.getComps(i);
        }
        return sqrt(sumOfSquares);
    }

    public Vector getUnitVector() {
        return Operator.scalMult(1 / this.getNorm(), this);
    }

    public Vector getNegativeVector() {
        return Operator.scalMult(-1, this);
    }


    //  show the vector as a string
    @Override
    public String toString() {
        String outputString = "V(";
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                outputString += this.getComps(i);
            } else {
                outputString += this.getComps(i) + ", ";
            }
        }
        outputString += ")";
        return outputString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Vector vector && this.getSize() == vector.getSize()) {
            for (int i = 0; i < this.getSize(); i++) {
                if (abs(this.getComps(i) - vector.getComps(i)) > 1E-7) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comps, size);
    }
}