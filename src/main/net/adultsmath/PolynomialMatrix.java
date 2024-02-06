package main.net.adultsmath;

import static java.lang.Math.pow;

public class PolynomialMatrix {
    private final int rows;
    private final int columns;
    private final Polynomial[][] entries;

    //  CONSTRUCTS AND GETTERS
    public PolynomialMatrix(Polynomial[][] inputs) {
        rows = inputs.length;
        columns = inputs[0].length;
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                try {
                    entries[i][j] = inputs[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    entries[i][j] = new Polynomial();
                }
            }
        }
    }

    public PolynomialMatrix(Matrix m) {
        rows = m.getNumRows();
        columns = m.getNumColumns();
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < m.getNumRows(); i++) {
            for (int j = 0; j < m.getNumColumns(); j++) {
                entries[i][j] = new Polynomial(m.getEntry(i, j));
            }
        }
    }

    //  create an identity matrix multiplied by a variable
    public PolynomialMatrix(int size) {
        rows = size;
        columns = size;
        entries = new Polynomial[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == j) {
                    entries[i][j] = new Polynomial(1);
                } else {
                    entries[i][j] = new Polynomial(0);
                }
            }
        }
    }

    public Polynomial getValue(int r, int c) {
        return entries[r][c];
    }

    public Polynomial[][] getEntries() {
        return entries;
    }

    public Polynomial[] getRow(int r) {
        return entries[r];
    }

    public Polynomial[] getColumn(int c) {
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
    public Matrix eval(double a) {
        double[][] mEntries = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mEntries[i][j] = getValue(i, j).getValueEvaluatedAt(a);
            }
        }
        return new Matrix(mEntries);
    }


    //  TESTS
    public boolean isSquare() {
        return rows == columns;
    }


    //  transpose
    public static PolynomialMatrix transpose(PolynomialMatrix a) {
        Polynomial[][] bEntries = new Polynomial[a.columns][a.rows];
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.columns; j++) {
                bEntries[j][i] = a.getValue(i, j);
            }
        }
        return new PolynomialMatrix(bEntries);
    }


    //  EIGENVALUES AND EIGENVECTORS
    //  removing a row for finding minor
    public PolynomialMatrix removeRow(int r) {
        if (r < rows) {
            Polynomial[][] mEntries = new Polynomial[rows - 1][columns];
            int i = 0;
            for (int j = 0; j < rows - 1; j++) {
                if (i == r) {
                    j--;
                } else {
                    for (int k = 0; k < columns; k++) {
                        mEntries[j][k] = getValue(i, k);
                    }
                }
                i++;
            }
            return new PolynomialMatrix(mEntries);
        } else {
            throw new InvalidDimensionsException("Cannot remove the indicated row");
        }
    }

    //  removing a column for finding minor
    public PolynomialMatrix removeColumn(int c) {
        if (c < columns) {
            return transpose(transpose(this).removeRow(c));
        } else {
            throw new InvalidDimensionsException("Cannot remove the indicated column");
        }
    }

    public Polynomial minorDet(int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            PolynomialMatrix m = this.removeRow(r).removeColumn(c);
            return m.det();
        } else {
            throw new InvalidDimensionsException("Cannot get the indicated minor");
        }
    }

    public Polynomial cofactor(int r, int c) {
        if (isSquare() && r < rows && c < columns) {
            return Operator.scalMult(pow(-1, r + c), minorDet(r, c));
        } else {
            throw new InvalidDimensionsException("Cannot get the indicated cofactor");
        }
    }

    //  to determinant of a 2 by 2 polynomial matrix
    private Polynomial det2X2() {
        if (rows == 2 && columns == 2) {
            Polynomial ad = Operator.multiply(getValue(0, 0), getValue(1, 1));
            Polynomial bc = Operator.multiply(getValue(0, 1), getValue(1, 0));
            //System.out.println(Polynomial.subtract(ad,bc));
            return Operator.subtract(ad, bc);
        } else {
            throw new InvalidDimensionsException("This method is for 2x2 polynomial matrices only");
        }
    }

    public Polynomial det() {
        if (isSquare()) {
            if (rows == 1 && columns == 1) {
                return getValue(0, 0);
            } else if (rows == 2 && columns == 2) {
                return det2X2();
            } else {
                //  cofactor expansion along first row
                Polynomial determinant = new Polynomial();
                for (int i = 0; i < columns; i++) {
                    determinant = Operator.add(determinant, Operator.multiply(getValue(0, i), cofactor(0, i)));
                }
                return determinant;
            }
        } else {
            throw new InvalidDimensionsException("Cannot get the determinant of a non-square matrix");
        }
    }


    @Override
    public String toString() {
        String display = "";
        for (int i = 0; i < rows; i++) {
            display += "[";
            for (int j = 0; j < columns; j++) {
                if (j + 1 == columns) {
                    if (i + 1 == rows) {
                        display += getValue(i, j) + "]";
                    } else {
                        display += getValue(i, j) + "]\n";
                    }
                } else {
                    display += getValue(i, j) + " ";
                }
            }
        }
        return display;
    }
}
