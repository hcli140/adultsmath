package net.adultsmath.m1zc3;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Polynomial {
    /*
    for each Polynomial, its coefficients are stored in coeffs in the order of rising powers
    for example: 1 - 3x + 6x^2 would be stored as {1, -3, 6}
    another example: 7 + 5x^3 would be stored as {7, 0, 0, 3}
     */
    private final double[] coeffs;
    private final int degree;
    public Polynomial (double... coeffs) {
        int trailZeroes = 0;
        for (int i = coeffs.length - 1; i >= 1; i--) {
            if (coeffs[i] != 0) {
                break;
            }
            else {
                trailZeroes++;
            }
        }
        this.coeffs = new double[coeffs.length - trailZeroes];
        degree = this.coeffs.length - 1;
        for (int i = 0; i < coeffs.length - trailZeroes; i++) {
            this.coeffs[i] = coeffs[i];
        }
    }
    public Polynomial () {
        degree = 0;
        coeffs = new double[]{0};
    }

    //  getters
    public double[] getCoeffs () {
        return coeffs;
    }
    public double getCoeffs (int index) {
        return coeffs[index];
    }

    public int getDegree () {
        return degree;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        //  first term
        if (degree >= 2) {
            if (coeffs[degree] != 1 && coeffs[degree] != -1) {
                sb.append(coeffs[degree] + "x^" + degree);
            }
            else if (coeffs[degree] == -1) {
                sb.append("-x^" + degree);
            }
            else {
                sb.append("x^" + degree);
            }
        }
        else if (degree == 1) {
            if (coeffs[degree] != 1 && coeffs[degree] != -1) {
                sb.append(coeffs[degree] + "x");
            }
            else if (coeffs[degree] == -1) {
                sb.append("-x");
            }
            else {
                sb.append("x");
            }
        }
        else {
            sb.append(coeffs[0]);
        }

        //  remaining terms
        for (int i = degree - 1; i >= 0; i--) {
            if (i >= 2) {
                if (coeffs[i] > 0) {
                    if (coeffs[i] != 1) {
                        sb.append(" + " + coeffs[i] + "x^" + i);
                    }
                    else {
                        sb.append(" + " + "x^" + i);
                    }
                }
                else if (coeffs[i] < 0) {
                    if (coeffs[i] != -1) {
                        sb.append(" - " + -coeffs[i] + "x^" + i);
                    }
                    else {
                        sb.append(" - " + "x^" + i);
                    }
                }
            }
            else if (i == 1) {
                if (coeffs[i] > 0) {
                    if (coeffs[i] != 1) {
                        sb.append(" + " + coeffs[i] + "x");
                    }
                    else {
                        sb.append(" + " + "x");
                    }
                }
                else if (coeffs[i] < 0) {
                    if (coeffs[i] != -1) {
                        sb.append(" - " + -coeffs[i] + "x");
                    }
                    else {
                        sb.append(" - " + "x");
                    }
                }
            }
            else {
                if (coeffs[i] > 0) {
                    sb.append(" + " + coeffs[i]);
                }
                else if (coeffs[i] < 0) {
                    sb.append(" - " + -coeffs[i]);
                }
            }
        }
        return sb.toString();
    }


    //  evaluate at x = a
    public double eval (double a) {
        double y = 0;
        for (int i = 0; i < degree + 1; i++) {
            y += coeffs[i] * pow(a,i);
        }
        return y;
    }
    //  first derivative
    public Polynomial deriv () {
        //  new array of coefficients for the derivative polynomial
        double[] dCoeffs = new double[degree];
        for (int i = 1; i < coeffs.length; i++) {
            dCoeffs[i-1] = coeffs[i] * i;
        }
        return new Polynomial(dCoeffs);
    }

    //  nth derivative
    public Polynomial nDeriv (int n) {
        Polynomial deriv = this;
        for (int i = 0; i < n; i++) {
            deriv = deriv.deriv();
        }
        return deriv;
    }







    //  adding two Polynomials
    public static Polynomial add (Polynomial p1, Polynomial p2) {
        double[] sumCoeffs;
        if (p1.degree >= p2.degree) {
            sumCoeffs = new double[p1.degree + 1];
            for (int i = 0; i < p1.degree + 1; i++) {
                try {
                    sumCoeffs[i] = p1.coeffs[i] + p2.coeffs[i];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    sumCoeffs[i] = p1.coeffs[i];
                }
            }
        }
        else {
            sumCoeffs = new double[p2.degree + 1];
            for (int i = 0; i < p2.degree + 1; i++) {
                try {
                    sumCoeffs[i] = p1.coeffs[i] + p2.coeffs[i];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    sumCoeffs[i] = p2.coeffs[i];
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
        double[] newCoeffs = new double[p.degree + 1];
        for (int i = 0; i < p.degree + 1; i++) {
            newCoeffs[i] = p.coeffs[i] * k;
        }
        return new Polynomial(newCoeffs);
    }

    //  multiply a Polynomial by x^n
    //  ex. x^3(6x^2 + 1) = 6x^5 + x^3
    //  {1, 0, 6} --> {0, 0, 0, 1, 0, 6}        (n = 3)
    private Polynomial xMultiply (int n) {
        double[] newCoeffs = new double[degree + 1 + n];
        for (int i = 0; i < newCoeffs.length; i++) {
            if (i < n) {
                newCoeffs[i] = 0;
            }
            else {
                newCoeffs[i] = coeffs[i - n];
            }
        }
        return new Polynomial(newCoeffs);
    }

    //  multiplying two Polynomials
    //  ex. (x + 3)(x^2 - 3x + 2) = x(x^2 - 3x + 2) + 3(x^2 - 3x + 2)
    //  {3, 1} * {2, -3, 1} = {6, -9, 3} + {0, 2, -3, 1}
    public static Polynomial multiply (Polynomial p1, Polynomial p2) {
        Polynomial[] pAddends = new Polynomial[p1.degree + 1];
        //  multiply p2 by each term in p1 by iterating through the indices of p1
        for (int i = 0; i < p1.degree + 1; i++) {
            pAddends[i] = scalMult(p1.coeffs[i],p2).xMultiply(i);
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







    //  SOLVING ROOTS

    //  quadratic equation for real roots
    //  ax^2 + bx + c
    private double[] quadraticMethod () {
        double a = coeffs[2];
        double b = coeffs[1];
        double c = coeffs[0];
        double discriminant = b*b - 4 * a * c;
        double[] roots;
        if (discriminant > 0) {
            roots = new double[2];
            roots[0] = (-b + sqrt(discriminant)) / (2 * a);
            roots[1] = (-b - sqrt(discriminant)) / (2 * a);
        }
        else if (discriminant == 0) {
            roots = new double[1];
            roots[0] = -b / (2 * a);
        }
        else {
            roots = null;
        }
        return roots;
    }

    //  Cardano's method for finding real roots of cubic equations
    private double[] cardanosMethod () {

        double a = coeffs[3];
        double b = coeffs[2];
        double c = coeffs[1];
        double d = coeffs[0];

        double q = (3*a*c - b*b) / (9*a*a);
        double r = (9*a*b*c - 27*a*a*d - 2*pow(b,3)) / (54*pow(a,3));
        double discriminant = pow(q,3) + pow(r,2);
        //  pick only one of the square roots
        Complex sqrtDiscr = Complex.nthRoots(2,discriminant)[0];

        Complex[] s = Complex.nthRoots(3,Complex.add(r,sqrtDiscr));
        Complex[] t = Complex.nthRoots(3,Complex.subtract(r,sqrtDiscr));

        ArrayList<Complex> rootsC = new ArrayList<Complex>();
        Complex x1;
        Complex x2;
        Complex x3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                //  x1
                x1 = Complex.add(-b/(3*a),Complex.add(s[i], t[j]));
                //System.out.println("x1 = " + x1);
                if (x1.isReal()) {
                    rootsC.add(x1);
                }

                //  x2
                Complex x2_1 = Complex.add(-b/(3*a),Complex.scalMult(-0.5,Complex.add(s[i],t[i])));
                Complex x2_2 = Complex.multiply(new Complex(0,sqrt(3)/2),Complex.subtract(s[i],t[i]));
                x2 = Complex.add(x2_1,x2_2);
                //System.out.println("x2 = " + x2);
                if (x2.isReal()) {
                    rootsC.add(x2);
                }

                //  x3
                x3 = Complex.subtract(x2_1,x2_2);
                //System.out.println("x3 = " + x3 + "\n");
                if (x3.isReal()) {
                    rootsC.add(x3);
                }
            }
        }

        //  remove imaginary parts
        for (int i = 0; i < rootsC.size(); i++) {
            rootsC.set(i, new Complex(rootsC.get(i).getRe(),0));
        }
        //System.out.println(rootsC);

        //  remove duplicate roots
        if (discriminant > 1E-7) {
            return new double[]{rootsC.get(0).getRe()};
        }
        else {
            ArrayList<Complex> rootsCFinal = new ArrayList<Complex>();
            rootsCFinal.add(rootsC.get(0));
            int k = 1;
            for (int i = 1; i < rootsC.size() && k < 3; i++) {
                for (int j = 0; j < i; j++) {
                    //System.out.println("i = " + i + " | j = " + j + " | k = " + k);
                    try {
                        if (abs(rootsC.get(i).getRe() - rootsCFinal.get(j).getRe()) < 1E-10) {
                            //System.out.println(rootsC.get(i).getReal() + " = " + rootsCFinal.get(j).getReal() + "\n");
                            break;
                        }
                        if (i - 1 == j) {
                            //System.out.println(rootsC.get(i).getReal());
                            rootsCFinal.add(rootsC.get(i));
                            k++;
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        break;
                    }
                    //System.out.println("");
                }
            }
            double[] roots = new double[rootsCFinal.size()];
            for (int i = 0; i < rootsCFinal.size(); i++) {
                roots[i] = rootsCFinal.get(i).getRe();
            }
            return roots;
        }
    }

    //  Ferrari's method for finding real roots of quartic equations
    private double[] ferrarisMethod () {
        //  https://mathworld.wolfram.com/QuarticEquation.html
        //  leading coefficient is removed by dividing the entire expression by it
        //  quartic equation becomes y^4 + ay^3 + by^2 + cy + d = 0
        Polynomial poly = scalMult(1/coeffs[degree],this);
        double a = poly.coeffs[3];
        double b = poly.coeffs[2];
        double c = poly.coeffs[1];
        double d = poly.coeffs[0];

        //  depressing the equation and removing its x^3 term
        //  substitute y = x - a/4
        //  reduced to x^4 + px^2 + qx + r = 0
        double p = b - (3.0/8.0)*pow(a,2);
        double q = c - (1.0/2.0)*a*b + (1.0/8.0)*pow(a,3);
        double r = d - (1.0/4.0)*a*c + (1.0/16.0)*pow(a,2)*b - (3.0/256.0)*pow(a,4);

        //  solve the resolvent cubic for one of its roots, u1
        Polynomial rCubic = new Polynomial(4*p*r - q*q, -4*r, -p, 1);
        double u1 = rCubic.cardanosMethod()[0];

        //  polynomials P and Q
        //  let pP = P and pQ = Q
        Polynomial pP = new Polynomial((1.0/2.0)*u1,0,1);
        double A = sqrt(u1 - p);
        Polynomial pQ = new Polynomial(-q/(2*A),A);

        //  P is quadratic in x and Q is linear in x
        //  P - Q and P + Q are both quadratic and their roots are the roots of the original quartic
        Polynomial pPQ1 = add(pP,scalMult(-1,pQ));
        Polynomial pPQ2 = add(pP,pQ);
        double[] r1 = pPQ1.quadraticMethod();
        double[] r2 = pPQ2.quadraticMethod();
        int r1Length;
        int r2Length;
        try {
            r1Length = r1.length;
        }
        catch (NullPointerException e) {
            r1Length = 0;
        }
        try {
            r2Length = r2.length;
        }
        catch (NullPointerException e) {
            r2Length = 0;
        }

        double[] roots = new double[r1Length + r2Length];
        for (int i = 0; i < r1Length; i++) {
            roots[i] = r1[i];
        }
        for (int i = 0; i < r2Length; i++) {
            roots[i + r1Length] = r2[i];
        }
        for (int i = 0; i < roots.length; i++) {
            roots[i] += -(1.0/4)*a;
        }



        return roots;
    }

    //  find roots
    public double[] roots () {
        if (degree == 0) {
            return null;
        }
        else if (degree == 1) {
            return new double[]{-coeffs[0] / coeffs[1]};
        }
        else if (degree == 2) {
            return quadraticMethod();
        }
        else if (degree == 3) {
            return cardanosMethod();
        }
        else if (degree == 4) {
            return ferrarisMethod();
        }
        else {
            return new double[0];
        }
    }


}
