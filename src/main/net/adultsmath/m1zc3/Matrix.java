package main.net.adultsmath.m1zc3;

import java.util.*;

import static java.lang.Math.*;


public class Matrix {
    //  the number of rows in the matrix
    private final int numRows;
    //  the number of columns in the matrix
    private final int numColumns;
    //  each row is represented by its corresponding vector
    private final List<Vector> entries;


    // --------------------------------------------------------------------------------------


    //  CONSTRUCTORS AND GETTERS
    public Matrix(double[][] inputs) {
        numRows = inputs.length;
        numColumns = inputs[0].length;
        entries = new ArrayList<>();
        for (double[] row : inputs) {
            Vector v = new Vector(row);
            if (v.getSize() == numColumns) {
                entries.add(v);
            } else {
                entries.add(v.getTruncated(numColumns));
            }
        }
    }

    public Matrix(Vector... vectors) {
        numRows = vectors.length;
        numColumns = vectors[0].getSize();
        entries = new ArrayList<>();
        for (Vector v : vectors) {
            if (v.getSize() == numColumns) {
                entries.add(v);
            } else {
                entries.add(v.getTruncated(numColumns));
            }
        }
    }

    public Matrix(List<Vector> vectors) {
        numRows = vectors.size();
        numColumns = vectors.get(0).getSize();
        entries = new ArrayList<>();
        for (Vector v : vectors) {
            if (v.getSize() == numColumns) {
                entries.add(v);
            } else {
                entries.add(v.getTruncated(numColumns));
            }
        }
    }

    public static Matrix createZeroMatrix(int rows, int columns) {
        if (rows <= 0)
            throw new InvalidMatrixSizeException("Cannot create a zero matrix with less than 1 row");
        if (columns <= 0)
            throw new InvalidMatrixSizeException("Cannot create a zero matrix with less than 1 column");
        double[][] entries = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                entries[i][j] = 0;
            }
        }
        return new Matrix(entries);
    }

    public static Matrix createIdentityMatrix(int size) {
        if (size <= 0)
            throw new InvalidMatrixSizeException("Cannot create an identity matrix of size 0 or less");
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        return new Matrix(matrix);
    }

    public static Matrix createFromColumnVectors(List<Vector> vectors) {
        return new Matrix(vectors).getTransposeMatrix();
    }

    public List<Vector> getEntries() {
        return entries;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public double getEntry(int row, int column) {
        return entries.get(row).getComps(column);
    }

    public Vector getRow(int r) {
        return entries.get(r);
    }

    public Vector getColumn(int c) {
        List<Double> column = new ArrayList<>();
        for (int i = 0; i < this.getNumRows(); i++) {
            column.add(this.getEntry(i, c));
        }
        return new Vector(column);
    }

    public List<Integer> getDimensions() {
        return Arrays.asList(numRows, numColumns);
    }


    public Vector getBottomRow() {
        return this.getRow(numRows - 1);
    }

    public Vector getRightMostColumn() {
        return this.getColumn(numColumns - 1);
    }


    // --------------------------------------------------------------------------------------


    public boolean hasLeadingOne(int r) {
        Vector row = this.getRow(r);
        for (double entry : row.getComps()) {
            if (entry != 0) {
                return entry == 1;
            }
        }
        return false;
    }

    public boolean isExplicitlyDefined(int r) {
        if (this.hasLeadingOne(r)) {
            for (int i = this.getPivotIndex(r) + 1; i < this.numColumns - 1; i++) {
                if (this.getEntry(r, i) != 0) return false;
            }
            return true;
        }
        return false;
    }

    private Matrix getWithInsertedRow(int index, Vector row) {
        if (row.getSize() != numColumns)
            throw new InvalidDimensionsException("This row's length does not match the number of columns in the matrix");
        if (index < 0) throw new IllegalArgumentException("The index cannot be negative");
        else if (index > numRows) throw new IllegalArgumentException("The index cannot exceed the number of rows");
        List<Vector> newEntries = new ArrayList<>();
        boolean inserted = false;
        for (int i = 0; i < numRows + 1; i++) {
            if (i == index && !inserted) {
                newEntries.add(row);
                i--;
                inserted = true;
            } else {
                try {
                    newEntries.add(this.getRow(i));
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        return new Matrix(newEntries.toArray(new Vector[numRows + 1]));
    }

    private Matrix getWithAppendedRow(Vector row) {
        return getWithInsertedRow(numRows, row);
    }

    private Matrix getWithInsertedColumn(int index, Vector column) {
        return this.getTransposeMatrix().getWithInsertedRow(index, column).getTransposeMatrix();
    }

    private Matrix getWithAppendedColumn(Vector column) {
        return this.getWithInsertedColumn(numColumns, column);
    }

    private Matrix getWithRemovedRow(int r) {
        if (r < numRows) {
            Matrix m = Matrix.createZeroMatrix(numRows - 1, numColumns);
            int i = 0;
            for (int j = 0; j < m.numRows; j++) {
                if (i == r) {
                    j--;
                } else {
                    m.entries.set(j, this.getRow(i));
                }
                i++;
            }
            return m;
        } else {
            throw new NoSuchRowException("Row with index " + r + " does not exist");
        }
    }

    public Matrix getWithRemovedBottomRow() {
        return this.getWithRemovedRow(this.numRows - 1);
    }

    private Matrix getWithRemovedColumn(int c) {
        if (c < numColumns) {
            Matrix m = Matrix.createZeroMatrix(numRows, numColumns - 1);
            for (int i = 0; i < numRows; i++) {
                m.entries.set(i, this.getRow(i).getWithoutIndex(c));
            }
            return m;
        } else {
            throw new IllegalArgumentException("The indicated column does not exist");
        }
    }


    // --------------------------------------------------------------------------------------


    //  GAUSS-JORDAN ELIMINATION
    private void switchRows(int row1, int row2) {
        Vector temp = this.getRow(row2);
        entries.set(row2, entries.get(row1));
        entries.set(row1, temp);
    }

    private void scalMultRow(double k, int row) {
        entries.set(row, Operator.scalMult(k, getRow(row)));
    }

    private void addTwoRows(int targetRow, int sourceRow, double k) {
        entries.set(targetRow,
                Operator.add(Operator.scalMult(k, this.getRow(sourceRow))
                        , this.getRow(targetRow)));
    }

    private void setLeadingNumOne(int row) {
        for (int i = 0; i < numColumns; i++) {
            if (abs(getEntry(row, i)) >= 1E-7) {
                scalMultRow(1 / getEntry(row, i), row);
                break;
            }
        }
        roundEntriesToOneOrZero();
    }

    private void roundEntriesToOneOrZero() {
        //  iterate through each row
        for (int i = 0; i < numRows; i++) {
            //  iterate through each entry in a row
            for (int j = 0; j < numColumns; j++) {
                double diffToZero = abs(getEntry(i, j));
                double diffToOne = abs(getEntry(i, j) - 1);
                //  is zero
                if (diffToZero <= 1E-5) {
                    entries.set(i, getRow(i).getReplace(j, 0));
                }
                //  is one
                else if (diffToOne <= 1E-5) {
                    entries.set(i, getRow(i).getReplace(j, 1));
                }
            }
        }
    }

    //  move all rows of 0's to the bottom of the matrix, also returns the number of zero rows
    private int sortZeroRows() {
        for (int i = 0; i < numRows; i++) {
            if (this.getRow(i).isZero()) {
                for (int j = numRows - 1; j > i; j--) {
                    if (!this.getRow(j).isZero()) {
                        switchRows(i, j);
                        break;
                    }
                }
            }
        }

        //  return the number of null rows
        int numNullRows = 0;
        for (int k = 0; k < numRows; k++) {
            if (this.getRow(k).isZero()) {
                numNullRows++;
            }
        }
        return numNullRows;
    }

    private int getPivotIndex(int r) {
        for (int i = 0; i < numColumns; i++) {
            if (getEntry(r, i) != 0) {
                return i;
            }
        }
        throw new ZeroRowException("This row (" + r + ") has only zeroes");
    }

    //  sort the matrix so that all the zero rows are at the bottom
    //  and all the other rows have cascading pivots
    public void sort() {
        //  get the number of non-zero rows
        int numNonNullRows = numRows - sortZeroRows();

        //  get an array of the indices of pivots in order of rows
        //int[] pivotIndices = new int[numNonNullRows];
        List<Integer> pivotIndices = new ArrayList<>();
        for (int i = 0; i < numNonNullRows; i++) {
            pivotIndices.add(getPivotIndex(i));
        }

        //  sort the rows using selection sort
        //  pivot indices in ascending order
        for (int i = 0; i < numNonNullRows; i++) {
            //  find the minimum in the remaining elements
            int minI = i;
            for (int j = i + 1; j < numNonNullRows; j++) {
                if (pivotIndices.get(j) < pivotIndices.get(minI)) {
                    //  set a new minimum
                    minI = j;
                }
            }
            //  swap the current entry with the min
            //  swapping in the array
            int temp = pivotIndices.get(i);
            pivotIndices.set(i, pivotIndices.get(minI));
            pivotIndices.set(minI, temp);

            //  swapping the rows
            switchRows(i, minI);
        }
    }

    public Matrix getRowEchelonForm() {
        Matrix m = this;
        m.sort();

        //  get leading 1's in cascading pattern
        //  iterate through the rows top to bottom
        for (int i = 0; i < m.numRows; i++) {

            if (!m.getRow(i).isZero()) {
                //  get leading 0's
                //  iterate through the rows from the top and stop before reaching the ith row
                for (int j = 0; j < i; j++) {

                    double constant = m.getEntry(i, j);
                    m.addTwoRows(i, j, -constant);

                }
            }

            //  get a leading 1
            m.setLeadingNumOne(i);
        }

        m.sort();
        return m;
    }

    public Matrix getReducedRowEchelonForm() {
        //  get to REF
        Matrix m = this.getRowEchelonForm();
//        System.out.println("After reduceREF:\n" + m);

        //  get 0's in the same column as leading 1's
        int smallestDimension = min(m.numRows, m.numColumns);
        for (int i = 0; i < m.numRows; i++) {

//            System.out.println("i = " + i);
            for (int j = i + 1; j < smallestDimension; j++) {
                if (!this.getRow(i).isZero()) {
//                    System.out.println("j = " + j);
//                    System.out.println("Before add:\n" + m);
                    double constant = m.getEntry(i, j);
                    boolean targetIsNull = m.getRow(i).isZero();
                    boolean sourceIsNull = m.getRow(j).isZero();
                    boolean shouldAdd = !targetIsNull && !sourceIsNull && !(constant == 0);
                    if (shouldAdd) {
                        m.addTwoRows(i, j, -constant);
                    }
//                    System.out.println("After add:\n" + m);
                }
            }
        }
        return m;
    }

    //  solve the linear system represented by the augmented matrix
    public LinearSystemSolution getSolution() {

        Matrix reduced = this.getReducedRowEchelonForm();

        //  add or remove some zero rows so that there is one more column than there are rows
        int rowsMissing = this.getNumColumns() - 1 - this.getNumRows();
        if (rowsMissing > 0) {
            for (int i = 0; i < rowsMissing; i++) {
                reduced = reduced.getWithAppendedRow(Vector.createZeroVector(this.getNumColumns()));
            }
        } else if (rowsMissing < 0) {
            for (int i = 0; i < -rowsMissing; i++) {
                if (reduced.getBottomRow().isZero()) reduced = reduced.getWithRemovedBottomRow();
                else return new LinearSystemSolution();
            }
        }

        if (!reduced.isConsistent()) return new LinearSystemSolution();

//        System.out.println("Reduced matrix: \n" + reduced);


        //  see if there is a single solution
        int numPivots = 0;
        for (Vector row : this.getEntries()) {
            if (!row.isZero()) numPivots++;
        }
        int numUnknowns = reduced.getNumColumns() - 1;
        if (numPivots == numUnknowns)
            return new LinearSystemSolution(reduced.getRightMostColumn().getTruncated(numPivots));

        //  infinite solutions ---> find the vector space
        //  find the indices of unknowns that are explicitly defined
        List<Integer> explicitlyDefinedIndices = new ArrayList<>();
        for (int i = 0; i < reduced.getNumRows(); i++) {
            if (reduced.isExplicitlyDefined(i)) explicitlyDefinedIndices.add(reduced.getPivotIndex(i));
        }
        //  find the indices of unknowns that are defined based on other unknowns
        List<Integer> implicitlyDefinedIndices = new ArrayList<>();
        for (int i = 0; i < reduced.getNumRows(); i++) {
            if (!reduced.getRow(i).isZero() && !reduced.isExplicitlyDefined(i))
                implicitlyDefinedIndices.add(reduced.getPivotIndex(i));
        }
        //  find the unknowns that are arbitrary
        List<Integer> arbitraryUnknownIndices = new ArrayList<>();
        for (int i = 0; i < reduced.getNumColumns() - 1; i++) {
            if (!explicitlyDefinedIndices.contains(i) && !implicitlyDefinedIndices.contains(i))
                arbitraryUnknownIndices.add(i);
        }
//        System.out.println("Explicit: " + explicitlyDefinedIndices);
//        System.out.println("Implicit: " + implicitlyDefinedIndices);
//        System.out.println("Arbitrary: " + arbitraryUnknownIndices);

        //  get the explicit values of the solution
        Vector solutionValue = reduced.getRightMostColumn().getTruncated(numUnknowns);

        //  get the basis
        List<Double> comps;
        List<Vector> solutionBasis = new ArrayList<>();
        //  iterate through each column except for the last one
        for (int i = 0; i < reduced.getNumColumns() - 1; i++) {
            //  if the unknown represented by the column is arbitrary
            if (arbitraryUnknownIndices.contains(i)) {
                comps = new ArrayList<>();
                //  iterate down the column
                for (int j = 0; j < numUnknowns; j++) {
//                    System.out.println("Value at (" + i + ", " + j + "): " + reduced.getValue(j, i));
                    if (j == i) comps.add(1.0);
                    else if (reduced.getEntry(j, i) != 0) comps.add(-reduced.getEntry(j, i));
                    else comps.add(0.0);
                }
                solutionBasis.add(new Vector(comps));
            }
        }

        return new LinearSystemSolution(solutionValue, new VectorSet(solutionBasis));
    }


    // --------------------------------------------------------------------------------------


    //  BOOLEANS
    public boolean isSquare() {
        return this.getNumRows() == this.getNumColumns();
    }

    public boolean isUpperTriangular() {
        if (isSquare()) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    if (i > j && getEntry(i, j) != 0) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean isLowerTriangular() {
        if (isSquare()) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    if (i < j && getEntry(i, j) != 0) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean isDiagonal() {
        return isUpperTriangular() && isLowerTriangular();
    }
    public boolean isIdentity() {
        if (isDiagonal()) {
            for (int i = 0; i < numRows; i++) {
                if (getEntry(i, i) != 1) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean isSymmetric() {
        if (isSquare()) {
            Matrix m = this.getTransposeMatrix();
            return this.equals(m);
        } else {
            return false;
        }
    }

    public boolean isInvertible() {
        return isSquare() && getDeterminant() != 0;
    }

    //  test if linear system is consistent
    public boolean isConsistent() {
        for (Vector row : this.getEntries()) {
            if (row.getWithoutIndex(numColumns - 1).isZero()
                    && abs(row.getComps(numColumns - 1)) > 1E-7)
                return false;
        }
        return true;
    }

    public boolean isDiagonalizeable() {
        try {
            this.getDiagonalized();
        } catch (NotDiagonalizeableException e) {
            return false;
        }
        return true;
    }


    // --------------------------------------------------------------------------------------


    //  transpose
    public Matrix getTransposeMatrix() {
        Matrix newMatrix = Matrix.createZeroMatrix(this.numColumns, this.numRows);
        for (int i = 0; i < this.numColumns; i++) {
            //System.out.println(a.getColumn(i).display());
            newMatrix.entries.set(i, this.getColumn(i));
        }
        return newMatrix;
    }

    //  get the trace
    public double getTrace() {
        double trace = 0;
        if (this.isSquare()) {
            for (int i = 0; i < this.numRows; i++) {
                trace += this.getEntry(i, i);
            }
        } else {
            throw new InvalidDimensionsException("Cannot get the trace of a non-square matrix");
        }
        return trace;
    }


    // --------------------------------------------------------------------------------------


    //  DETERMINANT
    //  of a 2x2
    private double getDet2X2() {
        if (numRows == 2 && numColumns == 2) {
            return getEntry(0, 0)
                    * getEntry(1, 1) - getEntry(0, 1)
                    * getEntry(1, 0);
        } else {
            throw new InvalidDimensionsException("This method is for 2x2 matrices only");
        }
    }

    //  get the minor's determinant
    public double getMinorDeterminant(int r, int c) {
        if (!this.isSquare())
            throw new InvalidDimensionsException(
                    "Cannot get the minor's determinant because the matrix is not square");
        else if (r > numRows || c > numColumns)
            throw new IllegalArgumentException("The indicated row or column does not exist");
        else {
            Matrix m = this.getWithRemovedRow(r).getWithRemovedColumn(c);
            return m.getDeterminant();
        }
    }

    //  get the cofactor
    private double getCofactor(int r, int c) {
        return pow(-1, r + c) * getMinorDeterminant(r, c);
    }

    public double getDeterminant() {
        if (isSquare()) {
            if (numRows == 1 && numColumns == 1) {
                return getEntry(0, 0);
            } else if (numRows == 2 && numColumns == 2) {
                return getDet2X2();
            } else {
                //  cofactor expansion along first row
                double determinant = 0;
                for (int i = 0; i < numColumns; i++) {
                    //System.out.println(entries[0].comps[i] + " x " + minor(0,i) + " x " + pow(-1, i));
                    determinant += getEntry(0, i) * getCofactor(0, i);
                }
                return determinant;
            }
        } else throw new InvalidDimensionsException("Cannot get the determinant of a non-square matrix");
    }


    // --------------------------------------------------------------------------------------


    //  INVERSE
    //  adjoint
    public Matrix getAdjoint() {
        if (isSquare()) {
            Matrix m = Matrix.createZeroMatrix(numRows, numColumns);
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    m.entries.set(i, m.getRow(i).getReplace(j, getCofactor(i, j)));
                }
            }
            return m.getTransposeMatrix();
        } else throw new InvalidDimensionsException("Cannot get the adjoint of a non-square matrix");
    }

    //  inverse
    public Matrix getInverse() {
        if (isInvertible()) {
            double k = 1 / getDeterminant();
            return Operator.scalMult(k, getAdjoint());
        } else throw new NotInvertibleException("This matrix is not invertible");
    }


    // --------------------------------------------------------------------------------------


    //  EIGENVECTORS AND EIGENVALUES
    //  create the lambda * I - A polynomial Matrix
    private PolynomialMatrix getLambdaIMinusAMatrix() {
        if (isSquare()) {
            Polynomial[][] mEntries = new Polynomial[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    if (i == j) {
                        mEntries[i][j] = new Polynomial(-getEntry(i, j), 1);
                    } else {
                        mEntries[i][j] = new Polynomial(-getEntry(i, j));
                    }
                }
            }
            return new PolynomialMatrix(mEntries);
        } else
            throw new InvalidDimensionsException(
                    "Cannot get the lambda * I - A polynomial matrix of a non-square matrix");
    }

    //  find the eigenvalues by solving for the
    //  roots of the characteristic polynomial, det(lambda * I - A)
    public PolynomialRoots getEigenValues() {
        return getLambdaIMinusAMatrix().det().getRoots();
    }

    //  return a matrix of eigenvectors and a diagonal matrix of eigenvalues in corresponding columns
    public MatrixDiagonalized getDiagonalized() {
        PolynomialRoots eigenvalues = getEigenValues();

        Map<VectorSet, Double> eigenMap = new HashMap<>();

        for (Map.Entry<Double, Integer> eigenvalueEntry : eigenvalues.getEntrySet()) {
            List<Vector> eigenvectorList =
                    this.getLambdaIMinusAMatrix().eval(eigenvalueEntry.getKey())
                    .getWithAppendedColumn(Vector.createZeroVector(this.getNumRows()))
                    .getSolution()
                    .solutionSet()
                    .getVectorsList();
            if (eigenvalueEntry.getValue() != eigenvectorList.size())
                throw new NotDiagonalizeableException("This matrix is not diagonalizeable");
            eigenvectorList.forEach(v -> {
                        eigenMap.put(new VectorSet(v), eigenvalueEntry.getKey());
                    });
        }

        return new MatrixDiagonalized(eigenMap);
    }


    // --------------------------------------------------------------------------------------


    //  display matrix as a String
    @Override
    public String toString() {
        String display = "";
        for (int i = 0; i < numRows; i++) {
            display += "[";
            for (int j = 0; j < numColumns; j++) {
                if (j + 1 == numColumns) {
                    if (i + 1 == numRows) {
                        display += getEntry(i, j) + "]";
                    } else {
                        display += getEntry(i, j) + "]\n";
                    }
                } else {
                    display += getEntry(i, j) + " ";
                }
            }
        }
        return display;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Matrix matrix) {
            if (this.getDimensions().equals(matrix.getDimensions())) {
                for (int i = 0; i < this.numRows; i++) {
                    if (!this.getRow(i).equals(matrix.getRow(i))) return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numRows, numColumns, entries);
    }


    public static class NotInvertibleException extends RuntimeException {
        public NotInvertibleException() {
            super();
        }

        public NotInvertibleException(String message) {
            super(message);
        }
    }

    public static class ZeroRowException extends RuntimeException {
        public ZeroRowException() {
            super();
        }

        public ZeroRowException(String message) {
            super(message);
        }
    }

    public static class NotDiagonalizeableException extends RuntimeException {
        public NotDiagonalizeableException() {
            super();
        }
        public NotDiagonalizeableException(String message) {
            super(message);
        }
    }

    public static class InvalidMatrixSizeException extends RuntimeException {
        public InvalidMatrixSizeException(String message) {
            super(message);
        }
    }

    public static class NoSuchRowException extends RuntimeException {
        public NoSuchRowException(String message) {
            super(message);
        }
    }
}
