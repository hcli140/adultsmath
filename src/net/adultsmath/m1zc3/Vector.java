package net.adultsmath.m1zc3;

import static java.lang.Math.*;
import java.util.Random;

public class Vector {
    private final double[] comps;
    private final int spaceR;




    // --------------------------------------------------------------------------------------




    //  CONSTRUCTORS AND GETTERS
    //  construct from an array of values
    public Vector (double... values) {
        spaceR = values.length;
        comps = new double[spaceR];
        for (int i = 0; i < spaceR; i++) {
            comps[i] = values[i];
        }
    }

    //  construct a null vector or a vector with random integers in a specified space Rn
    public Vector (char mode, int n) {
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
    public Vector copy (int n) {
        double[] newComps = new double[n];
        for (int i = 0; i < newComps.length; i++) {
            try {
                newComps[i] = comps[i];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                newComps[i] = 0;
            }
        }
        return new Vector(newComps);
    }

    //  replace a component
    public Vector replace (int n, double a) {
        double[] newComps = new double[spaceR];
        for (int i = 0; i < newComps.length; i++) {
            if (i == n) {
                newComps[i] = a;
            }
            else {
                newComps[i] = comps[i];
            }
        }
        return new Vector(newComps);
    }

    //  insert a component at a specified index
    public Vector insert (int n, double a) {
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
        return new Vector(newComps);
    }

    //  get a section of the vector
    public Vector section (int lower, int upper) {
        double[] newComps = new double[upper - lower];
        int i = 0;
        for (int j = lower; j < upper; j++) {
            newComps[i] = comps[j];
            i++;
        }
        return new Vector(newComps);
    }

    //  remove a component
    public Vector remove (int index) {
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
        return new Vector(newComps);
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




    // --------------------------------------------------------------------------------------




    //  VECTOR OPERATIONS
    //  norm ||v||
    public static double norm (Vector v) {
        double norm;
        double sumOfSquares = 0;
        for (int i = 0; i < v.spaceR; i++) {
            sumOfSquares += v.comps[i] * v.comps[i];
        }
        return norm = sqrt(sumOfSquares);
    }

    //  addition u + v
    public static Vector add (Vector u, Vector v) {
        double[] resultComponents;
        if (u.spaceR == v.spaceR) {
            resultComponents = new double[u.spaceR];
            for (int i = 0; i < u.spaceR; i++) {
                resultComponents[i] = u.comps[i] + v.comps[i];
            }
            return new Vector(resultComponents);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot add vectors of different sizes");
            return null;
        }
    }

    //  subtraction u - v
    public static Vector subtract (Vector u, Vector v) {
        return add(u,neg(v));
    }

    //  scalar multiplication k * v
    public static Vector scalMult (double k, Vector v) {
        double[] resultComponents = new double[v.spaceR];
        for (int i = 0; i < v.spaceR; i++) {
            resultComponents[i] = k * v.comps[i];
        }
        return new Vector(resultComponents);
    }

    //  negative -v
    public static Vector neg (Vector v) {
        return scalMult(-1, v);
    }

    //  dot product u dot v
    public static double dot (Vector u, Vector v) {
        double product = 0;
        if (u.spaceR == v.spaceR) {
            for (int i = 0; i < u.spaceR; i++) {
                product += u.comps[i] * v.comps[i];
            }
        }
        else {
            System.out.println("Invalid Dimensions: Cannot dot vectors of different sizes");
        }
        return product;
    }

    //  cross product u x v
    public static Vector cross (Vector u, Vector v) {
        if (u.spaceR == 3 && v.spaceR == 3) {
            double x = u.comps[1] * v.comps[2] - u.comps[2] * v.comps[1];
            double y = u.comps[2] * v.comps[0] - u.comps[0] * v.comps[2];
            double z = u.comps[0] * v.comps[1] - u.comps[1] * v.comps[0];
            return new Vector(x, y, z);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot cross vectors not in R3");
            return null;
        }
    }

    //  projection u onto v
    public static Vector proj (Vector u, Vector v) {
        double k = dot(u, v) / pow(norm(v),2);
        return scalMult(k, v);
    }

    //  component of u orthogonal to v
    public static Vector orth (Vector u, Vector v) {
        return subtract(u, proj(u, v));
    }

    //  unit vector
    public static Vector unit (Vector v) {
        double k = 1 / norm(v);
        return scalMult(k, v);
    }

    //  test if two vectors are identical
    public static boolean isIdentical (Vector u, Vector v) {
        if (u.spaceR == v. spaceR) {
            for (int i = 0; i < u.spaceR; i++) {
                if (u.comps[i] != v.comps[i]) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    //  show the vector as a string
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