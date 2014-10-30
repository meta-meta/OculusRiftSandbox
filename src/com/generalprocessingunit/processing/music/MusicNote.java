package com.generalprocessingunit.processing.music;

public class MusicNote extends MusicElement {
    public Integer noteNumber;
    public boolean wasPlayed = false;

    public MusicNote(int noteNumber, RhythmType rhythm) {
        this.noteNumber = noteNumber;
        this.rhythm = rhythm;
    }

    @Override
    public String getGlyph() {
        return rhythm.upGlyph;
    }
}
