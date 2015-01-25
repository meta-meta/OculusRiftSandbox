package com.generalprocessingunit.processing.demos.vr;

import com.generalprocessingunit.processing.demos.jBulletGloveString.BeadChain;
import com.generalprocessingunit.processing.demos.jBulletGloveString.ESOjBullet;
import com.generalprocessingunit.processing.demos.jBulletGloveString.marionetteRig;
import com.generalprocessingunit.processing.vr.PAppletVR;
import com.generalprocessingunit.processing.vr.controls.HandSpatialized;
import com.generalprocessingunit.processing.vr.controls.SpaceNavVR;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class JBulletGloveString extends PAppletVR {

    public static void main(String[] args){
        PAppletVR.main(JBulletGloveString.class);
    }

    SpaceNavVR spaceNav;

    HandSpatialized glove;

    marionetteRig rig;

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
//        size(1900, 1000, OPENGL);
//        frame.setLocation(1051 , 10);

        super.setup();

        rig = new marionetteRig(this);

        glove = new HandSpatialized(this, this);

        spaceNav = new SpaceNavVR(this, .0007f, .03f);

        headContainer.yaw(PI);
        headContainer.translateWRTObjectCoords(new PVector(0, .2f, -.7f ));
    }

    @Override
    protected void updateState() {
        spaceNav.update();
        glove.update();

        if(!rig.rigObjects.isEmpty()){
            for(ESOjBullet frst : chainHeads) {
                // I think this has to do with the way hand location is set each update.
                // setLocation SHOULD be getting called
                frst.setLocation(frst.getLocation());
            }
        }

        rig.update(this);
    }

    @Override
    public final void drawView(int eye, PGraphics pG) {
        pG.background(100, 90, 100);
        pG.lights();

        glove.drawView(pG);

        rig.draw(pG);
    }

    Set<ESOjBullet> chainHeads = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        rig.keyPressed(e, glove, chainHeads);

        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
            println("reset glove");

            recenterPose();

        }

        if(KeyEvent.VK_G == e.getKeyCode()) {
            glove.invert();
            println("invert glove");
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            spaceNav.invertControl();
            println("invert spacenav");
        }

        super.keyPressed(e);
    }

}
