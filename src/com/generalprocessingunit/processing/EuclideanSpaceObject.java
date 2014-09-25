package com.generalprocessingunit.processing;

import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;

public class EuclideanSpaceObject extends MathsHelpers {
    private Orientation orientation;
    private final PVector location;

    private EuclideanSpaceObject parent = null;
    private Set<EuclideanSpaceObject> progeny = new HashSet<>();
    private Set<EuclideanSpaceObject> children = new HashSet<>();


    public Set<EuclideanSpaceObject> getAllChildren() {
        Set<EuclideanSpaceObject> c = new HashSet<>();
        for (EuclideanSpaceObject child : children) {
            c.addAll(child.getAllChildren());
        }

        return c;
    }

    public void addChild(EuclideanSpaceObject child, PVector locationWRTParent, PVector rotationWRTParent) {

        child.location.set(add(
                PVector.mult(orientation.xAxis(), locationWRTParent.x),
                PVector.mult(orientation.yAxis(), locationWRTParent.y),
                PVector.mult(orientation.zAxis(), locationWRTParent.z),
                location
        ));

        child.orientation = orientation.rotateFrom(rotationWRTParent);

        child.parent = this;
        addProgeny(child);
        addProgeny(child.progeny);

        if (parent != null) {
            parent.addProgeny(progeny);
        }
    }

    private void addProgeny(EuclideanSpaceObject child) {
        progeny.add(child);
    }

    private void addProgeny(Set<EuclideanSpaceObject> children) {
        for (EuclideanSpaceObject child : children) {
            addProgeny(child);
        }
    }

    public EuclideanSpaceObject(PVector location, Orientation orientation) {
        this.orientation = orientation;
        this.location = location;
    }

    public EuclideanSpaceObject() {
        this(new PVector(), new Orientation());
    }

    public void translateWRTObjectCoords(PVector translation) {
        translate(add(
                PVector.mult(orientation.xAxis(), translation.x),
                PVector.mult(orientation.yAxis(), translation.y),
                PVector.mult(orientation.zAxis(), translation.z)
        ));
    }

    public void translate(PVector translation) {
        location.add(translation);

        for (EuclideanSpaceObject p : progeny) {
            p.location.add(translation);
        }
    }

    public void setLocation(PVector v) {
        if (progeny.size() > 0) {
            // translate all progeny by the same amount
            PVector t = PVector.sub(v, location);
            for (EuclideanSpaceObject p : progeny) {
                p.translate(t);
            }
        }

        location.set(v);
    }

    public void setLocation(float x, float y, float z) {
        setLocation(new PVector(x, y, z));
    }

    public void yaw(float theta) {
        revolveProgeny(theta, orientation.yAxis);
        orientation.yaw(theta);
    }

    public void pitch(float theta) {
        revolveProgeny(theta, orientation.xAxis);
        orientation.pitch(theta);
    }

    public void roll(float theta) {
        revolveProgeny(theta, orientation.zAxis);
        orientation.roll(theta);
    }

    private void revolveProgeny(float theta, PVector axis) {
        for (EuclideanSpaceObject p : progeny) {
            Quaternion q = Quaternion.fromAxis(theta, axis);
            p.orientation.orientation = q.mult(p.orientation.orientation);
            p.orientation.xAxis = q.rotateVector(p.orientation.xAxis);
            p.orientation.yAxis = q.rotateVector(p.orientation.yAxis);
            p.orientation.zAxis = q.rotateVector(p.orientation.zAxis);

            PVector relLocation = PVector.sub(p.location, location);
            relLocation = q.rotateVector(relLocation);
            p.location.set(add(location, relLocation));
        }
    }

    public void rotate(PVector rotation) {
        yaw(rotation.y);
        pitch(rotation.x);
        roll(rotation.z);
    }

    public PVector getLocation() {
        return location.get();
    }

    public float x() {
        return location.x;
    }

    public float y() {
        return location.y;
    }

    public float z() {
        return location.z;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(float yaw, float pitch, float roll) {
        //TODO:   set progeny new orientation
        orientation = new Orientation(yaw, pitch, roll);
    }

    public void setOrientation(Orientation o) {
        orientation = o;
    }

    public AxisAngle getAxisAngle() {
        return orientation.getOrientation();
    }

    public Quaternion getOrientationQuat() {
        return orientation.getOrientationQuat();
    }
}
