package com.generalprocessingunit.processing;

import processing.core.PVector;

public class Orientation {
    protected PVector xAxis = new PVector(1, 0, 0);
    protected PVector yAxis = new PVector(0, 1, 0);
    protected PVector zAxis = new PVector(0, 0, 1);

    protected Quaternion orientation = new Quaternion();

    public Orientation() {}

    public Orientation (float yaw, float pitch, float roll) {
        this();
        yaw(yaw);
        pitch(pitch);
        roll(roll);
    }

    private Orientation (Quaternion orientation, PVector xAxis, PVector yAxis, PVector zAxis) {
        this();
        this.orientation = new Quaternion(orientation.w, orientation.x, orientation.y, orientation.z);
        this.xAxis.set(xAxis);
        this.yAxis.set(yAxis);
        this.zAxis.set(zAxis);
    }

    //TODO: rotations can probably be combined into 1 step

    /**
     * preforms a yaw, then pitch, then roll according to the y, x, z values respectively of rotation
     * @param rotation
     */
    public void rotate(PVector rotation){
        yaw(rotation.y);
        pitch(rotation.x);
        roll(rotation.z);
    }

    /**
     * returns a new Orientation rotated relative to this orientation
     * @param rotation pitch, yaw, roll relateve to this orientation
     * @return
     */
    public Orientation rotateFrom(PVector rotation) {
        Orientation o = new Orientation(orientation, xAxis, yAxis, zAxis);
        o.yaw(rotation.y);
        o.pitch(rotation.x);
        o.roll(rotation.z);
        return o;
    }

    /**
     * rotates this orientation and its local coordinate system about its local y-Axis
     * @param theta
     */
    public void yaw(float theta) {
        Quaternion q = Quaternion.fromAxis(theta, yAxis);
        orientation = q.mult(orientation);
        xAxis = q.rotateVector(xAxis);
        zAxis = q.rotateVector(zAxis);
    }

    /**
     * rotates this orientation and its local coordinate system about its local x-Axis
     * @param theta
     */
    public void pitch(float theta) {
        Quaternion q = Quaternion.fromAxis(theta, xAxis);
        orientation = q.mult(orientation);
        yAxis = q.rotateVector(yAxis);
        zAxis = q.rotateVector(zAxis);
    }

    /**
     * rotates this orientation and its local coordinate system about its local z-Axis
     * @param theta
     */
    public void roll(float theta) {
        Quaternion q = Quaternion.fromAxis(theta, zAxis);
        orientation = q.mult(orientation);
        xAxis = q.rotateVector(xAxis);
        yAxis = q.rotateVector(yAxis);
    }

    /**
     * returns a PVector rotated about this orientation's y-axis
     * @param theta
     * @param v
     * @return
     */
    public PVector rotateVecAboutY(float theta, PVector v) {
        Quaternion q = Quaternion.fromAxis(theta, yAxis);
        return q.rotateVector(v);
    }

    /**
     * returns a PVector rotated about this orientation's x-axis
     * @param theta
     * @param v
     * @return
     */
    public PVector rotateVecAboutX(float theta, PVector v) {
        Quaternion q = Quaternion.fromAxis(theta, xAxis);
        return q.rotateVector(v);
    }

    /**
     * returns a PVector rotated about this orientation's z-axis
     * @param theta
     * @param v
     * @return
     */
    public PVector rotateVecAboutZ(float theta, PVector v) {
        Quaternion q = Quaternion.fromAxis(theta, zAxis);
        return q.rotateVector(v);
    }


    public AxisAngle getOrientation() {
        return orientation.getAxisAngle();
    }

    public PVector xAxis() {
        return xAxis.get();
    }

    public PVector yAxis() {
        return yAxis.get();
    }

    public PVector zAxis() {
        return zAxis.get();
    }

    public Quaternion getOrientationQuat() {
        return orientation;
    }
}
