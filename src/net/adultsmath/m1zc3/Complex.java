package net.adultsmath.m1zc3;

import static java.lang.Math.*;

public class Complex {
    enum Mode {
        CARTE, POLAR
    }

    //  a + bi
    private final double re;
    private final double im;
    public Complex (double re, double im) {
        this.re = re;
        this.im = im;
    }
    public Complex (double a, double b, Mode mode) {
        switch (mode) {
            case POLAR -> {
                this.re = a * cos(b);
                this.im = a * sin(b);
            }
            default -> {
                this.re = a;
                this.im = b;
            }
        }
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
                return String.valueOf(re);
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

}
