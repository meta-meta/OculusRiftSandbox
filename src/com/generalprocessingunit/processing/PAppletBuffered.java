package com.generalprocessingunit.processing;

import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class PAppletBuffered extends PApplet {
    public PGraphics pG;

    @Override
    public void setup() {
        pG = createGraphics(width, height);
    }

    @Override
    public final void draw() {
        pG.beginDraw();
        draw(pG);
        pG.endDraw();
        image(pG, 0, 0);
    }

    public abstract void draw(PGraphics pG);
}