package com.generalprocessingunit.processing;

import com.generalprocessingunit.processing.space.YawPitchRoll;
import processing.core.PApplet;
import processing.core.PVector;

public class MomentumYawPitchRoll extends Momentum<YawPitchRoll> {

    private MomentumYawPitchRoll() { /*don't want to call this*/ }

    public MomentumYawPitchRoll(PApplet p5, float friction) {
        super(p5, friction);
        m = new YawPitchRoll();
    }

    @Override
    protected void _add(YawPitchRoll amount) {
        m.add(amount);
    }

    public void add(float yaw, float pitch, float roll) {
        m.add(pitch, yaw, roll);
    }

    public void add(PVector v) {
        m.add(v);
    }

    public void set(float yaw, float pitch, float roll) {
        m.set(pitch, yaw, roll);
    }

    public void set(YawPitchRoll v) {
        m.set(v);
    }

    public void reversePitch() {
        m.x = -m.x;
    }

    public void reverseYaw() {
        m.y = -m.y;
    }

    public void reverseRoll() {
        m.z = -m.z;
    }

    private int prevMillis = 0;
    @Override
    public void friction() {
        int dMillis = p5.millis() - prevMillis;
        prevMillis = p5.millis();

        m.x = friction(m.x, dMillis);
        m.y = friction(m.y, dMillis);
        m.z = friction(m.z, dMillis);
    }
}
