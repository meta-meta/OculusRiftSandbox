package com.generalprocessingunit.processing;

import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class ProcessingDelegateComponent {
    public PApplet p5;

    public ProcessingDelegateComponent(PApplet p5){
        this.p5 = p5;
    }

    public abstract void draw(PGraphics pG);
}
