package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.vr.entities.Primitives;
import processing.core.*;

public class Dial extends AbstractControl{
    PShape knob;
    float radius = 50;
    float depth = 30;

    PShape meter;
    public static final int NUM_TICKS = 20;

    public Dial(PApplet p5, PVector position, int initialValue){
        super(position, initialValue);

        knob = Primitives.cylinder(p5, radius, depth, 50);
        knob.rotateX(PApplet.HALF_PI);

        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        p5.colorMode(PConstants.HSB);
        top.setFill(p5.color(0, 255, 255, 30));
        top.setStroke(false);

        PShape mid = knob.getChild(Primitives.CYLINDER_MID);
        p5.colorMode(PConstants.HSB);
        mid.setStroke(p5.color(0, 255, 255, 255));
        mid.setFill(false);

        knob.removeChild(knob.getChildIndex(knob.getChild(Primitives.CYLINDER_BOT)));

        meter = Primitives.arc(p5, radius * 1.05f, radius * 1.2f, PConstants.PI, NUM_TICKS);
        meter.rotateX(PApplet.HALF_PI);
        meter.setFill(p5.color(0));
        meter.setStroke(p5.color(150, 200, 200));
    }

    boolean touched = false;
    boolean engaged = false;
    float valAtEngage;

    int tick = 0;

    public void update(PApplet p5, PGraphics pG, PVector cursorPosition, float grip, PVector cursorRotation) {
        setState(p5, cursorPosition, grip, cursorRotation);
        draw(pG);
    }

    private void setState(PApplet p5, PVector cursorPosition, float grip, PVector cursorRotation) {
        if (!engaged) {
            // TODO this grabbing logic should get abstracted away
            // if we are already grabbing when we touch, we can't actually grab the knob
            if (!isGrabbing(grip) && isTouching(cursorPosition)) {
                indicateTouch();
                touched = true;
            }

            if (touched) {
                if (isTouching(cursorPosition)) {
                    if (isGrabbing(grip)) {
                        engaged = true;
                        prevRotation = cursorRotation.z + PConstants.PI;  // cursorRotation.z ranges from -PI to +PI
                        totalRotation = 0;
                        valAtEngage = val;
                    }
                } else {
                    touched = false;
                }
            }
        } else {
            if (isGrabbing(grip)) {

                if (!setValue(valAtEngage + getChangeInRotation(cursorRotation.z))) {
                    indicateMinOrMax();
                }

                // this divides the dial into 20 'ticks' where switching to the next tick triggers a vibrate
                int prevTick = tick;
                tick = (int) (val / (range / NUM_TICKS));
                if (prevTick != tick) {

                    // TODO drawing logic should be separate
                    if (tick > 0) {
                        for (int i = 0; i < 4; i++) {
                            meter.setFill((tick - 1) * 2 + i, p5.color(160, 200, 200));
                        }
                    }

                    if (prevTick > tick) {
                        for (int i = (prevTick == 1 ? 0 : 2); i < 4; i++) {
                            meter.setFill((prevTick - 1) * 2 + i, p5.color(0));
                        }
                    }

                    indicateTick();
                }
            } else {
                engaged = false;
            }
        }
    }

    private void indicateTick() {
        // TODO vibrate
    }

    private void indicateTouch() {
        // TODO vibrate lightly to indicate that we've brushed up against the knob
    }

    // TODO this total rotation logic should get abstracted away
    float prevRotation;
    float totalRotation;
    private float getChangeInRotation(float r) {

        // cursorRotation.z ranges from -PI to +PI
        float currRotation = r + PConstants.PI;

        float d = currRotation - prevRotation;

        // check if we've turned over past 2PI back to zero or the other way
        if (PApplet.abs(d) > PConstants.QUARTER_PI) {   // PConstants.QUARTER_PI is arbitrary
            if (d < 0) {
                // crossed 12 o'clock going clockwise
                totalRotation += (currRotation + (PConstants.TWO_PI - prevRotation));
            } else {
                // crossed 12 o'clock going counter-clockwise
                totalRotation -= (prevRotation + (PConstants.TWO_PI - currRotation));
            }
        } else {
            totalRotation += d;
        }

        prevRotation = currRotation;
        return (totalRotation / PConstants.TWO_PI) * range;
    }

    void draw(PGraphics pG) {
        pG.pushMatrix();
        pG.rotateX(-0.4f);
        pG.shape(meter);

        pG.rotateZ(PConstants.TWO_PI * (val / range));
        pG.shape(knob);
        pG.popMatrix();
    }

    void indicateMinOrMax() {
        // TODO vibrate hard to indicate that you've turned as far as you can
    }

    private boolean isTouching(PVector cursorPosition) {
        return PVector.dist(cursorPosition, position) < radius;
    }

    private boolean isGrabbing(float grip){
        return grip > 0.7;
    }
}
