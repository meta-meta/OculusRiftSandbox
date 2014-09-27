package com.generalprocessingunit.audio;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class SineBalls extends VRSoundEntities{
    private PApplet p5;
    public List<SineBall> balls = new ArrayList<>();

    private static String BASE_MSG = "/sineballs/%s";
    private static String FREQUENCY = BASE_MSG + "/freq";
    private static String AMPLITUDE = BASE_MSG + "/amp";

    public SineBalls(PApplet p5) {
        this.p5 = p5;
        for(int i = 1; i <= 16; i++) {
            OSC.sendMsg(String.format(AMPLITUDE, i), 0);
        }

        for(int i = 1; i <= 1; i++) {
            float freq = 60 + (int)p5.random(50) * 35;
            System.out.println(i + ": " + freq);
            balls.add(new SineBall(i, new PVector(0, 0, 1), freq, i == 1 ? .1f : .03f));
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

    public class SineBall extends EuclideanSpaceObject {
        int id;
        float freq;
        float amp;

        float tune = 0;

        SineBall(int id, PVector location, float freq, float amp) {
            this.id = id;
            this.freq = freq;
            this.amp = amp;
            this.setLocation(location);
        }

        void updateMaxPatch() {
            PVector loc = getLocation();
            float dist = loc.mag();

            OSC.sendMsg(String.format(FREQUENCY, id), freq + tune);

            OSC.sendMsg(String.format(AMPLITUDE, id), dist <= 1 ? amp : amp / dist);


            setVals(
                    id,
                    atan2(loc.z, loc.x),
                    acos(loc.y / loc.mag()),
                    dist
            );
        }

        public void updateMaxPatch(float az, float el, float r) {
            PVector loc = getLocation();
            float dist = loc.mag();

            OSC.sendMsg(String.format(FREQUENCY, id), freq + tune);

            OSC.sendMsg(String.format(AMPLITUDE, id), dist <= 1 ? amp : amp / dist);


            setVals(
                    id,
                    az,
                    el,
                    r
            );
        }

        public void draw(PGraphics pG) {
            PVector loc = getLocation();
            pG.pushMatrix();
            pG.translate(loc.x, loc.y, loc.z);
            pG.fill(200, 0, 255);
            pG.noStroke();
            pG.sphereDetail(10);
            pG.sphere(0.05f + 0.01f * sin((freq / 2f) * p5.millis() / 1000f));
            pG.popMatrix();
        }

        public void setTune(float tune) {
            this.tune = tune;
        }
    }
}
