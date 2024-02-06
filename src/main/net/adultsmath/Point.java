package main.net.adultsmath;

import java.util.List;

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

    public Point (List<Double> coords) {
        if (coords.size() != 3) throw new IllegalArgumentException("Points need three numbers");
        this.x = coords.get(0);
        this.y = coords.get(1);
        this.z = coords.get(2);
    }

    //  construct origin
    public Point () {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    //  GETTERS
    public List<Double> getCoords () {
        return List.of(x, y, z);
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
    @Override
    public String toString () {
        String outputString = "P(" + x + ", " + y + ", " + z + ")";
        return outputString;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return this.getX() == p.getX() && this.getY() == p.getY() && this.getZ() == this.getZ();
        }
        return false;
    }
}
