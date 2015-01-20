package com.generalprocessingunit.processing.demos;

import processing.core.PApplet;

public class ProcessingTest extends PApplet {

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen", "--display=3",*/ ProcessingTest.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

//    @Override
//    public void init() {
//        // http://wiki.processing.org/w/Undecorated_frame
////        frame.removeNotify();
////        frame.setUndecorated(true);
//        super.init();
//    }

    @Override
    public void setup() {

        size(1200, 800, OPENGL);
//        frame.setLocation(0,1200);

    }

    @Override
    public final void draw() {

        background(255, 0, 0);
        System.out.println(frameRate);

    }


}
