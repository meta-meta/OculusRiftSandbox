package com.generalprocessingunit.processing.demos.vr;

import com.generalprocessingunit.processing.vr.PAppletVR;
import processing.core.PGraphics;


public class PS3Eye32bitOnly extends PAppletVR {

    public static void main(String[] args){
        PAppletVR.main(PS3Eye32bitOnly.class);
    }

    PS3Eyes ps3Eyes;

    @Override
    public void setup() {
        super.setup();
        ps3Eyes = new PS3Eyes(this);
    }

    @Override
    protected void updateState() { }


    @Override
    protected void drawViewPreCamera(int eye, PGraphics pG) {
        pG.image(ps3Eyes.getImage(eye), 0, 0, pG.width, pG.height);
    }

    @Override
    protected void drawView(int eye, PGraphics pG) { }


    @Override
    public void dispose() {
        ps3Eyes.dispose();
        super.dispose();
    }
}
