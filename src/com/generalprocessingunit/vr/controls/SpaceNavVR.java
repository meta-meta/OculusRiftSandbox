package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.MomentumVector;
import com.generalprocessingunit.processing.MomentumYawPitchRoll;
import com.generalprocessingunit.vr.PAppletVR;
import processing.core.PVector;

public class SpaceNavVR {
    PAppletVR p5;

    SpaceNavigator spaceNav;
    public MomentumVector momentum;
    public MomentumYawPitchRoll rotMomentum;

    static float speedCoef = 0.004f;
    static float rotSpeedCoef = 0.015f;

    static boolean invertedControl = true;

    public SpaceNavVR(PAppletVR p5, float friction, float speed) {
        this.p5 = p5;
        spaceNav = new SpaceNavigator(p5);
        momentum = new MomentumVector(p5, friction);
        rotMomentum = new MomentumYawPitchRoll(p5, friction);

        speedCoef *= speed;
    }

    public void update() {
        spaceNav.poll();

        int i = invertedControl ? -1 : 1;
        momentum.add(PVector.mult(PVector.mult(spaceNav.translation, spaceNav.translation.mag() / 3f), i * speedCoef));
        rotMomentum.add(PVector.mult(spaceNav.rotation, i * rotSpeedCoef));

        momentum.friction();
        rotMomentum.friction();

        p5.headContainer.translateWRTObjectCoords(momentum.getValue());
        p5.headContainer.rotate(rotMomentum.getValue());
    }

    public void invertControl(){
        invertedControl = !invertedControl;
    }
}
