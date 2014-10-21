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

    MusicalStaff trebleStaff = new MusicalStaff(this, 60, Clef.Treble, key, timeSig);
    MusicalStaff bassStaff = new MusicalStaff(this, 60, Clef.Bass, key, timeSig);

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();
    }

    @Override
    public void draw(PGraphics pG) {
        pG.background(127);

        pG.pushMatrix();
        {
            pG.translate(0, pG.height / 2 - 70);
            bassStaff.draw(pG);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.translate(0, pG.height / 2 + 70);
            trebleStaff.draw(pG);
        }
        pG.popMatrix();
    }
}
