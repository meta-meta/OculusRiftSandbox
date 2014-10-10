package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.processing.EuclideanSpaceObject;

public abstract class AbstractControl extends EuclideanSpaceObject {
    public float val;
    float minVal;
    float maxVal;
    float range;

    AbstractControl(int initalVal){
        this(initalVal, 0, 100);
    }

    AbstractControl(int initalVal, float minVal, float maxVal){
        super();
        val = initalVal;
        this.minVal = minVal;
        this.maxVal = maxVal;
        range = maxVal - minVal;
    }

//    public abstract void draw(PApplet p5, PGraphics pG, PVector cursorPosition);

    boolean increment(){
        return increment(1);
    }

    boolean increment(float amount){
        return setValue(val + amount);
    }

    boolean decrement(){
        return decrement(1);
    }

    boolean decrement(float amount){
        return setValue(val - amount);
    }

    boolean setValue(float val){
        this.val = Math.min(maxVal, Math.max(minVal, val));
        return (val < maxVal && val > minVal);
    }
}
