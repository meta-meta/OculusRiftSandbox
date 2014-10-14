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

    public EuclideanSpaceObject(PVector location, Orientation orientation) {
        this.orientation = orientation;
        this.location = location;
    }

    public EuclideanSpaceObject() {
        this(new PVector(), new Orientation());
    }

    public void addChild(EuclideanSpaceObject child, PVector locationWRTParent, YawPitchRoll rotationWRTParent) {

        child.locationWRTParent = locationWRTParent;
        child.rotationWRTParent = rotationWRTParent;
        child.location.set(getTranslationWRTObjectCoords(locationWRTParent));
        child.orientation = orientation.rotateFrom(rotationWRTParent);

        child.parent = this;
        children.add(child);

        addProgeny(child);
        addProgeny(child.progeny);
    }

    public void addChild(EuclideanSpaceObject child) {
        addChild(child, new PVector(), new YawPitchRoll());
    }

    public void addChild(EuclideanSpaceObject child, PVector locationWRTParent) {
        addChild(child, locationWRTParent, new YawPitchRoll());
    }

    public void addChild(EuclideanSpaceObject child, YawPitchRoll rotationWRTParent) {
        addChild(child, new PVector(), rotationWRTParent);
    }

    public void clearChildren() {
        children.clear();
        progeny.clear();
    }

    private void addProgeny(EuclideanSpaceObject child) {
        progeny.add(child);
        if(null != parent) {
            parent.addProgeny(child);
        }
    }

    private void addProgeny(Set<EuclideanSpaceObject> children) {
        for (EuclideanSpaceObject child : children) {
            addProgeny(child);
        }
    }

    public PVector getTranslationWRTObjectCoords(PVector locationWRTParent) {
        return add(
                getLocationRelativeToThisObject(locationWRTParent),
                location
        );
    }

    private PVector getLocationRelativeToThisObject(PVector locationWRTParent) {
        return add(
                PVector.mult(orientation.xAxis(), locationWRTParent.x),
                PVector.mult(orientation.yAxis(), locationWRTParent.y),
                PVector.mult(orientation.zAxis(), locationWRTParent.z)
        );
    }

    public YawPitchRoll getDeltaYawPitchRollFromThisObject(Orientation orientation) {
        return new YawPitchRoll(
                PVector.angleBetween(this.orientation.yAxis, orientation.yAxis),
                PVector.angleBetween(this.orientation.xAxis, orientation.xAxis),
                PVector.angleBetween(this.orientation.zAxis, orientation.zAxis)
        );
    }

    /**
     * get local vector from world vector
     * http://stackoverflow.com/questions/21125987/how-do-i-convert-a-coordinate-in-one-3d-cartesian-coordinate-system-to-another-3
     * @param worldCoords
     * @return
     */
    public PVector localCoordinatesFromWorld(PVector worldCoords) {
        Mat3 local = new Mat3(orientation.xAxis, orientation.yAxis, orientation.zAxis);

        // O2 - O1 is just 0,0,0 - location
        PVector dOrigin = new PVector();
        dOrigin.sub(location);

        return local.transpose().mult(PVector.add(dOrigin, worldCoords));
    }

    class Mat3 {
        float a11, a12, a13, a21, a22, a23, a31, a32, a33;

        Mat3(float a11, float a12, float a13, float a21, float a22, float a23, float a31, float a32, float a33) {
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a31 = a31;
            this.a32 = a32;
            this.a33 = a33;
        }

        Mat3(PVector x, PVector y, PVector z) {
            this(x.x, x.y, x.z, y.x, y.y, y.z, z.x, z.y, z.z);
        }

        Mat3 transpose() {
            return new Mat3(a11, a21, a31, a12, a22, a32, a13, a23, a33);
        }

        PVector mult(PVector v) {
            return new PVector(
                    v.x * a11 + v.y * a12 + v.z * a13,
                    v.x * a21 + v.y * a22 + v.z * a23,
                    v.x * a31 + v.y * a32 + v.z * a33
            );
        }
    }

    /**
     * places the object relative to this object's local coordinates without adding it as a child
     * @param obj
     */
    public void translateAndRotateObjectWRTObjectCoords(EuclideanSpaceObject obj) {
        obj.setLocation(getTranslationWRTObjectCoords(obj.location));

        obj.setOrientation(orientation.rotateFrom(obj.orientation));
    }

    public void translateWRTObjectCoords(PVector translation) {
        translate(getLocationRelativeToThisObject(translation));
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
                p.location.add(t);
            }
        }

        //TODO: what if this object has a parent?
//        if(null != parent) {
//            location.set(add(
//                    PVector.mult(parent.orientation.xAxis(), locationWRTParent.x),
//                    PVector.mult(parent.orientation.yAxis(), locationWRTParent.y),
//                    PVector.mult(parent.orientation.zAxis(), locationWRTParent.z),
//                    v
//            ));
//
//            orientation = parent.orientation.rotateFrom(rotationWRTParent);
//        }

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

    public void rotate(YawPitchRoll rotation) {
        yaw(rotation.yaw());
        pitch(rotation.pitch());
        roll(rotation.roll());
    }

    public void rotate(Quaternion rotation) {
        orientation.rotate(rotation);
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

    public PVector locationWRTParent;
    public YawPitchRoll rotationWRTParent;

    public float getDistFrom(EuclideanSpaceObject that) {
        return getDistFrom(that.location);
    }

    public float getDistFrom(PVector that) {
        return PVector.dist(this.location, that);
    }

    public void setOrientation(YawPitchRoll rotation) {
        //TODO:   set progeny new orientation
        orientation = new Orientation(rotation);
        setOrientation(orientation);
    }

    public void setOrientation(Orientation o) {
        orientation = o;

        // TODO: THIS IS A HACK It does not preserve the progeny's orientation
        for(EuclideanSpaceObject p : progeny) {
            p.orientation = orientation.rotateFrom(p.rotationWRTParent);
        }

        for(EuclideanSpaceObject c : children) {
            c.location.set(getTranslationWRTObjectCoords(c.locationWRTParent));
            c.adjustChildren();
        }
    }

    private void adjustChildren() {
        for(EuclideanSpaceObject c : children) {
            c.location.set(getTranslationWRTObjectCoords(c.locationWRTParent));
            c.orientation = orientation.rotateFrom(c.rotationWRTParent);
            c.adjustChildren();
        }
    }

    public AxisAngle getAxisAngle() {
        return orientation.getOrientation();
    }

    public Quaternion getOrientationQuat() {
        return orientation.getOrientationQuat();
    }
}
