package com.generalprocessingunit.processing.music;

public class Note extends Element {
    public Integer noteNumber;

    public Note(int noteNumber, Duration rhythm) {
        this.noteNumber = noteNumber;
        this.rhythm = rhythm;
    }

    @Override
    public String getGlyph() {
        return rhythm.upGlyph;
    }
}
