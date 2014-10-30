package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.MusicalLibrary;
import com.generalprocessingunit.processing.music.*;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.illposed.osc.OSCMessage;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MusicStaffTest extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", "--display=1", MusicStaffTest.class.getCanonicalName()});
    }

    Key key = MusicalLibrary.KeyOfC();
    TimeSignature timeSig = TimeSignature.FourFour;
    Conductor mc = new Conductor(this, 120, Duration.ThirtySecond, timeSig);
    int size = 50;

    Set<Integer> currentlyPlayingNotes = Collections.synchronizedSet(new HashSet<Integer>());

    Staff trebleStaff = new Staff(this, size, Clef.Treble, key, mc);
    Staff bassStaff = new Staff(this, size, Clef.Bass, key, mc);


    static ElementSequence testSeqTreble = new ElementSequence();
    static ElementSequence testSeqBass = new ElementSequence();
    static {


        testSeqTreble.add(new Note(60, Duration.Whole));
        testSeqTreble.add(new Rest(Duration.Whole));

        testSeqTreble.add(new Note(62, Duration.Half));
        testSeqTreble.add(new Rest(Duration.Half));
        testSeqTreble.add(new Note(67, Duration.Half));
        testSeqTreble.add(new Note(69, Duration.Half));

        testSeqTreble.add(new Note(60, Duration.Quarter));
        testSeqTreble.add(new Note(62, Duration.Quarter));
        testSeqTreble.add(new Note(64, Duration.Quarter));
        testSeqTreble.add(new Rest(Duration.Quarter));
        testSeqTreble.add(new Note(67, Duration.Quarter));
        testSeqTreble.add(new Note(69, Duration.Quarter));
        testSeqTreble.add(new Note(71, Duration.Quarter));
        testSeqTreble.add(new Note(72, Duration.Quarter));

        testSeqTreble.add(new Note(60, Duration.Eighth));
        testSeqTreble.add(new Note(62, Duration.Eighth));
        testSeqTreble.add(new Note(64, Duration.Eighth));
        testSeqTreble.add(new Rest(Duration.Eighth));
        testSeqTreble.add(new Note(67, Duration.Eighth));
        testSeqTreble.add(new Note(69, Duration.Eighth));
        testSeqTreble.add(new Note(71, Duration.Eighth));
        testSeqTreble.add(new Note(72, Duration.Eighth));

        testSeqTreble.add(new Note(60, Duration.Eighth));
        testSeqTreble.add(new Note(62, Duration.Eighth));
        testSeqTreble.add(new Note(64, Duration.Eighth));
        testSeqTreble.add(new Rest(Duration.Eighth));
        testSeqTreble.add(new Note(67, Duration.Eighth));
        testSeqTreble.add(new Note(69, Duration.Eighth));
        testSeqTreble.add(new Note(71, Duration.Eighth));
        testSeqTreble.add(new Note(72, Duration.Eighth));

        testSeqTreble.add(new Note(60, Duration.Sixteenth));
        testSeqTreble.add(new Note(62, Duration.Sixteenth));
        testSeqTreble.add(new Note(64, Duration.Sixteenth));
        testSeqTreble.add(new Rest(Duration.Sixteenth));
        testSeqTreble.add(new Note(67, Duration.Sixteenth));
        testSeqTreble.add(new Note(69, Duration.Sixteenth));
        testSeqTreble.add(new Note(71, Duration.Sixteenth));
        testSeqTreble.add(new Note(72, Duration.Sixteenth));
        testSeqTreble.add(new Note(60, Duration.Sixteenth));
        testSeqTreble.add(new Note(62, Duration.Sixteenth));
        testSeqTreble.add(new Note(64, Duration.Sixteenth));
        testSeqTreble.add(new Rest(Duration.Sixteenth));
        testSeqTreble.add(new Note(67, Duration.Sixteenth));
        testSeqTreble.add(new Note(69, Duration.Sixteenth));
        testSeqTreble.add(new Note(71, Duration.Sixteenth));
        testSeqTreble.add(new Note(72, Duration.Sixteenth));

        testSeqTreble.add(new Note(60, Duration.Sixteenth));
        testSeqTreble.add(new Note(62, Duration.Sixteenth));
        testSeqTreble.add(new Note(64, Duration.Sixteenth));
        testSeqTreble.add(new Rest(Duration.Sixteenth));
        testSeqTreble.add(new Note(67, Duration.Sixteenth));
        testSeqTreble.add(new Note(69, Duration.Sixteenth));
        testSeqTreble.add(new Note(71, Duration.Sixteenth));
        testSeqTreble.add(new Note(72, Duration.Sixteenth));
        testSeqTreble.add(new Note(60, Duration.Sixteenth));
        testSeqTreble.add(new Note(62, Duration.Sixteenth));
        testSeqTreble.add(new Note(64, Duration.Sixteenth));
        testSeqTreble.add(new Rest(Duration.Sixteenth));
        testSeqTreble.add(new Note(67, Duration.Sixteenth));
        testSeqTreble.add(new Note(69, Duration.Sixteenth));
        testSeqTreble.add(new Note(71, Duration.Sixteenth));
        testSeqTreble.add(new Note(72, Duration.Sixteenth));


        // TODO: might be a good idea to clone a blank sequence from an existing
        for (int i = 0; i < 2; i++) {
            testSeqBass.add(new ElementBlank(Duration.Whole));
        }
        for (int i = 0; i < 4; i++) {
            testSeqBass.add(new ElementBlank(Duration.Half));
        }
        for (int i = 0; i < 8; i++) {
            testSeqBass.add(new ElementBlank(Duration.Quarter));
        }
        for (int i = 0; i < 16; i++) {
            testSeqBass.add(new ElementBlank(Duration.Eighth));
        }
        for (int i = 0; i < 32; i++) {
            testSeqBass.add(new ElementBlank(Duration.Sixteenth));
        }

    }

    @Override
    public void setup() {
//        size(1800, 900, OPENGL);
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        // add at least one blank measure for padding
        for (int i = 0; i < 2; i++) {
            trebleStaff.measureQueue.addMeasure(new Measure(new ElementSequence()));
            bassStaff.measureQueue.addMeasure(new Measure(new ElementSequence()));
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

    void noteOn(Integer noteNum) {
        // for EWI only because we are not receiving noteOff when slurring
        currentlyPlayingNotes.clear();

        currentlyPlayingNotes.add(noteNum);
    }

    void noteOff(Integer noteNum) {
        currentlyPlayingNotes.remove(noteNum);
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

        mc.markPlayedAndMissedNotes(trebleStaff.measureQueue.currentMeasure(), currentlyPlayingNotes);

//        synchronized (currentlyPlayingNotes) {
//            for (Integer n : currentlyPlayingNotes) {
//                print("NNN::: " + n + " ");
//                println();
//            }
//        }
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
