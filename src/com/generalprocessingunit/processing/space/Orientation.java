package com.generalprocessingunit.processing.space;

import processing.core.PVector;

public class Orientation {
    protected PVector xAxis = new PVector(1, 0, 0);
    protected PVector yAxis = new PVector(0, 1, 0);
    protected PVector zAxis = new PVector(0, 0, 1);

    protected Quaternion orientation = new Quaternion();

    public Orientation() {}

    public Orientation (YawPitchRoll rotation) {
        this();
        yaw(rotation.yaw());
        pitch(rotation.pitch());
        roll(rotation.roll());
    }

    private Orientation (Quaternion orientation, PVector xAxis, PVector yAxis, PVector zAxis) {
        this();
        this.orientation = new Quaternion(orientation.w, orientation.x, orientation.y, orientation.z);
        this.xAxis.set(xAxis);
        this.yAxis.set(yAxis);
        this.zAxis.set(zAxis);
    }

    public Orientation clone() {
        return new Orientation(orientation, xAxis, yAxis, zAxis);
    }

//    public static Orientation fromVector(PVector direction) {
//        Orientation o = new Orientation();
//        direction.normalize();
//        o.orientation = Quaternion.fromAxis(-PConstants.PI, direction);
//        o.xAxis =  o.orientation.rotateVector(o.xAxis);
//        o.yAxis = o.orientation.rotateVector(o.yAxis);
//        o.zAxis = o.orientation.rotateVector(o.zAxis);
//        return o;
//    }

    //TODO: rotations can probably be combined into 1 step

    /**
     * preforms a yaw, then pitch, then roll according to the y, x, z values respectively of rotation
     * @param rotation
     */
    public void rotate(YawPitchRoll rotation){
        yaw(rotation.yaw());
        pitch(rotation.pitch());
        roll(rotation.roll());
    }

    /**
     * rotates by a Quaternion. TODO: need to test this
     * @param rotation
     */
    public void rotate(Quaternion rotation) {
        orientation = rotation.mult(orientation);
        xAxis = rotation.rotateVector(xAxis);
        yAxis = rotation.rotateVector(yAxis);
        zAxis = rotation.rotateVector(zAxis);

        xAxis.normalize();
        yAxis.normalize();
        zAxis.normalize();
    }

    /**
     * returns a new Orientation rotated relative to this orientation
     * @param rotation pitch, yaw, roll relateve to this orientation
     * @return
     */
    public Orientation rotateFrom(YawPitchRoll rotation) {
        Orientation o = new Orientation(orientation, xAxis, yAxis, zAxis);
        o.yaw(rotation.yaw());
        o.pitch(rotation.pitch());
        o.roll(rotation.roll());
        return o;
    }

    /**
     * returns a new Orientation relative to this orientation
     * @param o
     */
    public Orientation rotateFrom(Orientation o) {
        Orientation newO = new Orientation();
        newO.orientation = orientation.mult(o.orientation);
        newO.xAxis = orientation.rotateVector(o.xAxis);
        newO.yAxis = orientation.rotateVector(o.yAxis);
        newO.zAxis = orientation.rotateVector(o.zAxis);
        return newO;
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
