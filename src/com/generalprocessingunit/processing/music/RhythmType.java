package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.processing.demos.MusicalFontConstants;

public enum RhythmType implements MusicalFontConstants {
    Whole          (1, NOTE_WHOLE, NOTE_WHOLE, REST_WHOLE),
    Half           (.5f, NOTE_HALF_UP, NOTE_HALF_UP, REST_HALF),
    Quarter        (.25f, NOTE_QUARTER_UP, NOTE_QUARTER_DN, REST_QUARTER),
    Eighth         (.125f, NOTE_EIGHTH_UP, NOTE_EIGHTH_DN, REST_EIGTH),
    Sixteenth      (.0625f, NOTE_16TH_UP, NOTE_16TH_DN, REST_16TH),
    ThirtySecond   (.03125f, NOTE_32ND_UP, NOTE_32ND_DN, REST_32ND);

    String upGlyph;
    String dnGlyph;
    String restGlyph;
    float val;

    RhythmType(float val, String upGlyph, String dnGlyph, String restGlyph) {
        this.upGlyph = upGlyph;
        this.dnGlyph = dnGlyph;
        this.restGlyph = restGlyph;
        this.val = val;
    }
}
