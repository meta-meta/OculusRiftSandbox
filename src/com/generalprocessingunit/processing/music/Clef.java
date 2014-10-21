package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.music.Accidental;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.NoteLetter;
import com.generalprocessingunit.music.NoteName;
import com.generalprocessingunit.processing.demos.MusicalFontContstants;
import com.google.common.base.Objects;
import processing.core.PGraphics;
import scala.Enumeration;
import scala.collection.immutable.VectorIterator;

import java.util.HashMap;
import java.util.Map;

public enum Clef implements MusicalFontContstants {
    Treble(G_CLEF, 2, new NoteName(NoteLetter.E(), 5, Accidental.Natural())),
    Bass(F_CLEF, 6, new NoteName(NoteLetter.G(), 3, Accidental.Natural()));

    private float yOffset;
    private String glyph;
    private int baseOctave;

    private final Map<NoteLetterAndOctave, Integer> noteToStaffPosition = new HashMap<>();


    Clef(String glyph, float yOffset, NoteName baseNote) {
        this.glyph = glyph;
        this.yOffset = yOffset;
        baseOctave = baseNote.octave() + 1;

        initNoteToStaffPositionMap(baseNote);
    }

    private void initNoteToStaffPositionMap(NoteName baseNote) {
        Enumeration.Value basePitchClass = baseNote.letter();
        noteToStaffPosition.put(new NoteLetterAndOctave(basePitchClass, baseNote.octave()), 0);

        { // walk up the staff and assign values
            int octave = baseNote.octave();
            Enumeration.Value pitchClass = basePitchClass;
            for (int position = 1; position < 50; position++) {
                pitchClass = NoteLetter.next(pitchClass);

                if (pitchClass.equals(NoteLetter.C())) {
                    octave++;
                }
//                System.out.println(pitchClass + "" + octave + " <- " + position );

                noteToStaffPosition.put(new NoteLetterAndOctave(pitchClass, octave), position);
            }
        }

        { // walk down the staff and assign values
            int octave = baseNote.octave();
            Enumeration.Value pitchClass = basePitchClass;
            for (int position = -1; position > -50; position--) {
                pitchClass = NoteLetter.prev(pitchClass);

                if (pitchClass.equals(NoteLetter.B())) {
                    octave--;
                }

                noteToStaffPosition.put(new NoteLetterAndOctave(pitchClass, octave), position);
            }
        }
    }

    void drawGlyph(PGraphics pG, float staveHeight) {
        pG.pushMatrix();
        {
            pG.translate(10, -staveHeight * yOffset);
            pG.text(glyph, 0, 0);
        }
        pG.popMatrix();
    }

    void drawKeySignature(PGraphics pG, Key keySig, float staveHeight, float glyphWidth) {
        VectorIterator<NoteName> iter = keySig.sharpsAndFlats().iterator();

        while(iter.hasNext()) {
            NoteName noteName = iter.next();

            pG.translate(glyphWidth, 0);
            pG.pushMatrix();
            {
                int staffPos = getStaffPosition(new NoteName(noteName.letter(), baseOctave, noteName.accidental()));
                // constrain so that there are no ledger lines
                if(staffPos > 9) {
                    staffPos -= 7;
                }

                pG.translate(0, -staveHeight * staffPos);
                pG.text(MusicalStaff.accidentalToGlyph(noteName), 0, 0);
            }
            pG.popMatrix();
        }
    }

    int getStaffPosition(Key keySig, MusicNote note) {
        NoteName noteName = keySig.getNoteName(note.noteNumber);
        return getStaffPosition(noteName);
    }

    private Integer getStaffPosition(NoteName noteName) {
        return noteToStaffPosition.get(new NoteLetterAndOctave(noteName.letter(), noteName.octave()));
    }

    private class NoteLetterAndOctave {
        Enumeration.Value pitchClass;
        int octave;

        NoteLetterAndOctave(Enumeration.Value pitchClass, int octave) {
            this.pitchClass = pitchClass;
            this.octave = octave;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(pitchClass, octave);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NoteLetterAndOctave)) {
                return false;
            }

            NoteLetterAndOctave that = (NoteLetterAndOctave) obj;
            return that.octave == octave && that.pitchClass.equals(pitchClass);
        }
    }

}
