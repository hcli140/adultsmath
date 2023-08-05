package net.adultsmath.m1zc3;

import static java.lang.Math.pow;

public class PolynomialMatrix {
    private final int rows;
    private final int columns;
    private final Polynomial[][] entries;

    //  CONSTRUCTS AND GETTERS
    public PolynomialMatrix (Polynomial[][] inputs) {
        rows = inputs.length;
        columns = inputs[0].length;
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                try {
                    entries[i][j] = inputs[i][j];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    entries[i][j] = new Polynomial();
                }
            }
        }
    }
    public PolynomialMatrix (Matrix m) {
        rows = m.getRows();
        columns = m.getColumns();
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                entries[i][j] = new Polynomial(m.getValue(i,j));
            }
        }
    }

    //  create an identity matrix multiplied by a variable
    public PolynomialMatrix (int size) {
        rows = size;
        columns = size;
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == j) {
                    entries[i][j] = new Polynomial(1);
                }
                else {
                    entries[i][j] = new Polynomial(0);
                }
            }
        }
    }

    public Polynomial getValue (int r, int c) {
        return entries[r][c];
    }
    public Polynomial[][] getEntries() {
        return entries;
    }
    public Polynomial[] getRow (int r) {
        return entries[r];
    }
    public Polynomial[] getColumn (int c) {
        Polynomial[] col = new Polynomial[columns];
        for (int i = 0; i < columns; i++) {
            entries[i][c] = col[i];
        }
        return col;
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }




    //  evaluate each polynomial in the matrix at x = a and return a normal matrix
    public Matrix eval (double a) {
        double[][] mEntries = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mEntries[i][j] = getValue(i,j).eval(a);
            }
        }
        return new Matrix(mEntries);
    }




    //  TESTS
    public boolean isSquare () {
        return rows == columns;
    }




    //  transpose
    public static PolynomialMatrix transpose (PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.columns][a.rows];
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.columns; j++) {
                bEntries[j][i] = a.getValue(i,j);
            }
        }
        return new PolynomialMatrix(bEntries);
    }






    //  EIGENVALUES AND EIGENVECTORS
    //  removing a row for finding minor
    private PolynomialMatrix removeRow (int r) {
        if (r < rows) {
            Polynomial[][] mEntries = new Polynomial[rows - 1][columns];
            int i = 0;
            for (int j = 0; j < rows - 1; j++) {
                if (i == r) {
                    j--;
                }
                else {
                    for (int k = 0; k < columns; k++) {
                        mEntries[j][k] = getValue(i,k);
                    }
                }
                i++;
            }
            return new PolynomialMatrix(mEntries);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot remove the indicated row");
            return null;
        }
    }

    //  removing a column for finding minor
    public PolynomialMatrix removeColumn (int c) {
        if (c < columns) {
            return transpose(transpose(this).removeRow(c));
        }
        else {
            System.out.println("Invalid Row: Cannot remove the indicated column");
            return null;
        }
    }

    private Polynomial minor (int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            PolynomialMatrix m = this.removeRow(r).removeColumn(c);
            return m.det();
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the indicated minor");
            return null;
        }
    }
    private Polynomial cofactor (int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            return Operator.scalMult(pow(-1,r+c),minor(r,c));
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the indicated cofactor");
            return null;
        }
    }

    //  to determinant of a 2 by 2 polynomial matrix
    private Polynomial det2X2 () {
        if (rows == 2 && columns == 2) {
            Polynomial ad = Operator.multiply(getValue(0,0),getValue(1,1));
            Polynomial bc = Operator.multiply(getValue(0,1),getValue(1,0));
            //System.out.println(Polynomial.subtract(ad,bc));
            return Operator.subtract(ad,bc);
        }
        else {
            System.out.println("Invalid Dimensions: This method is for 2x2 polynomial matrices only");
            return null;
        }
    }

    public Polynomial det () {
        if (isSquare()) {
            if (rows == 1 && columns == 1) {
                return getValue(0,0);
            }
            else if (rows == 2 && columns == 2) {
                return det2X2();
            }
            else {
                //  cofactor expansion along first row
                Polynomial determinant = new Polynomial();
                for (int i = 0; i < columns; i++) {
                    determinant = Operator.add(determinant,Operator.multiply(getValue(0,i),cofactor(0,i)));
                }
                return determinant;
            }
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the determinant of a non-square matrix");
            return null;
        }
    }



    @Override
    public String toString () {
        String display = "";
        for (int i = 0; i < rows; i++) {
            display += "[";
            for (int j = 0; j < columns; j++) {
                if (j + 1 == columns) {
                    if (i + 1 == rows) {
                        display += getValue(i,j) + "]";
                    }
                    else {
                        display += getValue(i,j) + "]\n";
                    }
                }
                else {
                    display += getValue(i,j) + " ";
                }
            }
        }
        return display;
    }
}
