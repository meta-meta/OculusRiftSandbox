package com.generalprocessingunit.processing.space;

import com.generalprocessingunit.processing.MomentumVector;
import com.generalprocessingunit.processing.MomentumYawPitchRoll;
import processing.core.PGraphics;
import processing.core.PVector;

public class Camera extends EuclideanSpaceObject{
    MomentumVector momentum;
    MomentumYawPitchRoll rotMomentum;

    public void update() {
        translateWRTObjectCoords(momentum.getValue());
        rotate(rotMomentum.getValue());

        momentum.friction();
        rotMomentum.friction();
    }

    public void camera(PGraphics pG) {
        PVector cam = getLocation();

        PVector lookat = getOrientation().zAxis();
        lookat.add(cam);
        PVector up = getOrientation().yAxis();
        up.mult(-1);

        pG.camera(
                cam.x, cam.y, cam.z,
                lookat.x, lookat.y, lookat.z,
                up.x, up.y, up.z
        );

//        pG.scale(-1, 1);
    }

}
