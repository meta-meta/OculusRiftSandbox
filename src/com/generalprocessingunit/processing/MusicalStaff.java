package com.generalprocessingunit.processing;

import com.generalprocessingunit.processing.demos.MusicalFontContstants;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

public class MusicalStaff extends ProcessingDelegateComponent implements MusicalFontContstants, PConstants {
    private static PFont bravura;

    public MusicalStaff(PApplet p5) {
        super(p5);
        bravura = p5.createFont("Bravura.otf", 100, true, charset);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(PGraphics pG) {
        pG.textFont(bravura);
        pG.textSize(50);

        pG.fill(0);
        pG.directionalLight(255, 240, 200, -1, -.3f, .4f);

        pG.pushMatrix();
        {
            pG.rotateX(PI);

            pG.text(STAFF_5 + STAFF_5 + STAFF_5 + STAFF_5, 0, 0);
            pG.text(NOTE_32ND_UP + NOTE_WHOLE, 0, 0);
        }
        pG.popMatrix();
    }
}
