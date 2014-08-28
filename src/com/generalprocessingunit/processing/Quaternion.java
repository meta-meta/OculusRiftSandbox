package com.generalprocessingunit.processing;

import processing.core.PVector;

public class Quaternion extends AxisAngle {
    public Quaternion(float w, float x, float y, float z) {
        super(w, x, y, z);
    }

    public Quaternion() {
        this (1, 0, 0, 0);
    }

    public static Quaternion fromAxis(float theta, PVector axis) {
        return fromAxis(theta, axis.x, axis.y, axis.z);
    }

    /** http://www.cprogramming.com/tutorial/3d/quaternions.html
     *
     *  local_rotation.w  = cosf( fAngle/2)
     *  local_rotation.x = axis.x * sinf( fAngle/2 )
     *  local_rotation.y = axis.y * sinf( fAngle/2 )
     *  local_rotation.z = axis.z * sinf( fAngle/2 )
     * */
    public static Quaternion fromAxis(float theta, float x, float y, float z) {
        float halfTheta = theta / 2;
        return new Quaternion(
                cos(halfTheta),
                x * sin(halfTheta),
                y * sin(halfTheta),
                z * sin(halfTheta)
        );
    }


    /** http://www.cprogramming.com/tutorial/3d/quaternions.html
     * Let Q1 and Q2 be two quaternions, which are defined, respectively, as (w1, x1, y1, z1) and (w2, x2, y2, z2).
     (Q1 * Q2).w = (w1w2 - x1x2 - y1y2 - z1z2)
     (Q1 * Q2).x = (w1x2 + x1w2 + y1z2 - z1y2)
     (Q1 * Q2).y = (w1y2 - x1z2 + y1w2 + z1x2)
     (Q1 * Q2).z = (w1z2 + x1y2 - y1x2 + z1w2

     */
    public Quaternion mult(Quaternion q) {
        return new Quaternion(
                w * q.w - x * q.x - y * q.y - z * q.z,
                w * q.x + x * q.w + y * q.z - z * q.y,
                w * q.y - x * q.z + y * q.w + z * q.x,
                w * q.z + x * q.y - y * q.x + z * q.w
        );
    }


    /** adapted from
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/
     * */
    public AxisAngle getAxisAngle() {
        Quaternion q = normalize(this);
        AxisAngle aa = new AxisAngle();
        aa.w = 2 * acos(q.w);
        float s = sqrt(1-sq(q.w)); // assuming quaternion normalised then w is less than 1, so term always positive.
        if (s < Float.MIN_VALUE) { // test to avoid divide by zero, s is always positive due to sqrt
            // if s close to zero then direction of axis not important
            aa.x = q.x; // if it is important that axis is normalised then replace with x=1; y=z=0;
            aa.y = q.y;
            aa.z = q.z;
        } else {
            aa.x = q.x / s; // normalize axis
            aa.y = q.y / s;
            aa.z = q.z / s;
        }

        return aa;
    }

    /** http://molecularmusings.wordpress.com/2013/05/24/a-faster-quaternion-vector-multiplication/
     *   t = 2 * cross(q.xyz, v)
         v' = v + q.w * t + cross(q.xyz, t)

     * */
    public PVector rotateVector (PVector v) {
        PVector qVec = new PVector(x, y, z);
        PVector t = PVector.mult(qVec.cross(v), 2);
        return PVector.add(PVector.add(v, PVector.mult(t, w)), qVec.cross(t));
    }

    public PVector rotateVector (float x, float y, float z) {
        return rotateVector(new PVector(x, y, z));
    }

    public static Quaternion normalize(Quaternion q) {
        float norm = sq(q.w) + sq(q.x) + sq(q.y) + sq(q.z);
        if(abs(norm - 1) > Float.MIN_VALUE) {
            norm = sqrt(norm);
            return new Quaternion(
                    q.w / norm,
                    q.x / norm,
                    q.y / norm,
                    q.z / norm
            );
        } else {
            return q;
        }
    }

    //TODO: go through this http://3dgep.com/understanding-quaternions/
}
