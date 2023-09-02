package main.net.adultsmath.m1zc3;

import main.net.adultsmath.ComplexNumber;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class Operator {
    //  VectorRn
    public static Vector add(Vector u, Vector v) {
        double[] resultComponents;
        if (u.getSize() == v.getSize()) {
            resultComponents = new double[u.getSize()];
            for (int i = 0; i < u.getSize(); i++) {
                resultComponents[i] = u.getComps(i) + v.getComps(i);
            }
            return new Vector(resultComponents);
        } else {
            throw new InvalidDimensionsException("Cannot add vectors of different sizes");
        }
    }

    //  subtraction u - v
    public static Vector subtract(Vector u, Vector v) {
        return add(u, neg(v));
    }

    //  scalar multiplication k * v
    public static Vector scalMult(double k, Vector v) {
        double[] resultComponents = new double[v.getSize()];
        for (int i = 0; i < v.getSize(); i++) {
            resultComponents[i] = k * v.getComps(i);
        }
        return new Vector(resultComponents);
    }

    //  negative -v
    public static Vector neg(Vector v) {
        return scalMult(-1, v);
    }

    //  dot product u dot v
    public static double dot(Vector u, Vector v) {
        double product = 0;
        if (u.getSize() == v.getSize()) {
            for (int i = 0; i < u.getSize(); i++) {
                product += u.getComps(i) * v.getComps(i);
            }
            return product;
        } else {
            throw new InvalidDimensionsException("Cannot dot vectors of different sizes");
        }
    }

    //  cross product u x v
    public static Vector cross(Vector u, Vector v) {
        if (u.getSize() == 3 && v.getSize() == 3) {
            double x = u.getComps(1) * v.getComps(2) - u.getComps(2) * v.getComps(1);
            double y = u.getComps(2) * v.getComps(0) - u.getComps(0) * v.getComps(2);
            double z = u.getComps(0) * v.getComps(1) - u.getComps(1) * v.getComps(0);
            return new Vector(x, y, z);
        } else {
            throw new InvalidDimensionsException("Cannot cross vectors not in R3");
        }
    }

    //  projection u onto v
    public static Vector proj(Vector u, Vector v) {
        double k = dot(u, v) / pow(v.getNorm(), 2);
        return scalMult(k, v);
    }

    //  component of u orthogonal to v
    public static Vector orth(Vector u, Vector v) {
        return subtract(u, proj(u, v));
    }


    //  Matrix
    //  a + b
    public static Matrix add(Matrix a, Matrix b) {
        if (a.getNumRows() == b.getNumRows() && a.getNumColumns() == b.getNumColumns()) {
            Vector[] newEntries = new Vector[a.getNumRows()];
            for (int i = 0; i < a.getNumRows(); i++) {
                newEntries[i] = add(a.getRow(i), b.getRow(i));
            }
            return new Matrix(newEntries);
        } else {
            throw new InvalidDimensionsException("Cannot add matrices of different dimensions");
        }
    }

    //  a - b
    public static Matrix subtract(Matrix a, Matrix b) {
        return add(a, scalMult(-1, b));
    }

    //  k * a
    public static Matrix scalMult(double k, Matrix a) {
        Vector[] newEntries = new Vector[a.getNumRows()];
        for (int i = 0; i < a.getNumRows(); i++) {
            newEntries[i] = scalMult(k, a.getRow(i));
        }
        return new Matrix(newEntries);
    }

    //  multiply a * b
    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.getNumColumns() == b.getNumRows()) {
            Vector[] newEntries = new Vector[a.getNumRows()];
            for (int i = 0; i < a.getNumRows(); i++) {
                for (int j = 0; j < b.getNumColumns(); j++) {
                    newEntries[i] = newEntries[i].getReplace(j, dot(a.getRow(i), b.getColumn(j)));
                }
            }
            return new Matrix(newEntries);
        } else {
            throw new InvalidDimensionsException("Cannot multiply matrices with these dimensions. The number of columns of the first matrix must match the number of rows of the second matrix");
        }
    }


    //  Complex
    //  add
    public static ComplexNumber add(ComplexNumber x, ComplexNumber y) {
        return new ComplexNumber(x.getRe() + y.getRe(), x.getIm() + y.getIm());
    }

    public static ComplexNumber add(double x, ComplexNumber y) {
        return new ComplexNumber(x + y.getRe(), y.getIm());
    }

    //  subtract
    public static ComplexNumber subtract(ComplexNumber x, ComplexNumber y) {
        return add(x, scalMult(-1, y));
    }

    public static ComplexNumber subtract(double x, ComplexNumber y) {
        return add(x, scalMult(-1, y));
    }

    public static ComplexNumber subtract(ComplexNumber x, double y) {
        return add(-y, x);
    }

    //  multiply by a scalar
    public static ComplexNumber scalMult(double k, ComplexNumber z) {
        return new ComplexNumber(k * z.getRe(), k * z.getIm());
    }

    //  multiply Complex numbers
    public static ComplexNumber multiply(ComplexNumber c1, ComplexNumber c2) {
        double a = c1.getRe();
        double b = c1.getIm();
        double c = c2.getRe();
        double d = c2.getIm();
        return new ComplexNumber(a * c - b * d, a * d + b * c);
    }

    //  divide Complex numbers ---> z = x / y
    public static ComplexNumber divide(ComplexNumber x, ComplexNumber y) {
        ComplexNumber numer = multiply(x, y.conj());
        double denom = multiply(y, y.conj()).getRe();
        return scalMult(1 / denom, numer);
    }

    //  power ---> w^n = z, n is a positive integer
    public static ComplexNumber power(ComplexNumber w, int n) {
        double zR = pow(w.modulus(), n);
        double zTheta = n * w.princArg();
        return new ComplexNumber(List.of(zR, zTheta));
    }

    //  nth rootsMap
    public static List<ComplexNumber> nthRoots(int n, ComplexNumber z) {
        List<ComplexNumber> roots = new ArrayList<>();
        double r = pow(z.modulus(), 1.0 / n);
        double theta;
        for (int k = 0; k < n; k++) {
            theta = (z.princArg() + k * 2 * PI) / n;
            roots.add(new ComplexNumber(List.of(r, theta)));
        }
        return roots;
    }

    public static List<ComplexNumber> nthRoots(int n, double a) {
        ComplexNumber z = new ComplexNumber(a, 0);
        List<ComplexNumber> roots = new ArrayList<>();
        double r = pow(z.modulus(), 1.0 / n);
        double theta;
        for (int k = 0; k < n; k++) {
            theta = (z.princArg() + k * 2 * PI) / n;
            roots.add(new ComplexNumber(List.of(r, theta)));
        }
        return roots;
    }


    //  Polynomial
    //  adding two Polynomials
    private static Polynomial addTwo(Polynomial p1, Polynomial p2) {
        List<Double> sumCoeffs = new ArrayList<>();
        if (p1.getDegree() >= p2.getDegree()) {
            for (int i = 0; i < p1.getCoeffs().size(); i++) {
                try {
                    sumCoeffs.add(p1.getCoeffs(i) + p2.getCoeffs(i));
                } catch (IndexOutOfBoundsException e) {
                    sumCoeffs.add(p1.getCoeffs(i));
                }
                if (i < p2.getCoeffs().size()) {

                }
            }
        } else {
            for (int i = 0; i < p2.getDegree() + 1; i++) {
                try {
                    sumCoeffs.add(p1.getCoeffs(i) + p2.getCoeffs(i));
                } catch (IndexOutOfBoundsException e) {
                    sumCoeffs.add(p2.getCoeffs(i));
                }
            }
        }
        return new Polynomial(sumCoeffs);
    }

    public static Polynomial add(Polynomial... polynomials) {
        Polynomial sum = new Polynomial(0);
        for (int i = 0; i < polynomials.length; i++) {
            sum = addTwo(sum, polynomials[i]);
        }
        return sum;
    }

    //  subtract Polynomials
    public static Polynomial subtract(Polynomial p1, Polynomial p2) {
        return add(p1, scalMult(-1, p2));
    }

    //  multiply a Polynomial by a scalar
    public static Polynomial scalMult(double k, Polynomial p) {
        double[] newCoeffs = new double[p.getDegree() + 1];
        for (int i = 0; i < p.getDegree() + 1; i++) {
            newCoeffs[i] = p.getCoeffs(i) * k;
        }
        return new Polynomial(newCoeffs);
    }

    //  multiplying two Polynomials
    //  ex. (x + 3)(x^2 - 3x + 2) = x(x^2 - 3x + 2) + 3(x^2 - 3x + 2)
    //  {3, 1} * {2, -3, 1} = {6, -9, 3} + {0, 2, -3, 1}
    private static Polynomial multiplyTwo(Polynomial p1, Polynomial p2) {
        Polynomial[] pAddends = new Polynomial[p1.getDegree() + 1];
        //  multiply p2 by each term in p1 by iterating through the indices of p1
        for (int i = 0; i < p1.getDegree() + 1; i++) {
            pAddends[i] = scalMult(p1.getCoeffs(i), p2).getIncreasedDegree(i);
        }
        Polynomial pSum = new Polynomial(0);
        //  add the addends together
        for (int j = 0; j < pAddends.length; j++) {
            pSum = add(pSum, pAddends[j]);
        }
        return pSum;
    }

    public static Polynomial multiply(Polynomial... polynomials) {
        Polynomial product = new Polynomial(1);
        for (Polynomial polynomial : polynomials) {
            product = multiplyTwo(polynomial, product);
        }
        return product;
    }

    //  positive integer powers of Polynomials
    public static Polynomial power(Polynomial p, int n) {
        Polynomial result = new Polynomial(1);
        for (int i = 0; i < n; i++) {
            result = multiply(result, p);
        }
        return result;
    }


    //  PolynomialMatrix
    //  addition ---> a + b = c
    public static PolynomialMatrix add(PolynomialMatrix a, PolynomialMatrix b) {
        if (a.getRows() == b.getRows() && a.getColumns() == b.getColumns()) {
            Polynomial[][] cEntries = new Polynomial[a.getRows()][a.getColumns()];
            for (int i = 0; i < a.getRows(); i++) {
                for (int j = 0; j < a.getColumns(); j++) {
                    cEntries[i][j] = add(a.getValue(i, j), b.getValue(i, j));
                }
            }
            return new PolynomialMatrix(cEntries);
        } else {
            throw new InvalidDimensionsException("Cannot add polynomial matrices of different dimensions");
        }
    }

    //  scalar multiplication
    public static PolynomialMatrix scalMult(Polynomial p, PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.getRows()][a.getColumns()];
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                bEntries[i][j] = multiply(p, a.getValue(i, j));
            }
        }
        return new PolynomialMatrix(bEntries);
    }

    public static PolynomialMatrix scalMult(double k, PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.getRows()][a.getColumns()];
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                bEntries[i][j] = multiply(new Polynomial(k), a.getValue(i, j));
            }
        }
        return new PolynomialMatrix(bEntries);
    }
}
