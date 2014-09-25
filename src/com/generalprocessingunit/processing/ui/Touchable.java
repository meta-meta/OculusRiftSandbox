package com.generalprocessingunit.processing.ui;

import processing.core.PApplet;

import java.awt.*;

public abstract class Touchable {
    protected Rectangle area;
    protected PApplet p5;

    public Touchable(PApplet p5, int x, int y, int w, int h) {
        this.p5 = p5;
        area = new Rectangle(x, y, w, h);
    }

    public abstract void update();
}
