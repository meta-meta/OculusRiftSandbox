package com.generalprocessingunit.processing.music;

public class MusicRest extends MusicElement {
    public MusicRest(RhythmType rhythm) {
        this.rhythm = rhythm;
    }

    @Override
    public String getGlyph() {
        return rhythm.restGlyph;
    }
}
