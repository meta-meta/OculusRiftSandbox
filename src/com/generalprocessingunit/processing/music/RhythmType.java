package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.processing.demos.MusicalFontContstants;

public enum RhythmType {
    Whole          (1, MusicalFontContstants.NOTE_WHOLE, MusicalFontContstants.NOTE_WHOLE),
    Half           (.5f, MusicalFontContstants.NOTE_HALF_UP, MusicalFontContstants.NOTE_HALF_UP),
    Quarter        (.25f, MusicalFontContstants.NOTE_QUARTER_UP, MusicalFontContstants.NOTE_QUARTER_DN),
    Eighth         (.125f, MusicalFontContstants.NOTE_EIGHTH_UP, MusicalFontContstants.NOTE_EIGHTH_DN),
    Sixteenth      (.0625f, MusicalFontContstants.NOTE_16TH_UP, MusicalFontContstants.NOTE_16TH_DN),
    ThirtySecond   (.03125f, MusicalFontContstants.NOTE_32ND_UP, MusicalFontContstants.NOTE_32ND_DN);

    String upGlyph;
    String dnGlyph;
    float val;

    RhythmType(float val, String upGlyph, String dnGlyph) {
        this.upGlyph = upGlyph;
        this.dnGlyph = dnGlyph;
        this.val = val;
    }
}
