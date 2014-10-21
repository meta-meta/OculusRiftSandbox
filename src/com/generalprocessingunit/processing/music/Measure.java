package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.processing.demos.MusicalFontConstants;
import processing.core.PGraphics;

public class Measure implements MusicalFontConstants {
    public MusicElementSeq elementSeq;

    public Measure(MusicElementSeq elementSeq) {
        this.elementSeq = elementSeq;
    }

    void draw(PGraphics pG) {

    }

    static void drawStaves(PGraphics pG, int numElements) {
        drawStaves((numElements + 1) * 2 + 1, pG);
    }

    static void drawStaves(int numGridPositions, PGraphics pG) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numGridPositions; i++) {
            sb.append(STAFF_5);
        }
        pG.text(sb.toString() + BARLINE_SINGLE, 0, 0);
    }

    public int numElements() {
        return elementSeq.size();
    }
}
