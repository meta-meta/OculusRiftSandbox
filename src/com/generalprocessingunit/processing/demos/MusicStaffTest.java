package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.space.Camera;
import com.generalprocessingunit.processing.music.MusicalStaff;
import com.generalprocessingunit.processing.PAppletBuffered;
import processing.core.PApplet;
import processing.core.PGraphics;

public class MusicStaffTest extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen",/* "--display=1",*/ MusicStaffTest.class.getCanonicalName()});
    }

    MusicalStaff staff = new MusicalStaff(this);

    Camera camera = new Camera();

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        camera.setLocation(0, 0, -500);
    }

    @Override
    public void draw(PGraphics pG) {
        pG.background(127);

        camera.camera(pG);

        staff.draw(pG);
    }
}
