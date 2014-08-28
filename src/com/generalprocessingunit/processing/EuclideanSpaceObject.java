package com.generalprocessingunit.processing;

import processing.core.PVector;

public class EuclideanSpaceObject extends MathsHelpers {
    private final Orientation orientation;
    private final PVector location;

    public EuclideanSpaceObject(PVector location, Orientation orientation) {
        this.orientation = orientation;
        this.location = location;
    }

    public EuclideanSpaceObject() {
        this(new PVector(), new Orientation());
    }

    public void translateObjectCoords(PVector translation) {
        translate(add(
                PVector.mult(orientation.xAxis(), translation.x),
                PVector.mult(orientation.yAxis(), translation.y),
                PVector.mult(orientation.zAxis(), translation.z)
        ));
    }

    public void translate(PVector translation) {
        location.add(translation);
    }

    public void setLocation(PVector v){
        location.set(v);
    }

    public void setLocation(float x, float y, float z){
        location.set(x, y, z);
    }

    public void yaw(float theta) {
        orientation.yaw(theta);
    }

    public void pitch(float theta) {
        orientation.pitch(theta);
    }

    public void roll(float theta) {
        orientation.roll(theta);
    }

    public void rotate(PVector rotation){
        orientation.rotate(rotation);
    }

    public PVector location(){
        return location.get();
    }

    public float x(){
        return location.x;
    }

    public float y(){
        return location.y;
    }

    public float z() {
        return location.z;
    }

    public AxisAngle getOrientation() {
        return orientation.getOrientation();
    }

    public Quaternion getOrientationQuat() {
        return orientation.getOrientationQuat();
    }
}
