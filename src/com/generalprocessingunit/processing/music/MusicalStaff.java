package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.processing.ProcessingDelegateComponent;
import com.generalprocessingunit.processing.demos.MusicalFontContstants;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.List;


public class MusicalStaff extends ProcessingDelegateComponent implements MusicalFontContstants, PConstants {
    public static final int SIZE = 60;
    private static PFont bravura;

    static List<MusicNote> testSeq = new MusicNoteSeq();
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

    public static final TimeSignature timeSig = TimeSignature.FourFour;
    int measuresOnScreen = 6;


    public MusicalStaff(PApplet p5) {
        super(p5);
        bravura = p5.createFont("Bravura.otf", 100, true, charset);
    }

    MusicConductor mc = new MusicConductor(60, RhythmType.ThirtySecond, timeSig);

    @Override
    public void update() {

    }

    @Override
    public void draw(PGraphics pG) {
        pG.textFont(bravura);
        pG.textSize(SIZE);

        pG.fill(0);
        pG.directionalLight(255, 240, 200, -1, -.3f, .4f);

        float measureWidth = SIZE * 2.53f;
        pG.translate(-measuresOnScreen / 2 * measureWidth - (p5.millis() / 20f) % measureWidth, 0, 0);
        for (int i = 0; i < measuresOnScreen + 2; i++) {
            pG.pushMatrix();
            {
                pG.translate(i * measureWidth, 0, 0);
                drawMeasure(pG);
            }
            pG.popMatrix();
        }
    }

    private void drawMeasure(PGraphics pG) {
        pG.pushMatrix();
        {
            pG.text(BARLINE_SINGLE + STAFF_5 + STAFF_5 + STAFF_5 + STAFF_5 + STAFF_5 + BARLINE_SINGLE, 0, 0);

            drawNote(new MusicNote(72, RhythmType.Quarter), 1, pG);
            drawNote(new MusicNote(60, RhythmType.Quarter), 1, pG);
            drawNote(new MusicNote(59, RhythmType.Quarter), 2, pG);
            drawNote(new MusicNote(57, RhythmType.Quarter), 3, pG);

            drawNote(new MusicNote(78, RhythmType.Quarter), 1, pG);
            drawNote(new MusicNote(82, RhythmType.Quarter), 2, pG);
            drawNote(new MusicNote(83, RhythmType.Quarter), 3, pG);
            drawNote(new MusicNote(84, RhythmType.Quarter), 4, pG);
        }
        pG.popMatrix();
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
            pG.translate(0, -SIZE ); // move from top of staff to bottom

            drawLedgerLines(note, spaces, pG);

            translateForNote(note.staffPosition, pG);

            pG.scale(1, -1);

//            pG.text(spaces.substring(1) + SHARP, 0, 0);

            pG.text(spaces + note.rhythm.upGlyph, 0, 0);
        }
        pG.popMatrix();
    }

    private void translateForNote(int staffPosition, PGraphics pG) {
        pG.translate(0, staffPosition * (SIZE / 8f) );
    }

    private void drawLedgerLines(MusicNote note, String spaces, PGraphics pG) {
        for (int i = note.staffPosition; i < 0; i++) {
            drawLedgerLine(i, spaces, pG);
        }

        for (int i = note.staffPosition; i > 8; i--) {
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
            pG.scale(1, -1);
            pG.text(spaces + MusicalFontContstants.LEDGER, 0, 0);
        }
        pG.popMatrix();
    }

    void drawStaffLines(PGraphics pG) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeSig.beatsPerMeasure; i++) {
            sb.append(STAFF_5);
        }
        pG.text(sb.toString(), 0, 0);
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
