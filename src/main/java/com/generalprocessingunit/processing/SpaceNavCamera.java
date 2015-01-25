package com.generalprocessingunit.processing;

import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.space.Camera;
import processing.core.PApplet;
import processing.core.PVector;

public class SpaceNavCamera extends Camera {
    SpaceNavigator spaceNavigator;

    public MomentumVector momentum;
    public MomentumYawPitchRoll rotMomentum;

    public boolean inverseControls = true;

    public SpaceNavCamera(PApplet p5) {
        spaceNavigator = new SpaceNavigator(p5);
        momentum = new MomentumVector(p5, .003f);
        rotMomentum = new MomentumYawPitchRoll(p5, .003f);
    }

    public void update() {
        spaceNavigator.poll();

        float mult = inverseControls ? -.045f: .045f;

        PVector t = PVector.mult(spaceNavigator.translation, mult / 100f);

        momentum.add(t);

        rotMomentum.add(PVector.mult(spaceNavigator.rotation, mult));

        momentum.friction();
        rotMomentum.friction();

        translateWRTObjectCoords(momentum.getValue());
        rotate(rotMomentum.getValue());
    }
}
