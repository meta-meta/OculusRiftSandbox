package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.hid.megamux.MegaMuxHost;
import processing.core.PApplet;

public class MegaMuxTest extends PApplet {

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen", "--display=3",*/ MegaMuxTest.class.getCanonicalName()});
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

        size(900, 900, OPENGL);
//        frame.setLocation(0,1200);

    }

    int millis = 0;
    boolean t= false;
    @Override
    public final void draw() {

//        background(255, 0, map(MegaMuxHost.devices.get(0).getInputVal(0), 100, 750, 0, 255));
//        System.out.println(frameRate);

        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {

                fill(255 * (700 - MegaMuxHost.devices.get(0).getInputVal(i)) / 700f);
                rect(x * 300, y * 300, 300, 300);
                i++;
            }
        }

        fill(255 * ((MegaMuxHost.devices.get(0).getInputVal(9)) / 1023f), 0, 0, 127);
        rect(0, 0, 900, 900);

        MegaMuxHost.devices.get(0).setOuputVal(0, t);
        MegaMuxHost.devices.get(0).setOuputVal(1, !t);

        if(millis() - millis > MegaMuxHost.devices.get(0).getInputVal(0)) {
            MegaMuxHost.writeOutputVals();
            millis = millis();
            t = !t;
        }
    }


}
