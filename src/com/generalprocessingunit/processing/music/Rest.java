package com.generalprocessingunit.processing.music;

public class Rest extends Element {
    public Rest(Duration rhythm) {
        this.rhythm = rhythm;
    }

    @Override
    public String getGlyph() {
        return rhythm.restGlyph;
    }
}
