package net.adultsmath.m1zc3;

import java.util.Arrays;

import static java.lang.Math.*;

//  Main class is used for testing other classes and methods

public class Main {
    public static void main(String[] args) {
        Matrix m = new Matrix(new double[][]{{6,2,3},{4,-1,9},{8,-2,0}});
        System.out.println(m + "\n");
        //m.sort();
        System.out.println(m.reduceREF());
        VectorRn v = new VectorRn(1,2,3);
    }
}