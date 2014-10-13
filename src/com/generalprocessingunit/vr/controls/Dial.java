package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.hid.Hand;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.Color;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.YawPitchRoll;
import com.generalprocessingunit.vr.entities.Primitives;
import processing.core.*;

public class Dial extends AbstractControl{
    PShape knob;
    Color knobColor;
    float radius;
    float depth;

    public static final int NUM_TICKS = 64;
    PShape meter;

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, float radius, float depth, int initialValue, float minVal, float maxVal, Color knobColor){
        super(initialValue, minVal, maxVal);

        locationWRTParent = location;
        rotationWRTParent = rotation;

        this.radius = radius;
        this.depth = depth;

        this.knobColor = knobColor;

        knob = Primitives.cylinder(p5, radius, depth, 50);
        knob.rotateX(PApplet.HALF_PI);

        p5.colorMode(PConstants.RGB);

        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        top.setStroke(false);
        top.setFill(p5.color(knobColor.R, knobColor.G, knobColor.B, knobColor.A));
        setDefaultKnobColor(p5);

        PShape mid = knob.getChild(Primitives.CYLINDER_MID);
        mid.setStroke(p5.color(127, 127, 127, 255));
        mid.setFill(p5.color(knobColor.R / 2, knobColor.G / 2, knobColor.B / 2, knobColor.A));

        knob.removeChild(knob.getChildIndex(knob.getChild(Primitives.CYLINDER_BOT)));

        meter = Primitives.arc(p5, radius * 1.05f, radius * 1.2f, PConstants.PI, NUM_TICKS);
        meter.rotateX(PApplet.HALF_PI);
        meter.setFill(p5.color(255, 127));
        meter.setStroke(p5.color(127, 127, 127));
    }

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, float radius, float depth, Color knobColor){
        this(p5, location, rotation, radius, depth, 0, 0, 1, knobColor);
    }

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, float radius, float depth, float maxVal, Color knobColor){
        this(p5, location, rotation, radius, depth, 0, 0, maxVal, knobColor);
    }

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, int initialValue, float minVal, float maxVal, Color knobColor){
        this(p5, location, rotation, .03f, .02f, initialValue, minVal, maxVal, knobColor);
    }

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, float maxVal, Color knobColor){
        this(p5, location, rotation, 0, 0, maxVal, knobColor);
    }

    public Dial(PApplet p5, PVector location, YawPitchRoll rotation, Color knobColor){
        this(p5, location, rotation, 0, 0, 1, knobColor);
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
                    for (int i = 1; i <= NUM_TICKS; i++) {
                        for (int v = 0; v < 4; v++) {
                            meter.setEmissive((i - 1) * 2 + v, i <= tick ? p5.color(255) : p5.color(0));
                        }
                    }

                    if(tick % 4 == 0){
                        indicateTick(hand);
                    }
                }
            } else {
                engaged = false;
            }
        }
    }

    private void setDefaultKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        top.setEmissive(p5.color(knobColor.R / 10, knobColor.G / 10, knobColor.B / 10));
    }

    private void setTouchedKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        top.setEmissive(p5.color(knobColor.R / 4, knobColor.G / 4, knobColor.B / 4));
    }

    private void setEngagedKnobColor(PApplet p5) {
        PShape top = knob.getChild(Primitives.CYLINDER_TOP);
        top.setEmissive(p5.color(knobColor.R, knobColor.G, knobColor.B));
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
        hand.fingers.vibrate(1);
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
        return (totalRotationWhileGrabbing / PConstants.HALF_PI) * range;
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
        return getDistFrom(PVector.sub(cursorPosition, new PVector(0, radius / 2f, 0))) < radius * 1.5f;
    }

    public Dial addAsChildTo(EuclideanSpaceObject parent) {
        parent.addChild(this, locationWRTParent, rotationWRTParent);
        return this;
    }
}
