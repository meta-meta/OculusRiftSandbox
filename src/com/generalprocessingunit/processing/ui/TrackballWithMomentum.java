package com.generalprocessingunit.processing.ui;

import com.generalprocessingunit.processing.MomentumVector;
import com.google.common.collect.EvictingQueue;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Queue;

public class TrackballWithMomentum extends Touchable {
    public MomentumVector mv;
    public PVector position = new PVector();

    private int prevX, prevY;
    private int prevMillis = 0;

    private boolean mouseDown = false;

    public TrackballWithMomentum(PApplet p5, int x, int y, int w, int h, float friction) {
        super(p5, x, y, w, h);
        mv = new MomentumVector(p5, friction);
    }

    private Queue<PVector> velocities = EvictingQueue.create(10);

    @Override
    public void update() {

        if (area.contains(p5.mouseX, p5.mouseY) && p5.mousePressed) {
            if (!mouseDown) {
                mouseDown = true;
            } else {
                int dMillis = p5.millis() - prevMillis;

                float dX = p5.mouseX - prevX;
                float dY = p5.mouseY - prevY;

                position.add(dX, dY, 0);

                PVector v = new PVector(
                        dX / dMillis,
                        dY / dMillis,
                        0f
                );

                velocities.add(PVector.mult(v, dMillis));
            }

            prevMillis = p5.millis();
            prevX = p5.mouseX;
            prevY = p5.mouseY;
        } else {
            if (mouseDown) { // mouse was just released
                PVector averagey = velocities.poll();
                PVector v;
                while (null != (v = velocities.poll())) {
                    // this is like an average but weighted heavier for the most recent val
                    averagey.add(v);
                    averagey.div(2);
                }

                mv.set(averagey);
            }

            mouseDown = false;
            position.add(mv.getValue());
            mv.friction();
        }

    }
}
