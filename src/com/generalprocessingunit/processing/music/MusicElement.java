package com.generalprocessingunit.processing.music;

import processing.core.PApplet;

public abstract class MusicElement {
    RhythmType rhythm;

    public float percentagePlayed = 0;
    public boolean wasPassed = false;

    public void reset() {
        wasPassed = false;
        percentagePlayed = 0;
    }

    public void incrementPercentagePlayed(float amount) {
        percentagePlayed = PApplet.min(amount + percentagePlayed, 1);
    }

    public abstract String getGlyph();
}
