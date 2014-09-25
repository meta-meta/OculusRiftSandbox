package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.MomentumVector;
import processing.core.PApplet;
import processing.core.PVector;
import procontroll.ControllButton;
import procontroll.ControllDevice;
import procontroll.ControllIO;
import procontroll.ControllSlider;

public class SpaceNavigatorDemo extends PApplet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        PApplet.main(new String[] { /*"--present",*/ SpaceNavigatorDemo.class.getCanonicalName() });
    }

    SpaceNavigator spaceNavigator;

    MomentumVector momentum = new MomentumVector(this, .001f);
    MomentumVector rotMomentum = new MomentumVector(this, .001f);

    EuclideanSpaceObject ship = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChild = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChild2 = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChildChild = new EuclideanSpaceObject();

    public void setup() {
        size(1280, 720, P3D);

        spaceNavigator = new SpaceNavigator(this);

        ship.addChild(shipChild, new PVector(20f, 10f, 10f), new PVector());
        ship.addChild(shipChild2, new PVector(-20f, 10f, 10f), new PVector());

        shipChild.addChild(shipChildChild, new PVector(0, 0, -10f), new PVector());
    }

    public void draw() {
        background(33, 170, 170);

        updateScene();

        translate(width/2, height/2);


        pushMatrix(); // shipChild
        {
            translate(shipChildChild.x(), shipChildChild.y(), shipChildChild.z());

            AxisAngle aa = shipChildChild.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);
            box(5);
        }
        popMatrix();

        pushMatrix(); // shipChild
        {
            translate(shipChild.x(), shipChild.y(), shipChild.z());

            AxisAngle aa = shipChild.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);
            box(10);
        }
        popMatrix();

        pushMatrix(); // shipChild
        {
            translate(shipChild2.x(), shipChild2.y(), shipChild2.z());

            AxisAngle aa = shipChild2.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);
            box(10);
        }
        popMatrix();


        pushMatrix();

        translate(ship.x(), ship.y(), ship.z());

        AxisAngle aa = ship.getAxisAngle();
        rotate(aa.w, aa.x, aa.y, aa.z);

        fill(204, 102, 0);
        box(20, 30, 20);

        pushMatrix();
        fill(0);
        translate(0, 15, -15);
        box(30, 3, 70);
        popMatrix();

        pushMatrix();
        translate(0, 0, -30);

        fill(200, 40, 200);
        box(20, 20, 20);
        popMatrix();

        popMatrix();
    }

    public void updateScene() {
        spaceNavigator.poll();

        PVector t = PVector.mult(spaceNavigator.translation, .1f);
        momentum.add(t.x, -t.y, -t.z);

        rotMomentum.add(PVector.mult(spaceNavigator.rotation, .1f));

        momentum.friction();
        rotMomentum.friction();

        ship.translateWRTObjectCoords(momentum.getValue());
        ship.rotate(rotMomentum.getValue());

        shipChild.roll(.1f);
        shipChild2.roll(-.1f);
        shipChildChild.yaw(.2f);
    }
}