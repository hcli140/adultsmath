package main.net.adultsmath;


public class Main {
    public static void main(String[] args) {
        double R0Actual = 990.24;
        double R1Actual = 988.33;
        double R2Actual = 98.957;
        double R3Actual = 10.016;
        double R4Actual = 9.910;
        double a1 = 1 / R0Actual + 1 / R1Actual + 1 / R2Actual + 1 / R3Actual;
        double b1 = -1 / R3Actual;
        double c1 = 12 / R0Actual;
        double a2 = 1 / R3Actual;
        double b2 = -(1 / R3Actual + 1 / R4Actual);
        Matrix m = new Matrix(new double[][]{
                {a1, b1, c1},
                {a2, b2, 0}
        });
        double V1 = m.getSolution().value().getComps(0);
        double V2 = m.getSolution().value().getComps(1);
        double I1 = V1 / R1Actual;
        double I2 = V1 / R2Actual;
        double I3 = V2 / R4Actual;
        double IT = I1 + I2 + I3;
        System.out.println("V1 = " + V1 + "\nV2 = " + V2 + "\nI1 = " + I1 + "\nI2 = " + I2 + "\nI3 = " + I3 + "\nIT = " + IT);
    }
}