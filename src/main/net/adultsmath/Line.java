package main.net.adultsmath;

public class Line {
    private final Point p0;

    public Point getP0() {
        return p0;
    }

    public Vector getVector() {
        return vector;
    }

    private final Vector vector;

    //  construct from a point and a vector
    public Line (Point p, Vector vector) {
        if (vector.getSize() != 3) throw new IllegalArgumentException("The input vector is not in R3");
        p0 = p;
        this.vector = vector;
    }

    //  construct from two points
    public Line (Point p, Point q) {
        p0 = p;
        vector = p.getVectorTo(q);
    }

    //  test if a point is on the line
    public boolean isPointOnLine (Point p) {
        double t = (p.getX() - p0.getX()) / vector.getComps(0);
        if (p.getY() == p0.getY() + vector.getComps(1) * t && p.getZ() == p0.getZ() + vector.getComps(2) * t) {
            return true;
        }
        else {
            return false;
        }
    }

    //  get the point on the line closest to a specified point
    public Point getPointClosestTo (Point p) {
        Vector u = Operator.neg(Operator.orth(p0.getVectorTo(p), vector));
        return p.getPointFromVector(u);
    }

    //  get the shortest distance between the line and a specified point
    public double getShortestDistanceTo (Point p) {
        return Operator.orth(p0.getVectorTo(p), vector).getNorm();
    }

    //  show parametric equations
    @Override
    public String toString () {
        String x = "x = " + p0.getX() + " + " + vector.getComps(0) + "t";
        String y = "y = " + p0.getY() + " + " + vector.getComps(1) + "t";
        String z = "z = " + p0.getZ() + " + " + vector.getComps(2) + "t";
        return x + "\n" + y + "\n" + z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Line line) {
            return this.getVector().isScalarMultiple(line.getVector()) && this.isPointOnLine(line.getP0());
        }
        return false;
    }
}
