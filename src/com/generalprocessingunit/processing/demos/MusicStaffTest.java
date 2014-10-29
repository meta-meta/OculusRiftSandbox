package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.MusicalLibrary;
import com.generalprocessingunit.processing.music.*;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.illposed.osc.OSCMessage;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Date;

public class MusicStaffTest extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen",*//* "--display=1",*/ MusicStaffTest.class.getCanonicalName()});
    }

    Key key = MusicalLibrary.KeyOfFs();
    TimeSignature timeSig = TimeSignature.FourFour;
    MusicConductor mc = new MusicConductor(this, 120, RhythmType.ThirtySecond, timeSig);
    int size = 50;

    MusicalStaff trebleStaff = new MusicalStaff(this, size, Clef.Treble, key, mc);
    MusicalStaff bassStaff = new MusicalStaff(this, size, Clef.Bass, key, mc);


    static MusicElementSeq testSeqTreble = new MusicElementSeq();
    static MusicElementSeq testSeqBass = new MusicElementSeq();
    static {


        testSeqTreble.add(new MusicNote(60, RhythmType.Whole));
        testSeqTreble.add(new MusicRest(RhythmType.Whole));

        testSeqTreble.add(new MusicNote(62, RhythmType.Half));
        testSeqTreble.add(new MusicRest(RhythmType.Half));
        testSeqTreble.add(new MusicNote(67, RhythmType.Half));
        testSeqTreble.add(new MusicNote(69, RhythmType.Half));

        testSeqTreble.add(new MusicNote(60, RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(62, RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(64, RhythmType.Quarter));
        testSeqTreble.add(new MusicRest(RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(67, RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(69, RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(71, RhythmType.Quarter));
        testSeqTreble.add(new MusicNote(72, RhythmType.Quarter));

        testSeqTreble.add(new MusicNote(60, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Eighth));
        testSeqTreble.add(new MusicRest(RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Eighth));

        testSeqTreble.add(new MusicNote(60, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Eighth));
        testSeqTreble.add(new MusicRest(RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Eighth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Eighth));

        testSeqTreble.add(new MusicNote(60, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicRest(RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(60, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicRest(RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Sixteenth));

        testSeqTreble.add(new MusicNote(60, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicRest(RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(60, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(62, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(64, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicRest(RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(67, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(69, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(71, RhythmType.Sixteenth));
        testSeqTreble.add(new MusicNote(72, RhythmType.Sixteenth));


        // TODO: might be a good idea to clone a blank sequence from an existing
        for (int i = 0; i < 2; i++) {
            testSeqBass.add(new MusicElementBlank(RhythmType.Whole));
        }
        for (int i = 0; i < 4; i++) {
            testSeqBass.add(new MusicElementBlank(RhythmType.Half));
        }
        for (int i = 0; i < 8; i++) {
            testSeqBass.add(new MusicElementBlank(RhythmType.Quarter));
        }
        for (int i = 0; i < 16; i++) {
            testSeqBass.add(new MusicElementBlank(RhythmType.Eighth));
        }
        for (int i = 0; i < 32; i++) {
            testSeqBass.add(new MusicElementBlank(RhythmType.Sixteenth));
        }

    }

    @Override
    public void setup() {
        size(1800, 900, OPENGL);
//        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        // add at least one blank measure for padding
        for (int i = 0; i < 2; i++) {
            trebleStaff.measureQueue.addMeasure(new Measure(new MusicElementSeq()));
            bassStaff.measureQueue.addMeasure(new Measure(new MusicElementSeq()));
        }

        for (int i = 0; i < 15; i++) {
            trebleStaff.measureQueue.addMeasure(testSeqTreble.getNextMeasure(timeSig));
            bassStaff.measureQueue.addMeasure(testSeqBass.getNextMeasure(timeSig));
        }

        OSC.addListener("/noteOn", new com.illposed.osc.OSCListener() {
            @Override
            public void acceptMessage(Date time, OSCMessage message) {
                noteOn((int) message.getArguments()[0]);
            }
        });

        OSC.addListener("/noteOff", new com.illposed.osc.OSCListener() {
            @Override
            public void acceptMessage(Date time, OSCMessage message) {
                noteOff((int) message.getArguments()[0]);
            }
        });

    }

    void noteOn(int noteNum) {

    }

    void noteOff(int noteNum) {

    }

    void update() {
        mc.start();

        if(mc.isTimeForNextMeasure()) {
            // remove measure
            trebleStaff.measureQueue.removeMeasure();
            bassStaff.measureQueue.removeMeasure();

            // add next measure
            trebleStaff.measureQueue.addMeasure(testSeqTreble.getNextMeasure(timeSig));
            bassStaff.measureQueue.addMeasure(testSeqBass.getNextMeasure(timeSig));
        }
    }

    @Override
    public void draw(PGraphics pG) {
        update();

        pG.background(32);

        pG.fill(127);
        pG.noStroke();
        pG.pushMatrix(); // light grey rectangle
        {
            pG.translate(0, pG.height / 2 - size * 1.5f);
            pG.rect(0, -size * 2.5f, pG.width, size * 7);
        }
        pG.popMatrix();

        pG.pushMatrix(); // Treble staff
        {
            pG.translate(0, pG.height / 2 - size * 1.5f);
            trebleStaff.draw(pG);
        }
        pG.popMatrix();

        pG.pushMatrix(); // Bass staff
        {
            pG.translate(0, pG.height / 2 + size * 1.5f);
            bassStaff.draw(pG);
        }
        pG.popMatrix();
    }

}
