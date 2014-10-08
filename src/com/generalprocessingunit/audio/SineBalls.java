package com.generalprocessingunit.audio;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.MomentumVector;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class SineBalls extends AudibleEntityVR {
    private PApplet p5;
    public List<SineBall> balls = new ArrayList<>();

    private static String BASE_MSG = "/sineballs/%s";
    private static String FREQUENCY = BASE_MSG + "/freq";
    private static String AMPLITUDE = BASE_MSG + "/amp";

    public SineBalls(PApplet p5, int numBalls) {
        this.p5 = p5;

        numBalls = min(16, numBalls);

        for(int i = 1; i <= 16; i++) {
            OSC.sendMsg(String.format(AMPLITUDE, i), 0);
        }

        for(int i = 1; i <= numBalls; i++) {
            float freq = 60 + (int)p5.random(50) * 35;
            System.out.println(i + ": " + freq);
            balls.add(new SineBall(i, new PVector(0, 0, 1), freq, .1f));
        }
    }

    public void updateMaxPatch() {
        for(SineBall ball: balls) {
            ball.updateMaxPatch();
        }
    }

    public void update() {
        for(SineBall ball: balls) {
            if(ball.id == 1) {
                continue;
            }

            ball.setLocation(
                    (2 * sin(p5.millis() / 5000f) + ball.id / 10f) * cos(p5.millis() / (11 - ball.id * 200f)),
                    0,
                    (2 * sin(p5.millis() / 5000f) + ball.id / 10f) * sin(p5.millis() / (11 - ball.id * 200f))
            );
        }
    }

    public void draw(PGraphics pG){
        for(SineBall ball: balls) {
            ball.draw(pG);
        }
    }

    public SineBall get(int i) {
        return balls.get(i);
    }

    public List<SineBall> get() {
        return balls;
    }

    public class SineBall extends EuclideanSpaceObject {
        public int id;
        float tune = 0;
        public float freq, amp;
        float azimuth, elevation, radius;

        public float size;

        // TODO: move to EuclideanSpaceObject
        public PVector prevLocation = new PVector();

        public MomentumVector momentum = new MomentumVector(p5, .0001f);

        SineBall(int id, PVector location, float freq, float amp) {
            this.id = id;
            this.freq = freq;
            this.amp = amp;
            this.setLocation(location);
        }

        void updateMaxPatch() {
            OSC.sendMsg(String.format(FREQUENCY, id), freq + tune);

            OSC.sendMsg(String.format(AMPLITUDE, id), radius <= 1 ? amp : amp / radius);

            setVals(
                    id,
                    azimuth,
                    elevation,
                    radius
            );
        }

        public void draw(PGraphics pG) {
            size = 0.05f + 0.01f * sin((freq / 2f) * p5.millis() / 1000f);

            PVector loc = getLocation();
            pG.pushMatrix();
            pG.translate(loc.x, loc.y, loc.z);
            pG.fill(200, 0, 255);
            pG.noStroke();
            pG.sphereDetail(10);
            pG.sphere(size);
            pG.popMatrix();
        }

        public void setRadialCoords(float azimuth, float elevation, float radius) {
            this.azimuth = azimuth;
            this.elevation = elevation;
            this.radius = radius;
        }

        public void setTune(float tune) {
            this.tune = tune;
        }
    }
}
