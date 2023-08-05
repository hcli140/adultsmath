package net.adultsmath.m1zc3;

import static java.lang.Math.*;
import java.util.Random;

public class VectorRn {
    private final double[] comps;
    private final int spaceR;




    // --------------------------------------------------------------------------------------




    //  CONSTRUCTORS AND GETTERS
    //  construct from an array of values
    public VectorRn (double... values) {
        spaceR = values.length;
        comps = new double[spaceR];
        for (int i = 0; i < spaceR; i++) {
            comps[i] = values[i];
        }
    }

    //  construct a null vector or a vector with random integers in a specified space Rn
    public VectorRn (char mode, int n) {
        spaceR = n;
        comps = new double[spaceR];
        Random random = new Random();
        if (mode == 'r') {
            for (int i = 0; i < n; i++) {
                boolean isNeg = random.nextBoolean();
                if (isNeg) {
                    comps[i] = -random.nextInt(10);
                }
                else {
                    comps[i] = random.nextInt(10);
                }
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                comps[i] = 0;
            }
        }
    }

    public double[] getComps () {
        return comps;
    }
    public double getComps (int n) {
        return comps[n];
    }
    public int getSpaceR () {
        return spaceR;
    }




    // --------------------------------------------------------------------------------------




    //  MANIPULATIONS
    //  make a copy in a specified space Rn ---> remove components or add 0's
    public VectorRn copy (int n) {
        double[] newComps = new double[n];
        for (int i = 0; i < newComps.length; i++) {
            try {
                newComps[i] = comps[i];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                newComps[i] = 0;
            }
        }
        return new VectorRn(newComps);
    }

    //  replace a component
    public VectorRn replace (int n, double a) {
        double[] newComps = new double[spaceR];
        for (int i = 0; i < newComps.length; i++) {
            if (i == n) {
                newComps[i] = a;
            }
            else {
                newComps[i] = comps[i];
            }
        }
        return new VectorRn(newComps);
    }

    //  insert a component at a specified index
    public VectorRn insert (int n, double a) {
        double[] newComps;
        if (n <= spaceR) {
            newComps = new double[spaceR + 1];
            for (int i = 0; i < newComps.length; i++) {
                newComps[i] = comps[i];
            }
        }
        else {
            newComps = new double[n + 1];
            for (int i = 0; i < newComps.length; i++) {
                try {
                    newComps[i] = comps[i];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    newComps[i] = 0;
                }
            }
        }
        newComps[n] = a;
        return new VectorRn(newComps);
    }

    //  get a section of the vector
    public VectorRn section (int lower, int upper) {
        double[] newComps = new double[upper - lower];
        int i = 0;
        for (int j = lower; j < upper; j++) {
            newComps[i] = comps[j];
            i++;
        }
        return new VectorRn(newComps);
    }

    //  remove a component
    public VectorRn remove (int index) {
        double[] newComps = new double[spaceR - 1];
        int i = 0;
        for (int j = 0; j < newComps.length; j++) {
            if (i == index) {
                j--;
            }
            else {
                newComps[j] = comps[i];
            }
            i++;
        }
        return new VectorRn(newComps);
    }

    //  check if the vector is a null vector
    public boolean isNull () {
        for (int i = 0; i < spaceR; i++) {
            if (comps[i] != 0) {
                return false;
            }
        }
        return true;
    }

    //  test if two vectors are identical
    public boolean equalsTo (VectorRn v) {
        if (this.getSpaceR() == v. getSpaceR()) {
            for (int i = 0; i < this.getSpaceR(); i++) {
                if (this.getComps(i) != v.getComps(i)) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }




    // --------------------------------------------------------------------------------------




    //  VECTOR OPERATIONS
    //  norm ||v||
    public double norm () {
        double norm;
        double sumOfSquares = 0;
        for (int i = 0; i < this.getSpaceR(); i++) {
            sumOfSquares += this.getComps(i) * this.getComps(i);
        }
        return norm = sqrt(sumOfSquares);
    }
    public VectorRn unit () {
        double k = 1 / this.norm();
        return Operator.scalMult(k, this);
    }







    //  show the vector as a string
    @Override
    public String toString () {
        String outputString = "V(";
        for (int i = 0; i < spaceR; i++) {
            if (i == spaceR - 1) {
                outputString += comps[i];
            }
            else {
                outputString += comps[i] + ", ";
            }
        }
        outputString += ")";
        return outputString;
    }
}