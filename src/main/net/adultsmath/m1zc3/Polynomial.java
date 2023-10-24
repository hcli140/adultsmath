package main.net.adultsmath.m1zc3;

import main.net.adultsmath.ComplexNumber;

import java.util.*;

import static java.lang.Math.*;

public class Polynomial {
    /*
    for each Polynomial, its coefficients are stored in coeffs in the order of rising powers
    for example: 1 - 3x + 6x^2 would be stored as {1, -3, 6}
    another example: 7 + 5x^3 would be stored as {7, 0, 0, 3}
     */
    private final List<Double> coeffs;
    private final int degree;

    public Polynomial(double... coeffs) {

        List<Double> inputs = new ArrayList<>();
        for (double coeff : coeffs) {
            inputs.add(coeff);
        }

        inputs = removeTrailingZeroesFromInput(inputs);
        this.coeffs = new ArrayList<>(inputs);
        this.degree = inputs.size() - 1;
    }

    public Polynomial(List<Double> coeffs) {
        coeffs = removeTrailingZeroesFromInput(coeffs);
        this.coeffs = new ArrayList<>(coeffs);
        this.degree = coeffs.size() - 1;
    }

    public Polynomial() {
        this.degree = 0;
        this.coeffs = new ArrayList<>();
        coeffs.add(0.0);
    }

    public static Polynomial createFromLinearFactors(double... roots) {
        List<Polynomial> factors = new ArrayList<>();
        for (double root : roots) {
            factors.add(new Polynomial(-root, 1));
        }
        return Operator.multiply(factors.toArray(new Polynomial[0]));
    }

    public static Polynomial createFromLinearFactors(List<Double> roots) {
        List<Polynomial> factors = new ArrayList<>();
        for (double root : roots) {
            factors.add(new Polynomial(-root, 1));
        }
        return Operator.multiply(factors.toArray(new Polynomial[0]));
    }


    //  getters
    public List<Double> getCoeffs() {
        return coeffs;
    }

    public double getCoeffs(int index) {
        return getCoeffs().get(index);
    }

    public int getDegree() {
        return degree;
    }


    //  evaluate at x = a
    public double getValueEvaluatedAt(double a) {
        double y = 0;
        for (int i = 0; i < degree + 1; i++) {
            y += this.getCoeffs(i) * pow(a, i);
        }
        return y;
    }

    //  first derivative
    public Polynomial getDerivative() {
        //  new array of coefficients for the derivative polynomial
        double[] dCoeffs = new double[degree];
        for (int i = 1; i < degree + 1; i++) {
            dCoeffs[i - 1] = this.getCoeffs(i) * i;
        }
        return new Polynomial(dCoeffs);
    }

    //  nth derivative
    public Polynomial getNthDerivative(int n) {
        Polynomial deriv = this;
        for (int i = 0; i < n; i++) {
            deriv = deriv.getDerivative();
        }
        return deriv;
    }

    //  multiply a Polynomial by x^n
    //  ex. x^3(6x^2 + 1) = 6x^5 + x^3
    //  {1, 0, 6} --> {0, 0, 0, 1, 0, 6}        (n = 3)
    public Polynomial getIncreasedDegree(int n) {
        double[] newCoeffs = new double[degree + 1 + n];
        for (int i = 0; i < newCoeffs.length; i++) {
            if (i < n) {
                newCoeffs[i] = 0;
            } else {
                newCoeffs[i] = this.getCoeffs(i - n);
            }
        }
        return new Polynomial(newCoeffs);
    }

    public boolean hasOnlyPositiveCoeffs() {
        for (double coeff : this.getCoeffs()) {
            if (((float) coeff) < -1E-7) return false;
        }
        return true;
    }


    //  SOLVING ROOTS

    //  quadratic equation for real roots
    //  ax^2 + bx + c

    private PolynomialRoots linearMethod() {
        if (this.degree != 1)
            throw new InvalidDegreeException("This method is for linear polynomials only");
        PolynomialRoots root = new PolynomialRoots();
        root.addRoot(-this.getCoeffs(0) / this.getCoeffs(1));
        return root;
    }

    private PolynomialRoots quadraticMethod() {
        if (this.degree != 2)
            throw new InvalidDegreeException("This method is for quadratic polynomials only");
        double a = this.getCoeffs(2);
        double b = this.getCoeffs(1);
        double c = this.getCoeffs(0);
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return new PolynomialRoots();
        PolynomialRoots roots = new PolynomialRoots();
        roots.addRoot((-b + sqrt(discriminant)) / (2 * a));
        roots.addRoot((-b - sqrt(discriminant)) / (2 * a));
        return roots;
    }

    //  Cardano's method for finding real roots of cubic equations
    private PolynomialRoots cardanosMethod() {

        //  https://proofwiki.org/wiki/Cardano%27s_Formula

        if (this.degree != 3)
            throw new InvalidDegreeException("This method is for cubic polynomials only");

        double a = this.getCoeffs(3);
        double b = this.getCoeffs(2);
        double c = this.getCoeffs(1);
        double d = this.getCoeffs(0);

        double Q = (3 * a * c - b * b) / (9 * a * a);
        double R = (9 * a * b * c - 27 * a * a * d - 2 * pow(b, 3)) / (54 * pow(a, 3));
        double discriminant = pow(Q, 3) + pow(R, 2);

//        System.out.println("Discriminant: " + discriminant);

        ComplexNumber sqrtDiscr = Operator.nthRoots(2, discriminant).get(0);

        List<ComplexNumber> S = Operator.nthRoots(3, Operator.add(R, sqrtDiscr));
        List<ComplexNumber> T = Operator.nthRoots(3, Operator.subtract(R, sqrtDiscr));

        PolynomialRoots roots = new PolynomialRoots();

        ComplexNumber root;

        for (ComplexNumber s : S) {
            for (ComplexNumber t : T) {
                root = Operator.subtract(Operator.add(s, t), b / (3 * a));
                if (root.isReal()) {
//                    System.out.println("Root: " + root);
                    roots.addRoot(((Double) root.getRe()).floatValue());
                }
            }
        }
//        System.out.println(roots);

        if (roots.getNumRoots() == 1 && roots.getRoot().getValue() == 9) {
            PolynomialRoots tempRoots = new PolynomialRoots();
            tempRoots.put(roots.getRoot().getKey(), 3);
            roots = PolynomialRoots.copyOf(tempRoots);
        }
        else if (discriminant < -1E-5) {
            for (double r : roots.getRootsList()) {
                roots.put(r, 1);
            }
        }

        return roots;
    }

    //  Ferrari's method for finding real roots of quartic equations
    private PolynomialRoots ferrarisMethod() {

        //  https://mathworld.wolfram.com/QuarticEquation.html

        if (this.degree != 4)
            throw new InvalidDegreeException("This method is for quartic polynomials only");

        //  leading coefficient is removed by dividing the entire expression by it
        //  quartic equation becomes y^4 + ay^3 + by^2 + cy + D = 0
        Polynomial poly = Operator.scalMult(1 / this.getCoeffs(degree), this);
        double a3 = poly.getCoeffs(3);
        double a2 = poly.getCoeffs(2);
        double a1 = poly.getCoeffs(1);
        double a0 = poly.getCoeffs(0);

        //  depressing the equation and removing its x^3 term
        //  substitute y = x - a/4
        //  reduced to x^4 + px^2 + qx + r = 0
        double p = a2
                - (3.0 / 8.0) * pow(a3, 2);
        double q = a1
                - (1.0 / 2.0) * a2 * a3
                + (1.0 / 8.0) * pow(a3, 3);
        double r = a0
                - (1.0 / 4.0) * a1 * a3
                + (1.0 / 16.0) * a2 * pow(a3, 2)
                - (3.0 / 256.0) * pow(a3, 4);
//        System.out.println("p = " + p + " | q = " + q + " | r = " + r);

        //  solve the resolvent cubic for one of its roots, u1
        PolynomialRoots u = new Polynomial(4 * p * r - q * q, -4 * r, -p, 1).cardanosMethod();
        Double u0 = null;
        for (double root : u.getRootsSet()) {
            if (root != p) {
                u0 = root;
                break;
            }
        }

        PolynomialRoots roots = new PolynomialRoots();

        if (u0 == null) {
            if (this.hasOnlyPositiveCoeffs()) roots.addRoot(
                        -abs(Operator.nthRoots(4, this.getCoeffs(0))
                                .get(0).getRe()), 4);
            else roots.addRoot(
                    abs(Operator.nthRoots(4, this.getCoeffs(0))
                            .get(0).getRe()), 4);
            return roots;
        }

//        System.out.println("u0 = " + u0);

        //  polynomials P and Q
        Polynomial P = new Polynomial((1.0 / 2.0) * u0, 0, 1);
        double A = sqrt(u0 - p);
        if (Double.isNaN(A)) return new PolynomialRoots();
        Polynomial Q = new Polynomial(-q / (2 * A), A);

//        System.out.println(P + "\n" + Q);

        //  P is quadratic in x and Q is linear in x
        //  P - Q and P + Q are both quadratic and their roots are the roots of the original quartic

        List<PolynomialRoots> rootsList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0 -> {rootsList.add(Operator.subtract(P, Q).quadraticMethod());}
                case 1 -> {rootsList.add(Operator.add(P, Q).quadraticMethod());}
            }
        }
//        System.out.println("Roots list: " + rootsList);
        return ferrarisReturnRootsToOriginalEquation(
                new Polynomial(-0.25 * a3, 1),
                PolynomialRoots.combine(rootsList));
    }

    //  find roots
    public PolynomialRoots getRoots() {
        switch (this.getDegree()) {
            case 0 -> {return new PolynomialRoots();}
            case 1 -> {return this.linearMethod();}
            case 2 -> {return this.quadraticMethod();}
            case 3 -> {return this.cardanosMethod();}
            case 4 -> {return this.ferrarisMethod();}
            default -> throw new InvalidDimensionsException(
                    "Only polynomials with degrees between and including 0 and 5 can be calculated");
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //  first term
        if (degree >= 2) {
            if (this.getCoeffs(degree) != 1 && this.getCoeffs(degree) != -1) {
                sb.append(this.getCoeffs(degree) + "x^" + degree);
            } else if (this.getCoeffs(degree) == -1) {
                sb.append("-x^" + degree);
            } else {
                sb.append("x^" + degree);
            }
        } else if (degree == 1) {
            if (this.getCoeffs(degree) != 1 && this.getCoeffs(degree) != -1) {
                sb.append(this.getCoeffs(degree) + "x");
            } else if (this.getCoeffs(degree) == -1) {
                sb.append("-x");
            } else {
                sb.append("x");
            }
        } else {
            sb.append(this.getCoeffs(0));
        }

        //  remaining terms
        for (int i = degree - 1; i >= 0; i--) {
            if (i >= 2) {
                if (this.getCoeffs(i) > 0) {
                    if (this.getCoeffs(i) != 1) {
                        sb.append(" + " + this.getCoeffs(i) + "x^" + i);
                    } else {
                        sb.append(" + " + "x^" + i);
                    }
                } else if (this.getCoeffs(i) < 0) {
                    if (this.getCoeffs(i) != -1) {
                        sb.append(" - " + -this.getCoeffs(i) + "x^" + i);
                    } else {
                        sb.append(" - " + "x^" + i);
                    }
                }
            } else if (i == 1) {
                if (this.getCoeffs(i) > 0) {
                    if (this.getCoeffs(i) != 1) {
                        sb.append(" + " + this.getCoeffs(i) + "x");
                    } else {
                        sb.append(" + " + "x");
                    }
                } else if (this.getCoeffs(i) < 0) {
                    if (this.getCoeffs(i) != -1) {
                        sb.append(" - " + -this.getCoeffs(i) + "x");
                    } else {
                        sb.append(" - " + "x");
                    }
                }
            } else {
                if (this.getCoeffs(i) > 0) {
                    sb.append(" + " + this.getCoeffs(i));
                } else if (this.getCoeffs(i) < 0) {
                    sb.append(" - " + -this.getCoeffs(i));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Polynomial polynomial) return this.getCoeffs().equals(polynomial.getCoeffs());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coeffs, degree);
    }

    private static class InvalidDegreeException extends RuntimeException {
        public InvalidDegreeException() {
            super();
        }

        public InvalidDegreeException(String message) {
            super(message);
        }
    }

    private List<Double> removeTrailingZeroesFromInput(List<Double> input) {
        int trailZeroes = 0;
        for (int i = input.size() - 1; i >= 1; i--) {
            if (input.get(i) != 0) break;
            else trailZeroes++;
        }
        List<Double> trimmedInput = new ArrayList<>();
        for (int i = 0; i < input.size() - trailZeroes; i++) {
            trimmedInput.add(input.get(i));
        }
        return trimmedInput;
    }

    private PolynomialRoots ferrarisReturnRootsToOriginalEquation(Polynomial function, PolynomialRoots roots) {
        Map<Double, Integer> outputRootsMap = new HashMap<>();
        for (Map.Entry<Double, Integer> rootEntry : roots.getEntrySet()) {
            outputRootsMap.put(function.getValueEvaluatedAt(rootEntry.getKey()), rootEntry.getValue());
        }
        return new PolynomialRoots(outputRootsMap);
    }

}
