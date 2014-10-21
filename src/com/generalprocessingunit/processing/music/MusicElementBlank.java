package com.generalprocessingunit.processing.music;

public class MusicElementBlank extends MusicElement {
    @Override
    public String getGlyph() {
        return null;
    }

    public MusicElementBlank(RhythmType rhythm) {
        this.rhythm = rhythm;
    }
}
