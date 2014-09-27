package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.audio.SineBalls;
import com.generalprocessingunit.hid.SpaceNavigator;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SpatialAudio extends PApplet {

    SineBalls sineBalls;
    SpaceNavigator spaceNav;
    PVector momentum = new PVector();

    static final float drag = 0.0125f;
    static final float speedCoef = 0.0004f;

    static boolean invertedControl = false;

    PGraphics pG;

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen", "--display=3", */SpatialAudio.class.getCanonicalName()});
    }

    @Override
    public void setup() {
        size(1200, 800, OPENGL);

        sineBalls = new SineBalls(this);
        pG = createGraphics(width, height, OPENGL);

        spaceNav = new SpaceNavigator(this);
    }

    @Override
    public void draw() {

        SineBalls.SineBall ball = sineBalls.balls.get(0);

        spaceNav.poll();
        int i = invertedControl ? -1 : 1;
        momentum.add(PVector.mult(spaceNav.translation, i * speedCoef));

        println(spaceNav.translation);

        momentum.x = momentum.x - momentum.x * drag;
        momentum.y = momentum.y - momentum.y * drag;
        momentum.z = momentum.z - momentum.z * drag;

        ball.translate(momentum);

        // fake doppler
        PVector nextPos = PVector.add(ball.getLocation(), momentum);
        float v = nextPos.mag() - ball.getLocation().mag();
        ball.setTune(-v * 1000);


        pG.beginDraw();
        pG.background(100);
        pG.perspective(PI / 5f, width / height, 0.1f, 10000f);
        pG.camera(
                0, .5f, -3,
                0, .3f, 1,
                0, -1, 0
        );


        pG.directionalLight(255, 255, 255, .3f, -1, .4f);

        pG.fill(200, 190, 100, 150);
        pG.sphere(.1f);

        sineBalls.update();
        sineBalls.draw(pG);
        pG.endDraw();

        image(pG, 0, 0);

        sineBalls.updateMaxPatch();
    }
}
