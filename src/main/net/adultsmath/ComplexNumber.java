package main.net.adultsmath;

import java.util.List;

import static java.lang.Math.*;

public record ComplexNumber(double re, double im) {

    public ComplexNumber(double re) {
        this(re, 0);
    }

    public ComplexNumber(List<Double> val) {
        this(val.get(0) * cos(val.get(1))
                , val.get(0) * sin(val.get(1)));
    }

    //  getters
    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }


    public boolean equals(ComplexNumber z) {
        return this.re == z.re && this.im == z.im;
    }

    public String toStringEx() {
        if (modulus() == 1) {
            return "e^(i " + princArg() + ")";
        } else if (modulus() == -1) {
            return "-e^(i " + princArg() + ")";
        } else {
            return modulus() + " e^(i " + princArg() + ")";
        }
    }

    //  does the number have a nonzero imaginary part
    public boolean isReal() {
        return abs(im) < 1E-5;
    }

    //  get the conjugate
    public ComplexNumber conj() {
        return new ComplexNumber(re, -im);
    }

    //  get the modulus
    public double modulus() {
        return hypot(re, im);
    }

    //  turn number into polar form and get its theta between -pi and pi
    public double princArg() {
        return atan2(im, re);
    }


    @Override
    public String toString() {
        if (re == 0) {
            if (im == 0) {
                return "0";
            } else if (im == 1) {
                return "i";
            } else if (im == -1) {
                return "-i";
            } else {
                return im + "i";
            }
        } else {
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

}
