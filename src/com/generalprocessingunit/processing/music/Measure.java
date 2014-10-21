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

    static void drawStaves(PGraphics pG, int numGlyphs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numGlyphs + 2; i++) { // 2 for padding
            sb.append(STAFF_5 + STAFF_5);
        }
        pG.text(sb.toString() + BARLINE_SINGLE, 0, 0);
    }

    public int numGlyphs() {
        return elementSeq.size();
    }
}
