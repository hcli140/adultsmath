package net.adultsmath.m1zc3;

import java.util.Arrays;

import static java.lang.Math.*;



public class Main {
    public static void main(String[] args) {
        Matrix m = new Matrix(new double[][]{{0,0,-2},{1,2,1},{1,0,3}});
        System.out.println(m + "\n");
        System.out.println(m.eig());
        //System.out.println(m.minorCharPoly(0,0));
        //System.out.println(new Vector(m.eigenvalues()));
    }
}