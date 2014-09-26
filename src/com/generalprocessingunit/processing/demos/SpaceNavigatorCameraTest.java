package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.MomentumVector;
import processing.core.PApplet;
import processing.core.PVector;

public class SpaceNavigatorCameraTest extends PApplet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        PApplet.main(new String[] { /*"--present",*/ SpaceNavigatorCameraTest.class.getCanonicalName() });
    }

    SpaceNavigator spaceNavigator;

    MomentumVector momentum = new MomentumVector(this, .001f);
    MomentumVector rotMomentum = new MomentumVector(this, .001f);

    EuclideanSpaceObject camera = new EuclideanSpaceObject();

    public void setup() {
        size(1280, 720, P3D);

        spaceNavigator = new SpaceNavigator(this);

        camera.setLocation(0, 0, -50);
    }

    public void draw() {
        background(33, 170, 170);

        updateScene();

        PVector cam = camera.getLocation();
        PVector lookat = camera.getOrientation().zAxis();
        lookat.add(cam);
        PVector up = camera.getOrientation().yAxis();
        up.mult(-1);

        camera(
                cam.x, cam.y, cam.z,
                lookat.x, lookat.y, lookat.z,
                up.x, up.y, up.z
        );

//        perspective(PI / 2.8f, width / height, 0.1f, 10000f);

        box(90);

        pushMatrix();
        translate(0, 90, 0);
        box(10);
        popMatrix();
    }

    public void updateScene() {
        spaceNavigator.poll();

        PVector t = PVector.mult(spaceNavigator.translation, .1f);
        momentum.add(t);

        rotMomentum.add(PVector.mult(spaceNavigator.rotation, .1f));

        momentum.friction();
        rotMomentum.friction();

        camera.translateWRTObjectCoords(momentum.getValue());
        camera.rotate(rotMomentum.getValue());
    }
}