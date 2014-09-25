package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.MomentumVector;
import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.hid.SpaceNavigator;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;

public class ExampleWithSpaceNavigator extends Example {


    public static void main(String[] args){
        PAppletVR.main(ExampleWithSpaceNavigator.class);
    }

    SpaceNavigator spaceNav;
    MomentumVector momentum = new MomentumVector(this, 0.0125f);
    MomentumVector rotMomentum = new MomentumVector(this, 0.0125f);

    static final float speedCoef = 0.004f;
    static final float rotSpeedCoef = 0.015f;

    static boolean invertedControl = true;

    @Override
    public void setup() {
        super.setup();

        spaceNav = new SpaceNavigator(this);
    }

    @Override
    protected void updateState() {
        spaceNav.poll();

        int i = invertedControl ? -1 : 1;
        momentum.add(PVector.mult(spaceNav.translation, i * speedCoef));
        rotMomentum.add(PVector.mult(spaceNav.rotation, i * rotSpeedCoef));

        momentum.friction();
        rotMomentum.friction();

        headContainer.translateWRTObjectCoords(momentum.getValue());
        headContainer.rotate(rotMomentum.getValue());

        if(headContainer.y() < 2) {
            PVector location = headContainer.getLocation();
            location.y = 2;
            headContainer.setLocation(location);
        }
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {
        pG.colorMode(RGB);
        pG.fill(255);
        pG.noStroke();

        pG.sphereDetail(5);
        float shipDiameter = .5f;
        for (float x = -shipDiameter; x <= shipDiameter; x += shipDiameter) {
            for (float y = -shipDiameter; y <= shipDiameter; y += shipDiameter) {
                for (float z = -shipDiameter; z <= shipDiameter * 2; z += shipDiameter * 2) {
                    if (abs(x) + abs(y) + abs(z) < Float.MIN_VALUE) {
                        continue;
                    }
                    pG.pushMatrix();
                    pG.translate(x, y, z);
                    pG.sphere(0.01f);
                    pG.popMatrix();
                }

            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'i') {
           invertedControl = !invertedControl;
        }

        super.keyPressed(e);
    }
}
