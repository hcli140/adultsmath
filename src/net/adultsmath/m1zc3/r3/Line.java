package net.adultsmath.m1zc3.r3;

import net.adultsmath.m1zc3.Operator;
import net.adultsmath.m1zc3.VectorRn;

public class Line {
    private Point p0;
    private final VectorRn v;

    //  construct from a point and a vector
    public Line (Point p, VectorRn v) {
        p0 = p;
        this.v = v.copy(3);
    }

    //  construct from two points
    public Line (Point p, Point q) {
        p0 = p;
        v = p.getVectorTo(q);
    }

    //  move reference point
    public void newP0 (double t) {
        double[] coords = p0.getCoords();
        for (int i = 0; i < 3; i++) {
            coords[i] += t * v.getComps(i);
        }
        p0 = new Point(coords[0],coords[1],coords[2]);
    }

    //  test if a point is on the line
    public boolean isPointOnLine (Point p) {
        double t = (p.getX() - p0.getX()) / v.getComps(0);
        if (p.getY() == p0.getY() + v.getComps(1) * t && p.getZ() == p0.getZ() + v.getComps(2) * t) {
            return true;
        }
        else {
            return false;
        }
    }

    //  get the point on the line closest to a specified point
    public Point getPointClosestTo (Point p) {
        VectorRn u = Operator.neg(Operator.orth(p0.getVectorTo(p),v));
        return p.getPointFromVector(u);
    }

    //  get the shortest distance between the line and a specified point
    public double getShortestDistanceTo (Point p) {
        return Operator.orth(p0.getVectorTo(p),v).norm();
    }

    //  show parametric equations
    public String showParametricEq () {
        String x = "x = " + p0.getX() + " + " + v.getComps(0) + "t";
        String y = "y = " + p0.getY() + " + " + v.getComps(1) + "t";
        String z = "z = " + p0.getZ() + " + " + v.getComps(2) + "t";
        return x + "\n" + y + "\n" + z;
    }
}
