package net.adultsmath.m1zc3.r3;

import net.adultsmath.m1zc3.Vector;
import static net.adultsmath.m1zc3.Vector.*;

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
        n = cross(u, v);
    }

    //  test if a point is on the plane
    public boolean isPointOnPlane (Point p) {
        Vector v = p0.getVectorTo(p);
        if (dot(v, n) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    //  get the point on the plane closest to a specified point
    public Point getPointClosestTo (Point p) {
        return p.getPointFromVector(neg(proj(p0.getVectorTo(p), n)));
    }

    //  get the shortest distance between the plane and a specified point
    public double getShortestDistanceTo (Point p) {
        return norm(proj(p0.getVectorTo(p), n));
    }
}
