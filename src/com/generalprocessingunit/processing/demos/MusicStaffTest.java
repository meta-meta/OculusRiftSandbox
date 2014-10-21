package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.music.MusicalLibrary;
import com.generalprocessingunit.processing.music.*;
import com.generalprocessingunit.processing.space.Camera;
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

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();
    }

    @Override
    public void draw(PGraphics pG) {
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
