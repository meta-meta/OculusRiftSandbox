package com.generalprocessingunit.processing;

import processing.core.PApplet;
import processing.core.PVector;

public class MomentumVector extends Momentum<PVector> {

    private MomentumVector() { /*don't want to call this*/ }

    public MomentumVector(PApplet p5, float friction) {
        super(p5, friction);
        m = new PVector();
    }

    @Override
    protected void _add(PVector amount) {
        m.add(amount);
    }

    public void add(float x, float y, float z) {
        m.add(x, y, z);
    }

    public void set(float x, float y, float z) {
        m.set(x, y, z);
    }

    public void set(PVector v) {
        m.set(v);
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
