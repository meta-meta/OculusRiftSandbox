package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.MusicalLibrary;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.music.*;
import com.illposed.osc.OSCMessage;
import processing.core.PApplet;
import processing.core.PGraphics;
import scala.collection.immutable.Vector;
import scala.collection.immutable.VectorIterator;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MusicStaffTest extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", "--display=1", MusicStaffTest.class.getCanonicalName()});
    }

    Key keySig = MusicalLibrary.KeyOfF();
    TimeSignature timeSig = TimeSignature.FourFour;
    Conductor conductor = new Conductor(this, 60, Duration.ThirtySecond, timeSig);
    int size = 50;

    // synchronized because the OSC listeners are on a different thread
    Set<Integer> currentlyPlayingNotes = Collections.synchronizedSet(new HashSet<Integer>());

    GrandStaff grandStaff = new GrandStaff(this, size, keySig, conductor);

    static ElementSequence testSeq = new ElementSequence();
    static {

        Vector<Object> noteSequence = MusicalLibrary.Modes().head().toRangeExercise(5, MusicalLibrary.EwiRange(), true).sequence();

        VectorIterator<Object> iter = noteSequence.iterator();

        while(iter.hasNext()) {
            int note = (int)iter.next();
            testSeq.add(new Note(note, Duration.Quarter));
        }

    }

    @Override
    public void setup() {
//        size(1800, 900, OPENGL);
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        // add at least one blank measure for padding
        ElementSequence seq = new ElementSequence();
        seq.add(new Rest(Duration.Whole));
        grandStaff.addMeasure(new Measure(seq));

        for (int i = 0; i < 15; i++) {
            grandStaff.addMeasure(testSeq.getNextMeasure(timeSig));
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
        conductor.start();

        if(conductor.isTimeForNextMeasure()) {
            // remove measure
            grandStaff.removeMeasure();

            // add next measure
            grandStaff.addMeasure(testSeq.getNextMeasure(timeSig));
        }

        grandStaff.update(currentlyPlayingNotes);

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

        grandStaff.draw(pG);
    }

}
