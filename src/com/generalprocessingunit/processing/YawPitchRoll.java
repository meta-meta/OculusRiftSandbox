package com.generalprocessingunit.processing;

import processing.core.PVector;

public class YawPitchRoll extends PVector {
    public YawPitchRoll() {
        super();
    }

    public YawPitchRoll(float yaw, float pitch, float roll) {
        super(pitch, yaw, roll);
    }

    public float yaw() {
        return y;
    }

    public float pitch() {
        return x;
    }

    public float roll() {
        return z;
    }

    @Override
    public void set(float yaw, float pitch, float roll) {
        super.set(pitch, yaw, roll);
    }
}
