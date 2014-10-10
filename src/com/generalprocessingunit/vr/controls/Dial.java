package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.hid.Hand;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.vr.entities.Primitives;
import processing.core.*;

public class Dial extends AbstractControl{
    PShape knob;
    float radius;
    float depth;

    PShape meter;
    public static final int NUM_TICKS = 20;

    public Dial(PApplet p5, int initialValue, float radius, float depth){
        super(initialValue);

        this.radius = radius;
        this.depth = depth;

        knob = Primitives.cylinder(p5, radius, depth, 50);
        knob.rotateX(PApplet.HALF_PI);

        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        p5.colorMode(PConstants.HSB);
        top.setStroke(false);
        setDefaultKnobColor(p5);

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

    public void update(PApplet p5, Hand hand) {
        setState(p5, hand);
    }

    private void setState(PApplet p5, Hand hand) {
        if (!engaged) {
            // TODO this grabbing logic should get abstracted away
            // if we are already grabbing when we touch, we can't actually grab the knob
            if (!hand.isGrabbing() && isTouching(hand.getLocation())) {
                if(!touched) {
                    indicateTouch(hand);
                }
                touched = true;
            }

            if (touched) {
                if (isTouching(hand.getLocation())) {
                    if (hand.isGrabbing()) { // just started grabbing
                        engaged = true;
                        prevRotation = hand.getRoll()  + PConstants.PI;
                        totalRotationWhileGrabbing = 0;
                        valAtEngage = val;
                        setEngagedKnobColor(p5);
                    } else {
                        setTouchedKnobColor(p5);
                    }
                } else {
                    touched = false;
                    setDefaultKnobColor(p5);
                }
            }
        } else {
            if (hand.isGrabbing()) {

                float nextVal = valAtEngage + getChangeInRotation(hand.getRoll());
                if (!setValue(nextVal)) {
                    indicateMinOrMax(nextVal, hand);
                } else {
                    prevWarningLimit = 0;
                }

                // this divides the dial into NUM_TICKS 'ticks' where switching to the next tick triggers a vibrate
                int prevTick = tick;
                tick = (int) (val / (range / NUM_TICKS));
                if (prevTick != tick) {

                    // TODO drawing logic should be separate
                    if (tick > 0) {
                        for (int i = 0; i < 4; i++) {
                            int vertex = (tick - 1) * 2 + i;
                            meter.setEmissive(vertex, 127);
                        }
                    }

                    if (prevTick > tick) {
                        for (int i = (prevTick == 1 ? 0 : 2); i < 4; i++) {
                            int vertex = (prevTick - 1) * 2 + i;
                            meter.setEmissive(vertex, 0);
                        }
                    }

                    indicateTick(hand);
                }
            } else {
                engaged = false;
            }
        }
    }

    private void setDefaultKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        p5.colorMode(PConstants.HSB);
        top.setEmissive(20);
        top.setFill(p5.color(200, 255, 255, 127));
    }

    private void setTouchedKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        p5.colorMode(PConstants.HSB);
        top.setEmissive(64);
    }

    private void setEngagedKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        p5.colorMode(PConstants.HSB);
        top.setEmissive(127);
    }

    private void indicateTick(Hand hand ) {
        hand.fingertips.vibrate(1);
    }

    float prevWarningLimit = 0;
    void indicateMinOrMax(float attemptedVal, Hand hand) {
        if(attemptedVal > maxVal && attemptedVal > prevWarningLimit) {
            hand.index.vibrate(1);
            prevWarningLimit = attemptedVal;
        }

        if(attemptedVal < minVal && attemptedVal < prevWarningLimit) {
            hand.pinky.vibrate(1);
            prevWarningLimit = attemptedVal;
        }
    }

    private void indicateTouch(Hand hand) {
        hand.palm.vibrate(1);
    }

    // TODO this total rotation logic should get abstracted away
    private float prevRotation;
    private float totalRotationWhileGrabbing;
    private float getChangeInRotation(float r) {

        // cursorRotation.z ranges from -PI to +PI
        float currRotation = r + PConstants.PI;

        float d = currRotation - prevRotation;

        // check if we've turned over past 2PI back to zero or the other way
        if (PApplet.abs(d) > PConstants.QUARTER_PI) {   // PConstants.QUARTER_PI is arbitrary
            if (d < 0) {
                // crossed 12 o'clock going clockwise
                totalRotationWhileGrabbing -= (currRotation + (PConstants.TWO_PI - prevRotation));
            } else {
                // crossed 12 o'clock going counter-clockwise
                totalRotationWhileGrabbing += (prevRotation + (PConstants.TWO_PI - currRotation));
            }
        } else {
            totalRotationWhileGrabbing -= d;
        }

        prevRotation = currRotation;
        return (totalRotationWhileGrabbing / PConstants.TWO_PI) * range;
    }

    public void draw(PGraphics pG) {
        pG.pushMatrix();
        {
            pG.translate(this.x(), this.y(), this.z());
            AxisAngle aa = getAxisAngle();
            pG.rotate(aa.w, aa.x, aa.y, aa.z);
            pG.rotateX(PConstants.PI);
            pG.shape(meter);

            pG.rotateZ(PConstants.TWO_PI * (val / range));
            pG.shape(knob);
        }
        pG.popMatrix();
    }

    private boolean isTouching(PVector cursorPosition) {
        return getDistFrom(cursorPosition) < radius * 1.4f;
    }

}
