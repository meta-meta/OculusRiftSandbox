package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.music.Accidental;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.NoteName;
import com.generalprocessingunit.processing.ProcessingDelegateComponent;
import com.generalprocessingunit.processing.demos.MusicalFontContstants;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;



public class MusicalStaff extends ProcessingDelegateComponent implements MusicalFontContstants, PConstants {
    private int size;
    private static PFont bravura;
    private Clef clef;
    private TimeSignature timeSig;

    private Key keySig;
    private float staveHeight;


    static MusicNoteSeq testSeq = new MusicNoteSeq();
    static {
        testSeq.add(new MusicNote(60, RhythmType.Whole));
        testSeq.add(new MusicNote(62, RhythmType.Whole));
        testSeq.add(new MusicNote(64, RhythmType.Whole));
        testSeq.add(new MusicNote(65, RhythmType.Whole));
        testSeq.add(new MusicNote(67, RhythmType.Whole));
        testSeq.add(new MusicNote(69, RhythmType.Whole));
        testSeq.add(new MusicNote(71, RhythmType.Whole));
        testSeq.add(new MusicNote(72, RhythmType.Whole));
    }


    public MusicalStaff(PApplet p5, int size, Clef clef, Key keySig, TimeSignature timeSig) {
        super(p5);

        if(null == bravura) {
            bravura = p5.createFont("Bravura.otf", 100, true, charset);
        }

        this.size = size;
        this.clef = clef;
        this.timeSig = timeSig;
        this.keySig = keySig;

        staveHeight = size / 8f;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(PGraphics pG) {
        pG.textFont(bravura);
        pG.textSize(size);

        pG.fill(0);

        float measureWidth = size * timeSig.beatsPerMeasure * .76f;


        pG.pushMatrix();
        {
//            pG.translate(-measuresOnScreen / 2 * measureWidth, 0);
            drawStaves(pG);
            clef.drawGlyph(pG, staveHeight);
        }
        pG.popMatrix();

//        pG.translate(-measuresOnScreen / 2 * measureWidth + measureWidth - (p5.millis() / 20f) % measureWidth, 0, 0);


//        for (int i = 0; i < measuresOnScreen + 2; i++) {
//            pG.pushMatrix();
//            {
//                pG.translate(i * measureWidth, 0, 0);
//                drawMeasure(pG);
//            }
//            pG.popMatrix();
//        }
    }

    private void drawMeasure(PGraphics pG) {
        pG.pushMatrix();
        {
            drawStaves(pG);

            drawNote(new MusicNote(60, RhythmType.Quarter), 1, pG);
            drawNote(new MusicNote(62, RhythmType.Quarter), 2, pG);
            drawNote(new MusicNote(64, RhythmType.Quarter), 3, pG);
            drawNote(new MusicNote(65, RhythmType.Quarter), 4, pG);
//
//            drawNote(new MusicNote(78, RhythmType.Quarter), 1, pG);
//            drawNote(new MusicNote(82, RhythmType.Quarter), 2, pG);
//            drawNote(new MusicNote(83, RhythmType.Quarter), 3, pG);
//            drawNote(new MusicNote(84, RhythmType.Quarter), 4, pG);
        }
        pG.popMatrix();
    }

    private void drawStaves(PGraphics pG) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeSig.beatsPerMeasure + 2; i++) {
            sb.append(STAFF_5);
        }
        pG.text(sb.toString() + BARLINE_SINGLE, 0, 0);
    }

    void drawNote(MusicNote note, int startBeat, PGraphics pG) {
        StringBuilder sb = new StringBuilder();

        sb.append(" ");
        for (int i = 1; i < startBeat; i++) {
            sb.append("  ");
        }
        String spaces = sb.toString();


        pG.pushMatrix();
        {
            pG.translate(0, -size); // move from top of staff to bottom

            drawLedgerLines(note, spaces, pG);

            translateForNote(clef.getStaffPosition(keySig, note), pG);

//            pG.scale(1, -1);

            // TODO: if accidental appears on this staff position earlier in the measure, mark a note in key with natural symbol
            NoteName noteName = keySig.getNoteName(note.noteNumber);
            if(!keySig.isNoteInKey(note.noteNumber)) {
                String accidental = noteName.accidental().equals(Accidental.Sharp()) ? SHARP :
                        noteName.accidental().equals(Accidental.Flat()) ? FLAT : NATURAL;

                pG.pushMatrix();
                {
                    pG.translate(size / 10f, 0);
                    pG.text(spaces.substring(1) + accidental, 0, 0);
                }
                pG.popMatrix();
            }

            pG.text(spaces + note.rhythm.upGlyph, 0, 0);
        }
        pG.popMatrix();
    }

    private void translateForNote(int staffPosition, PGraphics pG) {
        pG.translate(0, staffPosition * staveHeight);
    }

    private void drawLedgerLines(MusicNote note, String spaces, PGraphics pG) {
        for (int i = clef.getStaffPosition(keySig, note); i < 0; i++) {
            drawLedgerLine(i, spaces, pG);
        }

        for (int i = clef.getStaffPosition(keySig, note); i > 8; i--) {
            drawLedgerLine(i, spaces, pG);
        }
    }

    private void drawLedgerLine(int staffPos, String spaces, PGraphics pG) {
        // don't draw in between staves
        if(staffPos % 2 != 0) {
            return;
        }

        pG.pushMatrix();
        {
            translateForNote(staffPos, pG);
//            pG.scale(1, -1);
            pG.text(spaces + MusicalFontContstants.LEDGER, 0, 0);
        }
        pG.popMatrix();
    }

    void drawNotes(PGraphics pG, MusicNoteSeq measure) {
        StringBuilder sb = new StringBuilder();


        for(MusicNote note : measure) {

        }

        for (int i = 0; i < timeSig.beatsPerMeasure; i++) {
            sb.append(measure.get(i));
        }

        pG.text(sb.toString(), 0, 0);
    }
}
