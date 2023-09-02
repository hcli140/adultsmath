package main.net.adultsmath.m1zc3.r3;

import main.net.adultsmath.m1zc3.Operator;
import main.net.adultsmath.m1zc3.Vector;

public class Plane {
    private final Vector n;
    private final Point p0;

    //  construct from normal vector and point
    public Plane (Point p, Vector n) {
        this.n = n;
        this.p0 = p;
    }

    //  construct from three points
    public Plane (Point p, Point q, Point r) {
        Vector u = p.getVectorTo(q);
        Vector v = p.getVectorTo(r);
        p0 = p;
        n = Operator.cross(u, v);
    }

    public Vector getN() {return n;}
    public Point getP0() {return p0;}

    //  test if a point is on the plane
    public boolean isPointOnPlane (Point p) {
        Vector v = p0.getVectorTo(p);
        if (Operator.dot(v, n) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    //  get the point on the plane closest to a specified point
    public Point getPointClosestTo (Point p) {
        return p.getPointFromVector(Operator.neg(Operator.proj(p0.getVectorTo(p), n)));
    }

    //  get the shortest distance between the plane and a specified point
    public double getShortestDistanceTo (Point p) {
        return Operator.proj(p0.getVectorTo(p), n).getNorm();
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Plane) {
            Plane plane = (Plane) obj;
            return this.getN().isScalarMultiple(plane.getN()) && this.isPointOnPlane(plane.getP0());
        }
        return false;
    }
}
