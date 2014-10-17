package com.generalprocessingunit.processing.space;

import com.generalprocessingunit.processing.MathsHelpers;

public class AxisAngle extends MathsHelpers {
    public float w, x, y, z;

    public AxisAngle(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisAngle(){}
}
