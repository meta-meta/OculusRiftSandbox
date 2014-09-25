package com.generalprocessingunit.processing;

import processing.core.PApplet;

public abstract class Momentum<T> extends MathsHelpers{
    protected PApplet p5;

    private float friction;

    T m;

    protected Momentum() { /*don't want to call this*/ }


    /**
     * @param friction portion of momentum to decrement by every millisecond
     * */
    public Momentum(PApplet p5, float friction) {
        this.p5 = p5;
        this.friction = friction;
    }


    public void add(T amount){
        _add(amount);

        friction();
    }

    public T getValue(){
        return m;
    }

    protected float friction(float f, int ellapsedMillis) {
        float d = min(1f, friction * ellapsedMillis);
        return f - f * d;
    }

    protected abstract void _add(T amount);

    protected abstract void friction();


}
