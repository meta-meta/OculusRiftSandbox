package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.MusicalLibrary;
import com.generalprocessingunit.processing.music.*;
import com.generalprocessingunit.processing.PAppletBuffered;
import processing.core.PApplet;
import processing.core.PGraphics;

public class MusicStaffTest extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen",/* "--display=1",*/ MusicStaffTest.class.getCanonicalName()});
    }

    Key key = MusicalLibrary.KeyOfFs();
    TimeSignature timeSig = TimeSignature.FourFour;
    MusicConductor mc = new MusicConductor(60, RhythmType.ThirtySecond, timeSig);
    int size = 60;

    MusicalStaff trebleStaff = new MusicalStaff(this, size, Clef.Treble, key, timeSig);
    MusicalStaff bassStaff = new MusicalStaff(this, size, Clef.Bass, key, timeSig);


    int millisAtMeasureStart = 0;

    static MusicElementSeq testSeq = new MusicElementSeq();
    static {
        testSeq.add(new MusicNote(60, RhythmType.Whole));
        testSeq.add(new MusicNote(62, RhythmType.Whole));
        testSeq.add(new MusicNote(64, RhythmType.Whole));
        testSeq.add(new MusicNote(65, RhythmType.Whole));
        testSeq.add(new MusicNote(67, RhythmType.Whole));
        testSeq.add(new MusicNote(69, RhythmType.Whole));
        testSeq.add(new MusicNote(71, RhythmType.Whole));
        testSeq.add(new MusicNote(72, RhythmType.Whole));

        testSeq.add(new MusicNote(60, RhythmType.Half));
        testSeq.add(new MusicNote(62, RhythmType.Half));
        testSeq.add(new MusicNote(64, RhythmType.Half));
        testSeq.add(new MusicNote(65, RhythmType.Half));
        testSeq.add(new MusicNote(67, RhythmType.Half));
        testSeq.add(new MusicNote(69, RhythmType.Half));
        testSeq.add(new MusicNote(71, RhythmType.Half));
        testSeq.add(new MusicNote(72, RhythmType.Half));

        testSeq.add(new MusicNote(60, RhythmType.Quarter));
        testSeq.add(new MusicNote(62, RhythmType.Quarter));
        testSeq.add(new MusicNote(64, RhythmType.Quarter));
        testSeq.add(new MusicNote(65, RhythmType.Quarter));
        testSeq.add(new MusicNote(67, RhythmType.Quarter));
        testSeq.add(new MusicNote(69, RhythmType.Quarter));
        testSeq.add(new MusicNote(71, RhythmType.Quarter));
        testSeq.add(new MusicNote(72, RhythmType.Quarter));

        testSeq.add(new MusicNote(60, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(62, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(64, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(65, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(67, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(69, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(71, RhythmType.Sixteenth));
        testSeq.add(new MusicNote(72, RhythmType.Sixteenth));
    }

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        // add at least one blank measure for padding
        for (int i = 0; i < 2; i++) {
            trebleStaff.measureQueue.addMeasure(new Measure(new MusicElementSeq()));
        }

        for (int i = 0; i < 5; i++) {
            trebleStaff.measureQueue.addMeasure(testSeq.getNextMeasure(timeSig));
        }
    }

    void update() {

        if(millis() - millisAtMeasureStart > timeSig.totalValOfMeasure() * 1000) {
            millisAtMeasureStart = millis();

            // remove measure
            trebleStaff.measureQueue.removeMeasure();

            // add next measure
            trebleStaff.measureQueue.addMeasure(testSeq.getNextMeasure(timeSig));
        }

    }

    @Override
    public void draw(PGraphics pG) {
        update();

        pG.background(32);

        pG.pushMatrix();
        {
            pG.translate(0, pG.height / 2 - size * 1.5f);
            pG.fill(127);
            pG.noStroke();
            pG.rect(0, -size * 2.5f, pG.width, size * 4);
            trebleStaff.draw(pG);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.translate(0, pG.height / 2 + size * 1.5f);
            pG.fill(127);
            pG.noStroke();
            pG.rect(0, -size * 2.5f, pG.width, size * 4);
            bassStaff.draw(pG);
        }
        pG.popMatrix();
    }

}
