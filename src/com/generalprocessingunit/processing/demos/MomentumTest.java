package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.ui.PAppletTouch;
import com.generalprocessingunit.processing.ui.TrackballWithMomentum;
import processing.core.PApplet;
import processing.core.PGraphics;

public class MomentumTest extends PAppletTouch {

    public static void main(String[] args) {
        PApplet.main(new String[]{/*"--full-screen", "--display=3",*/ MomentumTest.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

    TrackballWithMomentum trackball;

    @Override
    public void setup() {
        size(1200, 800, OPENGL);
        super.setup();

        trackball = new TrackballWithMomentum(this, 0, 0, width, height, 0.0008f);
        addTouchable(trackball);
    }

    @Override
    public final void draw(PGraphics pG) {
        pG.background(127);

        for (int x = -20; x < 10; x++) {
            for (int y = -20; y < 10; y++) {
                pG.fill((x % 2 == 0 ^ y % 2 == 0) ? 255 : 0);
                pG.rect(
                        (width + x * width / 10 + trackball.position.x % width),
                        (height + y * height / 10 + trackball.position.y % height),
                        width / 10,
                        height / 10);
            }
        }
    }
}
