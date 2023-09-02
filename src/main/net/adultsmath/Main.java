package main.net.adultsmath;

import main.net.adultsmath.m1zc3.*;


public class Main {
    public static void main(String[] args) {
        //  find the basis for the eigenspace for m
        Matrix m = new Matrix(new double[][]{{0, 0, -2}, {1, 2, 1}, {1, 0, 3}});
        //  matrix lambda * I - A for lambda = 2, plus a column of zeroes
        Matrix a = new Matrix(new double[][]{{2, 0, 2, 0}, {-1, 0, -1, 0}, {-1, 0, -1, 0}});
        Matrix b = new Matrix(new double[][]{{1, 2, 3, 4}, {-1, -2, 0, 5}, {0, -4, 1, 2}});
        System.out.println(a);
        System.out.println(a.getReducedRowEchelonForm());
        System.out.println(new Polynomial(-15, -3, -3, 1).getRoots());
    }
}