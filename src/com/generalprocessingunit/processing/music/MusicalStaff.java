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
    private MusicConductor mc;

    private Key keySig;
    private float staveHeight;
    private float gridWidth;
    private float glyphWidth;

    public MeasureQueue measureQueue = new MeasureQueue();

    public MusicalStaff(PApplet p5, int size, Clef clef, Key keySig, MusicConductor mc) {
        super(p5);

        if(null == bravura) {
            bravura = p5.createFont("Bravura.otf", 100, true, charset);
        }

        this.size = size;
        this.clef = clef;
        this.mc = mc;
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
        glyphWidth = pG.textWidth(STAFF_5);
        gridWidth = glyphWidth * 2;

        pG.pushMatrix();
        {
            // clef
            pG.translate(glyphWidth * 8 + pG.textWidth(BARLINE_SINGLE), 0);

            // scroll
            float prevMeasureWidth = getMeasureWidth(measureQueue.prevMeasure());
            float trans = prevMeasureWidth * mc.measureProgress();
            pG.translate(-trans, 0);
//            trans /= 2; // alternate scrolling - kind of disorienting
//            pG.translate(-trans - trans * PApplet.sq(mc.measureProgress()), 0);


            // prev measure
            Measure pm = measureQueue.prevMeasure();
            drawMeasure(pG, pm);
            pG.translate(getMeasureWidth(pm), 0);

            // experimental metronome
            drawMetronome(pG);

            // current and later measures
            for (int i = 0; i < 15; i++) {
                Measure m = measureQueue.nMeasuresFromCurrent(i);
                drawMeasure(pG, m);
                pG.translate(getMeasureWidth(m), 0);
            }
        }
        pG.popMatrix();

        drawClef(pG);
    }

    private void drawMetronome(PGraphics pG) {
        pG.fill(64, 0, 64);
        float measureWidth = getMeasureWidth(measureQueue.currentMeasure());
        for (int i = 0; i < 8 * mc.measureProgress(); i++) {
            pG.pushMatrix();
            {
                pG.translate(i * measureWidth / 8f, size / 2f + (i % 2 == 0 ? size / 4f : size / 8f));
                pG.ellipse(size / 10f, size / 10f, size / 10f, size / 10f);
            }
            pG.popMatrix();
        }
    }

    float getMeasureWidth(Measure m) {
        return glyphWidth * ((m.numElements() + 1) * 2 + 1);
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

        if(measure.equals(measureQueue.prevMeasure())) {
            pG.fill(64);
        } else {
            pG.fill(0);
        }
        Measure.drawStaves(pG, measure.numElements());

        pG.pushMatrix();
        {
            float rhythmTotal = 0;
            for(MusicElement mE : measure.elementSeq) {
                pG.translate(gridWidth, 0);

                // TODO: check if note was played by MIDI instrument
                // TODO: abstract the instrument so that acoustic instruments can be used as well

                // have we passed this note yet?
                if(measure.equals(measureQueue.prevMeasure())) {
                    pG.fill(64, 32, 32);
                } else if(measure.equals(measureQueue.currentMeasure()) && mc.millisSinceMeasureStart() > mc.millisForRhythmVal(rhythmTotal)) {
                    pG.fill(0, 0, 96);
                } else {
                    pG.fill(0);
                }

                rhythmTotal += mE.rhythm.val;

                if(mE instanceof MusicNote) {
                    drawNote((MusicNote)mE, pG);
                } else if(mE instanceof MusicRest) {
                    drawRest((MusicRest) mE, pG);
                }
            }
        }
        pG.popMatrix();
    }

    void drawNote(MusicNote note, PGraphics pG) {
        pG.pushMatrix();
        {
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

    void drawRest(MusicRest rest, PGraphics pG) {
        pG.pushMatrix();
        {
            pG.translate(0, -4 * staveHeight);
            pG.text(rest.getGlyph(), 0, 0);
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
}
