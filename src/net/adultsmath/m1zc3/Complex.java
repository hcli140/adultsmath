package net.adultsmath.m1zc3;

import static java.lang.Math.*;

public class Complex {
    //  a + bi
    private final double re;
    private final double im;
    public Complex (double a, double b) {
        re = a;
        im = b;
    }

    //  getters
    public double getRe () {
        return re;
    }
    public double getIm () {
        return im;
    }

    @Override
    public String toString () {
        if (re == 0) {
            if (im == 0) {
                return "0";
            }
            else if (im == 1) {
                return "i";
            }
            else if (im == -1) {
                return "-i";
            }
            else {
                return im + "i";
            }
        }
        else {
            if (im == 0) {
                return re + "";
            } else if (im > 0) {
                if (im == 1) {
                    return re + " + i";
                } else {
                    return re + " + " + im + "i";
                }
            } else {
                if (im == -1) {
                    return re + " - i";
                } else {
                    return re + " - " + -im + "i";
                }
            }
        }
    }
    public String toStringEx () {
        if (modulus() == 1) {
            return "e^(i " + princArg() + ")";
        }
        else if (modulus() == -1) {
            return "-e^(i " + princArg() + ")";
        }
        else {
            return modulus() + " e^(i " + princArg() + ")";
        }
    }

    //  does the number have a nonzero imaginary part
    public boolean isReal () {
        return abs(im) < 1E-7;
    }

    //  get the conjugate
    public Complex conj () {
        return new Complex(re,-im);
    }

    //  get the modulus
    public double modulus () {
        return hypot(re,im);
    }

    //  turn number into polar form and get its theta between -pi and pi
    public double princArg () {
        return atan2(im, re);
    }







    //  STATIC METHODS

    //  create a Complex number from an r and theta
    private static Complex toCartesian (double r, double theta) {
        double a = r * cos(theta);
        double b = r * sin(theta);
        return new Complex(a,b);
    }

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
    public static Complex multiply (Complex x, Complex y) {
        double a = x.getRe();
        double b = x.getIm();
        double c = y.getRe();
        double d = y.getIm();
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
        return toCartesian(zR,zTheta);
    }

    //  nth roots
    public static Complex[] nthRoots (int n, Complex z) {
        Complex[] roots = new Complex[n];
        double r = pow(z.modulus(), 1.0/n);
        double theta;
        for (int k = 0; k < n; k++) {
            theta = (z.princArg() + k * 2 * PI) / n;
            roots[k] = toCartesian(r, theta);
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
            roots[k] = toCartesian(r, theta);
        }
        return roots;
    }
}
