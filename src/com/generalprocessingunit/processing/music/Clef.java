package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.music.Accidental;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.NoteLetter;
import com.generalprocessingunit.music.NoteName;
import com.generalprocessingunit.processing.demos.MusicalFontContstants;
import com.google.common.base.Objects;
import processing.core.PGraphics;
import scala.Enumeration;

import java.util.HashMap;
import java.util.Map;

public enum Clef implements MusicalFontContstants {
    Treble(G_CLEF, .72f, new NoteName(NoteLetter.E(), 5, Accidental.Natural())),
    Bass(F_CLEF, .25f, new NoteName(NoteLetter.G(), 3, Accidental.Natural()));

    private float yOffset;
    private String glyph;

    private final Map<NoteLetterAndOctave, Integer> noteToStaffPosition = new HashMap<>();


    Clef(String glyph, float yOffset, NoteName baseNote) {
        this.glyph = glyph;
        this.yOffset = yOffset;

        Enumeration.Value baseNoteLetter = baseNote.letter();
        noteToStaffPosition.put(new NoteLetterAndOctave(baseNoteLetter, baseNote.octave()), 0);

        { // walk up the staff and assign values
            int octave = baseNote.octave();
            Enumeration.Value noteLetter = baseNoteLetter;
            for (int position = 1; position < 50; position++) {
                noteLetter = NoteLetter.next(noteLetter);
                if (noteLetter.equals(NoteLetter.C())) {
                    octave++;
                }
//                System.out.println(noteLetter + "" + octave + " <- " + position );
                noteToStaffPosition.put(new NoteLetterAndOctave(noteLetter, octave), position);
            }
        }

        { // walk down the staff and assign values
            int octave = baseNote.octave();
            Enumeration.Value noteLetter = baseNoteLetter;
            for (int position = -1; position > -50; position--) {
                noteLetter = NoteLetter.prev(noteLetter);
                if (noteLetter.equals(NoteLetter.B())) {
                    octave--;
                }
                noteToStaffPosition.put(new NoteLetterAndOctave(noteLetter, octave), position);
            }
        }
    }

    void drawGlyph(PGraphics pG, float size) {
        pG.pushMatrix();
        {
            pG.translate(0, -size * yOffset);
            pG.scale(1, -1);
            pG.text(glyph, 0, 0);
        }
        pG.popMatrix();
    }

    int getStaffPosition(Key key, MusicNote note) {
        NoteName noteName = key.getNoteName(note.note);
        return noteToStaffPosition.get(new NoteLetterAndOctave(noteName.letter(), noteName.octave()));
    }

    class NoteLetterAndOctave {
        Enumeration.Value letter;
        int octave;

        NoteLetterAndOctave(Enumeration.Value l, int o) {
            letter = l;
            octave = o;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(letter, octave);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof NoteLetterAndOctave)){
                return false;
            }

            NoteLetterAndOctave that = (NoteLetterAndOctave)obj;
            return that.octave == octave && that.letter.equals(letter);
        }
    }

}
