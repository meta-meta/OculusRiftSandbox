package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.hid.RazerHydra;
import com.generalprocessingunit.hid.RazerHydraManager;
import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.MomentumVector;
import processing.core.PApplet;
import processing.core.PVector;

public class SpaceNavigatorDemo extends PApplet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        PApplet.main(new String[] { /*"--present",*/ SpaceNavigatorDemo.class.getCanonicalName() });
    }

    SpaceNavigator spaceNavigator;
    RazerHydraManager razerHydraManager;

    MomentumVector momentum = new MomentumVector(this, .001f);
    MomentumVector rotMomentum = new MomentumVector(this, .001f);

    EuclideanSpaceObject ship = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChild = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChild2 = new EuclideanSpaceObject();
    EuclideanSpaceObject shipChildChild = new EuclideanSpaceObject();

    EuclideanSpaceObject hydraChild = new EuclideanSpaceObject();

    public void setup() {
        size(1280, 720, P3D);

        spaceNavigator = new SpaceNavigator(this);
        razerHydraManager = new RazerHydraManager();
        razerHydraManager.razerHydras[0].addChild(hydraChild, new PVector(0, 0, -30), new PVector());

        ship.addChild(shipChild, new PVector(20f, 10f, -10f), new PVector());
        ship.addChild(shipChild2, new PVector(-20f, 10f, -10f), new PVector());

        shipChild.addChild(shipChildChild, new PVector(0, 0, 10f), new PVector());
    }

    public void draw() {
        background(33, 170, 170);

        updateScene();

        camera(
                0, 50, -500,
                0, 0, 0,
                0, -1, 0
        );


        pushMatrix();
        {
            translate(100, 100, 100);
            fill(255, 0, 0);
            box(40);
        }
        popMatrix();

        pushMatrix();
        {
            translate(100, 100, -100);
            fill(0, 0, 255);
            box(40);
        }
        popMatrix();

        pushMatrix();
        {
            translate(ship.x(), ship.y(), ship.z());

            AxisAngle aa = ship.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);

            fill(204, 102, 0);
            box(20, 30, 20);

            pushMatrix();
            {
                fill(0);
                translate(0, -15, 15);
                box(30, 3, 70);
            }
            popMatrix();

            pushMatrix();
            {
                translate(0, 0, 30);

                fill(200, 40, 200);
                box(20, 20, 20);
            }
            popMatrix();
        }
        popMatrix();

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


        pushMatrix(); // hydra
        {
            RazerHydra hydra = razerHydraManager.razerHydras[0];
            PVector loc = hydra.getLocation();
            translate(loc.x, loc.y, loc.z);

            AxisAngle aa = hydra.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);
            fill(60);
            box(25, 5, 50);
        }
        popMatrix();

        pushMatrix(); // hydra child
        {
            PVector loc = hydraChild.getLocation();
            translate(loc.x, loc.y, loc.z);

            AxisAngle aa = hydraChild.getAxisAngle();
            rotate(aa.w, aa.x, aa.y, aa.z);
            fill(120);
            box(5, 5, 5);
        }
        popMatrix();

    }

    public void updateScene() {
        spaceNavigator.poll();
        razerHydraManager.poll();

        PVector t = PVector.mult(spaceNavigator.translation, .1f);
        momentum.add(t);

        println(t);

        rotMomentum.add(PVector.mult(spaceNavigator.rotation, .1f));

        momentum.friction();
        rotMomentum.friction();

        ship.translateWRTObjectCoords(momentum.getValue());
        ship.rotate(rotMomentum.getValue());

        shipChild.roll(.1f);
        shipChild2.roll(-.1f);
        shipChildChild.yaw(.2f);

        RazerHydra hydra = razerHydraManager.razerHydras[0];
        hydra.setLocation(PVector.mult(hydra.getLocation(), 300));
        ship.translateAndRotateObjectWRTObjectCoords(hydra);
    }
}