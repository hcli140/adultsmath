package net.adultsmath.m1zc3;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class Operator {
    //  VectorRn
    public static VectorRn add (VectorRn u, VectorRn v) {
        double[] resultComponents;
        if (u.getSpaceR() == v.getSpaceR()) {
            resultComponents = new double[u.getSpaceR()];
            for (int i = 0; i < u.getSpaceR(); i++) {
                resultComponents[i] = u.getComps(i) + v.getComps(i);
            }
            return new VectorRn(resultComponents);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot add vectors of different sizes");
            return null;
        }
    }

    //  subtraction u - v
    public static VectorRn subtract (VectorRn u, VectorRn v) {
        return add(u,neg(v));
    }

    //  scalar multiplication k * v
    public static VectorRn scalMult (double k, VectorRn v) {
        double[] resultComponents = new double[v.getSpaceR()];
        for (int i = 0; i < v.getSpaceR(); i++) {
            resultComponents[i] = k * v.getComps(i);
        }
        return new VectorRn(resultComponents);
    }

    //  negative -v
    public static VectorRn neg (VectorRn v) {
        return scalMult(-1, v);
    }

    //  dot product u dot v
    public static double dot (VectorRn u, VectorRn v) {
        double product = 0;
        if (u.getSpaceR() == v.getSpaceR()) {
            for (int i = 0; i < u.getSpaceR(); i++) {
                product += u.getComps(i) * v.getComps(i);
            }
        }
        else {
            System.out.println("Invalid Dimensions: Cannot dot vectors of different sizes");
        }
        return product;
    }

    //  cross product u x v
    public static VectorRn cross (VectorRn u, VectorRn v) {
        if (u.getSpaceR() == 3 && v.getSpaceR() == 3) {
            double x = u.getComps(1) * v.getComps(2) - u.getComps(2) * v.getComps(1);
            double y = u.getComps(2) * v.getComps(0) - u.getComps(0) * v.getComps(2);
            double z = u.getComps(0) * v.getComps(1) - u.getComps(1) * v.getComps(0);
            return new VectorRn(x, y, z);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot cross vectors not in R3");
            return null;
        }
    }

    //  projection u onto v
    public static VectorRn proj (VectorRn u, VectorRn v) {
        double k = dot(u, v) / pow(v.norm(),2);
        return scalMult(k, v);
    }

    //  component of u orthogonal to v
    public static VectorRn orth (VectorRn u, VectorRn v) {
        return subtract(u, proj(u, v));
    }




    //  Matrix
    //  a + b
    public static Matrix add (Matrix a, Matrix b) {
        if (a.getRows() == b.getRows() && a.getColumns() == b.getColumns()) {
            VectorRn[] newEntries = new VectorRn[a.getRows()];
            for (int i = 0; i < a.getRows(); i++) {
                newEntries[i] = add(a.getRow(i),b.getRow(i));
            }
            return new Matrix(newEntries);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot add matrices of different dimensions");
            return null;
        }
    }

    //  a - b
    public static Matrix subtract (Matrix a, Matrix b) {
        return add(a,scalMult(-1, b));
    }

    //  k * a
    public static Matrix scalMult (double k, Matrix a) {
        VectorRn[] newEntries = new VectorRn[a.getRows()];
        for (int i = 0; i < a.getRows(); i++) {
            newEntries[i] = scalMult(k, a.getRow(i));
        }
        return new Matrix(newEntries);
    }

    //  multiply a * b
    public static Matrix multiply (Matrix a, Matrix b) {
        if (a.getColumns() == b.getRows()) {
            VectorRn[] newEntries = new VectorRn[a.getRows()];
            for (int i = 0; i < a.getRows(); i++) {
                for (int j = 0; j < b.getColumns(); j++) {
                    newEntries[i] = newEntries[i].replace(j, dot(a.getRow(i),b.getColumn(j)));
                }
            }
            return new Matrix(newEntries);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot multiply matrices with these dimensions. The number of columns of the first matrix must match the number of rows of the second matrix");
            return null;
        }
    }



    //  Complex
    //  add
    public static Complex add (Complex x, Complex y) {
        return new Complex(x.getRe() + y.getRe(), x.getIm() + y.getIm());
    }
    public static Complex add (double x, Complex y) {
        return new Complex(x + y.getRe(), y.getIm());
    }

    //  subtract
    public static Complex subtract (Complex x, Complex y) {
        return add(x,scalMult(-1,y));
    }
    public static Complex subtract (double x, Complex y) {
        return add(x,scalMult(-1,y));
    }
    public static Complex subtract (Complex x, double y) {
        return add(-y,x);
    }

    //  multiply by a scalar
    public static Complex scalMult (double k, Complex z) {
        return new Complex(k * z.getRe(), k * z.getIm());
    }

    //  multiply Complex numbers
    public static Complex multiply (Complex c1, Complex c2) {
        double a = c1.getRe();
        double b = c1.getIm();
        double c = c2.getRe();
        double d = c2.getIm();
        return new Complex(a*c-b*d,a*d+b*c);
    }

    //  divide Complex numbers ---> z = x / y
    public static Complex divide (Complex x, Complex y) {
        Complex numer = multiply(x,y.conj());
        double denom = multiply(y,y.conj()).getRe();
        return scalMult(1 / denom, numer);
    }

    //  power ---> w^n = z, n is a positive integer
    public static Complex power (Complex w, int n) {
        double zR = pow(w.modulus(),n);
        double zTheta = n * w.princArg();
        return new Complex(zR,zTheta, Complex.Mode.POLAR);
    }

    //  nth roots
    public static Complex[] nthRoots (int n, Complex z) {
        Complex[] roots = new Complex[n];
        double r = pow(z.modulus(), 1.0/n);
        double theta;
        for (int k = 0; k < n; k++) {
            theta = (z.princArg() + k * 2 * PI) / n;
            roots[k] = new Complex(r, theta, Complex.Mode.POLAR);
        }
        return roots;
    }
    public static Complex[] nthRoots (int n, double a) {
        Complex z = new Complex(a,0);
        Complex[] roots = new Complex[n];
        double r = pow(z.modulus(), 1.0/n);
        double theta;
        for (int k = 0; k < n; k++) {
            theta = (z.princArg() + k * 2 * PI) / n;
            roots[k] = new Complex(r, theta, Complex.Mode.POLAR);
        }
        return roots;
    }






    //  Polynomial
    //  adding two Polynomials
    public static Polynomial add (Polynomial p1, Polynomial p2) {
        double[] sumCoeffs;
        if (p1.getDegree() >= p2.getDegree()) {
            sumCoeffs = new double[p1.getDegree() + 1];
            for (int i = 0; i < p1.getDegree() + 1; i++) {
                try {
                    sumCoeffs[i] = p1.getCoeffs(i) + p2.getCoeffs(i);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    sumCoeffs[i] = p1.getCoeffs(i);
                }
            }
        }
        else {
            sumCoeffs = new double[p2.getDegree() + 1];
            for (int i = 0; i < p2.getDegree() + 1; i++) {
                try {
                    sumCoeffs[i] = p1.getCoeffs(i) + p2.getCoeffs(i);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    sumCoeffs[i] = p2.getCoeffs(i);
                }
            }
        }
        return new Polynomial(sumCoeffs);
    }

    //  get the sum of multiply Polynomials
    public static Polynomial sum (Polynomial... p) {
        Polynomial sum = new Polynomial(0);
        for (int i = 0; i < p.length; i++) {
            sum = add(sum,p[i]);
        }
        return sum;
    }

    //  subtract Polynomials
    public static Polynomial subtract (Polynomial p1, Polynomial p2) {
        return add(p1,scalMult(-1,p2));
    }

    //  multiply a Polynomial by a scalar
    public static Polynomial scalMult (double k, Polynomial p) {
        double[] newCoeffs = new double[p.getDegree() + 1];
        for (int i = 0; i < p.getDegree() + 1; i++) {
            newCoeffs[i] = p.getCoeffs(i) * k;
        }
        return new Polynomial(newCoeffs);
    }

    //  multiplying two Polynomials
    //  ex. (x + 3)(x^2 - 3x + 2) = x(x^2 - 3x + 2) + 3(x^2 - 3x + 2)
    //  {3, 1} * {2, -3, 1} = {6, -9, 3} + {0, 2, -3, 1}
    public static Polynomial multiply (Polynomial p1, Polynomial p2) {
        Polynomial[] pAddends = new Polynomial[p1.getDegree() + 1];
        //  multiply p2 by each term in p1 by iterating through the indices of p1
        for (int i = 0; i < p1.getDegree()+ 1; i++) {
            pAddends[i] = scalMult(p1.getCoeffs(i),p2).xMultiply(i);
        }
        Polynomial pSum = new Polynomial(0);
        //  add the addends together
        for (int j = 0; j < pAddends.length; j++) {
            pSum = add(pSum, pAddends[j]);
        }
        return pSum;
    }

    //  positive integer powers of Polynomials
    public static Polynomial power (Polynomial p, int n) {
        Polynomial result = new Polynomial(1);
        for (int i = 0; i < n; i++) {
            result = multiply(result, p);
        }
        return result;
    }






    //  PolynomialMatrix
    //  addition ---> a + b = c
    public static PolynomialMatrix add (PolynomialMatrix a, PolynomialMatrix b) {
        if (a.getRows() == b.getRows() && a.getColumns() == b.getColumns()) {
            Polynomial[][] cEntries = new Polynomial[a.getRows()][a.getColumns()];
            for (int i = 0; i < a.getRows(); i++) {
                for (int j = 0; j < a.getColumns(); j++) {
                    cEntries[i][j] = add(a.getValue(i,j),b.getValue(i,j));
                }
            }
            return new PolynomialMatrix(cEntries);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot add polynomial matrices of different dimensions");
            return null;
        }
    }

    //  scalar multiplication
    public static PolynomialMatrix scalMult (Polynomial p, PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.getRows()][a.getColumns()];
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                bEntries[i][j] = multiply(p,a.getValue(i,j));
            }
        }
        return new PolynomialMatrix(bEntries);
    }
    public static PolynomialMatrix scalMult (double k, PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.getRows()][a.getColumns()];
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                bEntries[i][j] = multiply(new Polynomial(k),a.getValue(i,j));
            }
        }
        return new PolynomialMatrix(bEntries);
    }
}
