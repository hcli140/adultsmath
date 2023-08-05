package net.adultsmath.m1zc3.r3;

import net.adultsmath.m1zc3.Operator;
import net.adultsmath.m1zc3.VectorRn;
import static net.adultsmath.m1zc3.VectorRn.*;

public class Plane {
    private final VectorRn n;
    private final Point p0;

    //  construct from normal vector and point
    public Plane (Point p, VectorRn n) {
        this.n = n;
        this.p0 = p;
    }

    //  construct from three points
    public Plane (Point p, Point q, Point r) {
        VectorRn u = p.getVectorTo(q);
        VectorRn v = p.getVectorTo(r);
        p0 = p;
        n = Operator.cross(u, v);
    }

    //  test if a point is on the plane
    public boolean isPointOnPlane (Point p) {
        VectorRn v = p0.getVectorTo(p);
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
        return Operator.proj(p0.getVectorTo(p), n).norm();
    }
}
