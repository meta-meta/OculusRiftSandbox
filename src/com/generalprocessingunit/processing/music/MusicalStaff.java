package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.music.Accidental;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.NoteName;
import com.generalprocessingunit.processing.ProcessingDelegateComponent;
import com.generalprocessingunit.processing.demos.MusicalFontConstants;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;


public class MusicalStaff extends ProcessingDelegateComponent implements MusicalFontConstants, PConstants {
    private int size;
    private static PFont bravura;
    private Clef clef;
    private TimeSignature timeSig;

    private Key keySig;
    private float staveHeight;
    private float gridWidth;
    private float glyphWidth;

    public MeasureQueue measureQueue = new MeasureQueue();

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


    public int millisSinceMeasureStart = 0;

    @Override
    public void draw(PGraphics pG) {
        pG.textFont(bravura);
        pG.textSize(size);
        glyphWidth = pG.textWidth(STAFF_5);
        gridWidth = glyphWidth * 2;

        pG.pushMatrix();
        {
            // clef
            pG.translate(glyphWidth * 8 + pG.textWidth(BARLINE_SINGLE), 0);


            // scroll
            float currMeasureWidth = glyphWidth * ((measureQueue.prevMeasure().numElements() + 1) * 2 + 1);
            float measureTime = timeSig.totalValOfMeasure() * 2000;
            pG.translate(-millisSinceMeasureStart * currMeasureWidth / measureTime, 0);

            pG.fill(64);
            drawMeasure(pG, measureQueue.prevMeasure());

            pG.fill(0);
            Measure prevMeasure = measureQueue.prevMeasure();
            for (int i = 0; i < 15; i++) {
                pG.translate(glyphWidth * ((prevMeasure.numElements() + 1) * 2 + 1), 0);
                prevMeasure = measureQueue.nMeasuresFromCurrent(i);
                drawMeasure(pG, measureQueue.nMeasuresFromCurrent(i));
            }
        }
        pG.popMatrix();

        drawClef(pG);
    }

    private void drawClef(PGraphics pG) {
        pG.fill(64);
        pG.noStroke();

        pG.rect(0, -size * 2.5f, glyphWidth * 8 + pG.textWidth(BARLINE_SINGLE), size * 4);

        pG.fill(0);

        pG.pushMatrix();
        {
            Measure.drawStaves(8, pG); // number is fudged because we are not spacing things the same
            clef.drawGlyph(pG, staveHeight);
            pG.translate(size * .6f, 0);
            clef.drawKeySignature(pG, keySig, staveHeight, glyphWidth);
        }
        pG.popMatrix();
    }

    private void drawMeasure(PGraphics pG, Measure measure) {
        Measure.drawStaves(pG, measure.numElements());

        pG.pushMatrix();
        {
            for(MusicElement mE : measure.elementSeq) {
                pG.translate(gridWidth, 0);

                if(mE instanceof MusicNote) {
                    drawNote((MusicNote)mE, pG);
                }
            }
        }
        pG.popMatrix();
    }

    void drawNote(MusicNote note, PGraphics pG) {
        pG.pushMatrix();
        {
//            pG.translate(0, -size); // move from top of staff to bottom

            drawLedgerLines(note, pG);

            translateForNote(clef.getStaffPosition(keySig, note), pG);

            // TODO: if accidental appears on this staff position earlier in the measure, mark a note in key with natural symbol
            NoteName noteName = keySig.getNoteName(note.noteNumber);
            if(!keySig.isNoteInKey(note.noteNumber)) {
                String accidental = accidentalToGlyph(noteName);

                pG.pushMatrix();
                {
                    pG.translate( -glyphWidth / 2f, 0);
                    pG.text(accidental, 0, 0);
                }
                pG.popMatrix();
            }

            pG.text(note.getGlyph(), 0, 0);
        }
        pG.popMatrix();
    }

    static String accidentalToGlyph(NoteName noteName) {
        return noteName.accidental().equals(Accidental.Sharp()) ? SHARP :
                noteName.accidental().equals(Accidental.Flat()) ? FLAT : NATURAL;
    }

    private void translateForNote(int staffPosition, PGraphics pG) {
        pG.translate(0, -staffPosition * staveHeight);
    }

    private void drawLedgerLines(MusicNote note, PGraphics pG) {
        for (int i = clef.getStaffPosition(keySig, note); i < 0; i++) {
            drawLedgerLine(i, pG);
        }

        for (int i = clef.getStaffPosition(keySig, note); i > 8; i--) {
            drawLedgerLine(i, pG);
        }
    }

    private void drawLedgerLine(int staffPos, PGraphics pG) {
        // don't draw in between staves
        if(staffPos % 2 != 0) {
            return;
        }

        pG.pushMatrix();
        {
            translateForNote(staffPos, pG);
            pG.text(MusicalFontConstants.LEDGER, 0, 0);
        }
        pG.popMatrix();
    }

    void drawNotes(PGraphics pG, MusicElementSeq measure) {
        StringBuilder sb = new StringBuilder();


        for(MusicElement mE : measure) {

        }

        for (int i = 0; i < timeSig.beatsPerMeasure; i++) {
            sb.append(measure.get(i));
        }

        pG.text(sb.toString(), 0, 0);
    }
}
