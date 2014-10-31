package com.generalprocessingunit.processing.music;

import com.google.common.base.Objects;
import processing.core.PGraphics;

// TODO: maybe associate key signature and time signature with measure
public class Measure implements MusicalFontConstants {
    public ElementSequence seq;
    private final int hash;

    public Measure(ElementSequence seq) {
        this.seq = seq;
        hash = Objects.hashCode(System.nanoTime());
    }

    void draw(PGraphics pG) {

    }

    void drawStaves(PGraphics pG) {
        drawStaves(getNumGridPositions(), pG);
    }

    static void drawStaves(int numGridPositions, PGraphics pG) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numGridPositions; i++) {
            sb.append(STAFF_5);
        }
        pG.text(sb.toString() + BARLINE_SINGLE, 0, 0);
    }

    int getNumGridPositions() {
        return (seq.size() + 1) * 2 + 1;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Measure)) {
            return false;
        }

        Measure that = (Measure)obj;
        return that.hash == hash;
    }
}
