package net.adultsmath.m1zc3.r3;

import net.adultsmath.m1zc3.Vector;

public class Point {
    private final double x;
    private final double y;
    private final double z;

    //  construct from three components
    public Point (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //  construct origin
    public Point () {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    //  GETTERS
    public double[] getCoords () {
        return new double[]{x,y,z};
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    //  get the vector from this to another point
    public Vector getVectorTo (Point p) {
        double dx = p.x - this.x;
        double dy = p.y - this.y;
        double dz = p.z - this.z;
        return new Vector(dx, dy, dz);
    }

    //  get the point from this point and a vector
    public Point getPointFromVector (Vector v) {
        double x = this.x + v.getComps(0);
        double y = this.y + v.getComps(1);
        double z = this.z + v.getComps(2);
        return new Point(x, y, z);
    }


    //  show the point as a string
    public String show () {
        String outputString = "P(" + x + ", " + y + ", " + z + ")";
        return outputString;
    }
}
