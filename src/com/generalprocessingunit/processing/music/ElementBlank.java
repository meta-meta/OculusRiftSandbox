package com.generalprocessingunit.processing.music;

public class ElementBlank extends Element {
    @Override
    public String getGlyph() {
        return null;
    }

    public ElementBlank(Duration rhythm) {
        this.rhythm = rhythm;
    }
}
