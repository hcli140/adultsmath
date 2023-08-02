package net.adultsmath.m1zc3;

import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.*;


public class Matrix {
    //  the number of rows in the matrix
    private final int rows;
    //  the number of columns in the matrix
    private final int columns;
    //  each row is represented by its corresponding vector
    private final Vector[] entries;




    // --------------------------------------------------------------------------------------




    //  CONSTRUCTORS AND GETTERS
    //  construct from an array of arrays
    public Matrix (double[][] inputs) {
        rows = inputs.length;
        columns = inputs[0].length;
        entries = new Vector[rows];
        for (int i = 0; i < rows; i++) {
            Vector v = new Vector(inputs[i]);
            if (v.getSpaceR() == columns) {
                entries[i] = v;
            }
            else {
                entries[i] = v.copy(columns);
            }
        }
    }

    //  construct from an array of row vectors
    public Matrix (Vector... vectors) {
        rows = vectors.length;
        columns = vectors[0].getSpaceR();
        entries = new Vector[rows];
        for (int i = 0; i < rows; i++) {
            if (vectors[i].getSpaceR() == columns) {
                entries[i] = vectors[i];
            }
            else {
                entries[i] = vectors[i].copy(columns);
            }
        }
    }

    //  construct a zero matrix from specified dimensions
    public Matrix (int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        entries = new Vector[rows];
        for (int i = 0; i < rows; i++) {
            entries[i] = new Vector('0',columns);
        }
    }

    //  construct an identity matrix
    public Matrix (int size) {
        rows = size;
        columns = size;
        entries = new Vector[size];
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = 1;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < size; k++) {
            entries[k] = new Vector(matrix[k]);
        }
    }

    //  construct a matrix with random numbers
    public Matrix (char mode, int r, int c) {

        //  matrix of specified dimensions
        Random random = new Random();
        if (mode == 's') {
            rows = r;
            columns = c;
            entries = new Vector[rows];
            for (int i = 0; i < rows; i++) {
                entries[i] = new Vector('r',columns);
            }
        }
        //  random dimensions
        //  rows must be in [2,r], columns must be in [2,c]
        else {
            rows = random.nextInt(r-1) + 2;
            columns = random.nextInt(c-1) + 2;
            entries = new Vector[rows];
            for (int i = 0; i < rows; i++) {
                entries[i] = new Vector('r',columns);
            }
        }
    }

    //  get a value in the matrix
    public double getValue (int row, int column) {
        return entries[row].getComps(column);
    }

    //  get a row
    public Vector getRow (int r) {
        return entries[r];
    }

    //  get a column
    public Vector getColumn (int c) {
        double[] column = new double[rows];
        for (int i = 0; i < rows; i++) {
            column[i] = getValue(i,c);
        }
        return new Vector(column);
    }

    //  get the number of rows
    public int getRows () {
        return rows;
    }

    //  get the number of columns
    public int getColumns() {
        return columns;
    }

    //  get an array of all the row vectors
    public Vector[] getEntries() {
        return entries;
    }





    // --------------------------------------------------------------------------------------




    //  GAUSS-JORDAN ELIMINATION
    private void switchRows (int r1, int r2) {
        Vector temp = entries[r1];
        entries[r1] = entries[r2];
        entries[r2] = temp;
    }
    private void scalMultRow (double k, int r) {
        entries[r] = Vector.scalMult(k, entries[r]);
    }

    //  replace targetRow by targetRow + k * r
    private void addRows (int targetRow, int r, double k) {
        entries[targetRow] = Vector.add(entries[targetRow], Vector.scalMult(k, entries[r]));
    }

    //  set the pivot entry to 1 by dividing the
    private void setLeadingNumOne (int r) {
        //  iterate through each entry in a row
        for (int i = 0; i < columns; i++) {
            //  diffToZero is to account for any floating point inaccuracies
            double diffToZero = abs(getValue(r,i));
            if (diffToZero >= 1E-7) {
                //  divide each entry in the row by its pivot
                scalMultRow(1 / getValue(r,i), r);
                break;
            }
        }
        round();
    }

    //  round all entries to either 1 or 0
    private void round () {
        //  iterate through each row
        for (int i = 0 ; i < rows; i++) {
            //  iterate through each entry in a row
            for (int j = 0; j < columns; j++) {
                double diffToZero = abs(getValue(i,j));
                double diffToOne = abs(getValue(i,j) - 1);
                if (diffToZero <= 1E-7) {
                    entries[i] = entries[i].replace(j,0);
                }
                else if (diffToOne <= 1E-7) {
                    entries[i] = entries[i].replace(j,1);;
                }
            }
        }
    }

    //  move all rows of 0's to the bottom of the matrix, also outputs the number of zero rows
    private int sortZeroRows () {
        //  iterate through the rows from the top to the bottom
        for (int i = 0; i < rows; i++) {
            //  encountering a zero row
            if (entries[i].isNull()) {
                //  iterate through the rows from the bottom and stop before reaching the ith row
                for (int j = rows - 1; j > i; j--) {
                    //  swap rows if the ith row is null but the jth row is not
                    if (!entries[j].isNull()) {
                        switchRows(i,j);
                        break;
                    }
                }
            }
        }

        //  return the number of null rows
        int numNullRows = 0;
        for (int k = 0; k < rows; k++) {
            if (entries[k].isNull()) {
                numNullRows++;
            }
        }
        return numNullRows;
    }

    //  get the index of the pivot in a given row
    private int getPivotIndex (int r) {
        for (int i = 0; i < columns; i++) {
            if (getValue(r,i) != 0) {
                return i;
            }
        }
        return -1;
    }

    //  sort the matrix so that all the zero rows are at the bottom and all the other rows have cascading pivots
    public void sort () {
        //  get the number of non-zero rows
        int numNonNullRows = rows - sortZeroRows();

        //  get an array of the indices of pivots in order of rows
        int[] pivotIndices = new int[numNonNullRows];
        for (int i = 0; i < numNonNullRows; i++) {
            pivotIndices[i] = getPivotIndex(i);
        }

        //  sort the rows using selection sort
        //  pivot indices in ascending order
        for (int i = 0; i < numNonNullRows; i++) {
            //  find the minimum in the remaining elements
            int min = pivotIndices[i];
            for (int j = i + 1; j < numNonNullRows; j++) {
                if (pivotIndices[j] < min) {
                    //  set a new minimum
                    min = pivotIndices[j];
                }
            }
            //  swap the current entry with the min
            //  swapping in the array
            int temp = pivotIndices[i];
            pivotIndices[i] = pivotIndices[min];
            pivotIndices[min] = temp;

            //  swapping the rows
            switchRows(i,min);
        }
    }

    //  return the matrix in row-echelon form
    public Matrix reduceREF () {
        Matrix m = this;
        m.sort();

        //  get leading 1's in cascading pattern
        //  iterate through the rows top to bottom
        for (int i = 0; i < m.rows; i++) {

            System.out.println("i = " + i + "\n");

            if (!m.entries[i].isNull()) {
                //  get leading 0's
                //  iterate through the rows from the top and stop before reaching the ith row
                for (int j = 0; j < i; j++) {

                    double constant = m.getValue(i,j);
                    m.addRows(i, j,-constant);

                }
            }

            //  get a leading 1
            m.setLeadingNumOne(i);
        }

        return m;
    }

    //  return the matrix in reduced row-echelon form
    public Matrix reduceRREF () {
        //  get to REF
        Matrix m = this.reduceREF();

        //  get 0's in the same column as leading 1's
        int smallestDimension = min(m.rows,m.columns);
        for (int i = 0; i < m.rows; i++) {

            //System.out.println("i = " + i);
            for (int j = i + 1; j < smallestDimension; j++) {
                if (!entries[i].isNull()) {
                    //System.out.println("j = " + j);
                    //System.out.println("Before add:\n" + m.display());
                    double constant = m.getValue(i,j);
                    boolean targetIsNull = m.entries[i].isNull();
                    boolean sourceIsNull = m.entries[j].isNull();
                    boolean shouldAdd = !targetIsNull && !sourceIsNull && !(constant == 0);
                    if (shouldAdd) {
                        m.addRows(i, j, -constant);
                    }
                    //System.out.println("After add:\n" + m.display());
                }
            }
        }

        return m;
    }




    // --------------------------------------------------------------------------------------




    //  TESTS
    //  test if matrix is square
    public boolean isSquare () {
        return rows == columns;
    }

    //  test if matrix is upper triangular
    public boolean isUTri () {
        if (isSquare()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (i > j && getValue(i,j) != 0) {
                        return false;
                    }
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    //  test if a matrix is lower triangular
    public boolean isLTri () {
        if (isSquare()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (i < j && getValue(i,j) != 0) {
                        return false;
                    }
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    //  test if matrix is diagonal
    public boolean isDiag () {
        return isUTri() && isLTri();
    }

    //  test if matrix is an identity
    public boolean isIdentity () {
        if (isDiag()) {
            for (int i = 0; i < rows; i++) {
                if (getValue(i,i) != 1) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    //  test if matrix is symmetric
    public boolean isSymmetric () {
        if (isSquare()) {
            Matrix a = transpose(this);
            if (isIdentical(a,this)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    //  test if matrix is invertible
    public boolean isInvertible () {
        return isSquare() && det() != 0;
    }




    // --------------------------------------------------------------------------------------




    //  MATRIX OPERATIONS
    //  a + b
    public static Matrix add (Matrix a, Matrix b) {
        if (a.rows == b.rows && a.columns == b.columns) {
            Matrix sum = new Matrix(a.rows, a.columns);
            for (int i = 0; i < a.rows; i++) {
                sum.entries[i] = Vector.add(a.entries[i], b.entries[i]);
            }
            return sum;
        }
        else {
            System.out.println("Invalid Dimensions: Cannot add matrices of different dimensions");
            return null;
        }
    }

    //  a - b
    public static Matrix subtract (Matrix a, Matrix b) {
        return add(a,scalMult(-1, b));
    }

    //  k * a
    public static Matrix scalMult (double k, Matrix a) {
        Matrix newMatrix = new Matrix(a.rows, a.columns);
        for (int i = 0; i < a.rows; i++) {
            newMatrix.entries[i] = Vector.scalMult(k, a.entries[i]);
        }
        return newMatrix;
    }

    //  multiply
    public static Matrix multiply (Matrix a, Matrix b) {
        if (a.isIdentity()) {
            return b;
        }
        else if (b.isIdentity()) {
            return a;
        }
        else if (a.columns == b.rows) {
            Matrix product = new Matrix(a.rows, b.columns);
            for (int i = 0; i < a.rows; i++) {
                for (int j = 0; j < b.columns; j++) {
                    product.entries[i] = product.entries[i].replace(j,Vector.dot(a.entries[i],b.getColumn(j)));
                }
            }
            return product;
        }
        else {
            System.out.println("Invalid Dimensions: Cannot multiply matrices with these dimensions. The number of columns of the first matrix must match the number of rows of the second matrix");
            return null;
        }
    }

    //  transpose
    public static Matrix transpose (Matrix a) {
        Matrix newMatrix = new Matrix(a.columns, a.rows);
        for (int i = 0; i < a.columns; i++) {
            //System.out.println(a.getColumn(i).display());
            newMatrix.entries[i] = a.getColumn(i);
        }
        return newMatrix;
    }

    //  get the trace
    public static double trace (Matrix a) {
        double trace = 0;
        if (a.isSquare()) {
            for (int i = 0; i < a.rows; i++) {
                trace += a.getValue(i,i);
            }
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the trace of a non-square matrix");
        }
        return trace;
    }




    // --------------------------------------------------------------------------------------




    //  DETERMINANT
    //  of a 2x2
    private double det2X2 () {
        if (rows == 2 && columns == 2) {
            return getValue(0,0) * getValue(1,1) - getValue(0,1) * getValue(1,0);
        }
        else {
            System.out.println("Invalid Dimensions: This method is for 2x2 matrices only");
            return 0;
        }
    }

    //  remove a row (to get minor)
    private Matrix removeRow (int r) {
        if (r < rows) {
            Matrix m = new Matrix(rows - 1, columns);
            int i = 0;
            for (int j = 0; j < m.rows; j++) {
                if (i == r) {
                    j--;
                }
                else {
                    m.entries[j] = entries[i];
                }
                i++;
            }
            return m;
        }
        else {
            System.out.println("Invalid Dimensions: Cannot remove the indicated row");
            return null;
        }
    }

    //  remove a column (to get minor)
    private Matrix removeColumn (int c) {
        if (c < columns) {
            Matrix m = new Matrix(rows, columns - 1);
            for (int i = 0; i < rows; i++) {
                m.entries[i] = entries[i].remove(c);
            }
            return m; 
        }
        else {
            System.out.println("Invalid Row: Cannot remove the indicated column");
            return null;
        }
    }

    //  get the minor
    private double minorDet (int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            Matrix m = this.removeRow(r).removeColumn(c);
            return m.det();
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the indicated minor");
            return 0;
        }
    }

    //  get the cofactor
    private double cofactor (int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            return pow(-1,r+c) * minorDet(r,c);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the indicated cofactor");
            return 0;
        }
    }

    public double det () {
        if (isSquare()) {
            if (rows == 1 && columns == 1) {
                return getValue(0,0);
            }
            else if (rows == 2 && columns == 2) {
                return det2X2();
            }
            else {
                //  cofactor expansion along first row
                double determinant = 0;
                for (int i = 0; i < columns; i++) {
                    //System.out.println(entries[0].comps[i] + " x " + minor(0,i) + " x " + pow(-1, i));
                    determinant += getValue(0,i) * cofactor(0,i);
                }
                return determinant;
            }
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the determinant of a non-square matrix");
            return 0;
        }
    }




    // --------------------------------------------------------------------------------------




    //  INVERSE
    //  adjoint
    public Matrix adj () {
        if (isSquare()) {
            Matrix m = new Matrix(rows, columns);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    m.entries[i] = m.entries[i].replace(j,cofactor(i,j));
                }
            }
            return transpose(m);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the adjoint of a non-square matrix");
            return null;
        }
    }

    //  inverse
    public Matrix inv () {
        if (isInvertible()) {
            double k = 1 / det();
            return scalMult(k,adj());
        }
        else {
            System.out.println("Invalid Matrix: This matrix is not invertible");
            return null;
        }
    }






    // --------------------------------------------------------------------------------------





    //  EIGENVECTORS AND EIGENVALUES
    //  create the lambda * I - A polynomial Matrix
    private PolynomialMatrix eigPolyM () {
        if (isSquare()) {
            Polynomial[][] mEntries = new Polynomial[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (i == j) {
                        mEntries[i][j] = new Polynomial(-getValue(i,j),1);
                    }
                    else {
                        mEntries[i][j] = new Polynomial(-getValue(i,j));
                    }
                }
            }
            return new PolynomialMatrix(mEntries);
        }
        else {
            System.out.println("Invalid Dimensions: Cannot get the lambda * I - A polynomial matrix of a non-square matrix");
            return null;
        }
    }

    //  find the eigenvalues by solving for the roots of the characteristic polynomial det(lambda * I - A)
    //  further tests needed
    public double[] eigenvalues () {
        return eigPolyM().det().roots();
    }

    //  return a diagonal matrix of eigenvalues with a corresponding matrix of eigenvectors
    //  not done yet
    public Matrix[] eig () {
        double[] eVal = eigenvalues();
        System.out.println(new Vector(eVal) + "\n");
        ArrayList<Vector> eVec = new ArrayList<Vector>();
        Matrix I = new Matrix(rows);
        Matrix a;
        Matrix b;
        for (int i = 0; i < eVal.length; i++) {
            a = subtract(scalMult(eVal[i],I),this);
            b = new Matrix(rows,columns+1);
            //  add a column of 0's on the right
            for (int j = 0; j < rows; j++) {
                b.entries[j] = a.entries[j].copy(columns + 1);
            }
            System.out.println(b + "\n");
            System.out.println(b.reduceRREF() + "\n");
        }
        return new Matrix[]{new Matrix(1)};
    }








    // --------------------------------------------------------------------------------------





    //  test if two matrices are identical
    public static boolean isIdentical (Matrix a, Matrix b) {
        if (a.rows == b.rows && a.columns == b.columns) {
            for (int i = 0; i < a.rows; i++) {
                if (!Vector.isIdentical(a.entries[i], b.entries[i])) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    //  display matrix as a String
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
