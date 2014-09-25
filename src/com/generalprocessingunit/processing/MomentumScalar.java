package com.generalprocessingunit.processing;

import processing.core.PApplet;

public class MomentumScalar extends Momentum<Float> {

    private MomentumScalar() { /*don't want to call this*/ }

    public MomentumScalar(PApplet p5, float drag) {
        super(p5, drag);
        m = 0f;
    }

    @Override
    protected void _add(Float amount) {
        m += amount;
    }

    private int prevMillis = 0;
    @Override
    protected void friction() {
        m = friction(m, p5.millis() - prevMillis);
        prevMillis = p5.millis();
    }
}
