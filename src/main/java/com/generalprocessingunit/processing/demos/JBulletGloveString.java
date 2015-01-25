package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.SpaceNavCamera;
import com.generalprocessingunit.processing.demos.jBulletGloveString.BeadChain;
import com.generalprocessingunit.processing.demos.jBulletGloveString.ESOjBullet;
import com.generalprocessingunit.processing.demos.jBulletGloveString.marionetteRig;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import com.generalprocessingunit.processing.vr.controls.HandSpatialized;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.util.*;


public class JBulletGloveString extends PAppletBufferedHeadModel {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", /*"--display=3",*/ JBulletGloveString.class.getCanonicalName()});
    }

    SpaceNavCamera camera;

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
        glove.color.A = 100;

        camera = new SpaceNavCamera(this);
        camera.setLocation(0, .2f, 1);
        camera.rotate(new YawPitchRoll(PI, .45f, 0));
        frameRate(60);

        camera.fov = PI / 2.4f;

    }


    @Override
    public final void draw(PGraphics pG) {
        camera.update();
        head.setLocation(camera.getLocation());
        head.setOrientation(camera.getOrientation());
        headContainer.setLocation(camera.getLocation());
        headContainer.setOrientation(camera.getOrientation());
        /* Neck Location Object
            * */
        neck.setLocation(head.x(), head.y() - .01f, head.z());
        Orientation o = camera.getOrientation().rotateFrom(new YawPitchRoll(0, -.45f, 0));
        neck.setOrientation(o);

        camera.camera(pG);

        pG.background(100, 90, 100);
        pG.lights();

        glove.update();
        glove.update();
        glove.leftHand.gloveDevice.pitch(-.5f);


        if(!rig.rigObjects.isEmpty()){
            for(ESOjBullet frst : chainHeads) {
                // I think this has to do with the way hand location is set each update.
                // setLocation SHOULD be getting called
                frst.setLocation(frst.getLocation());
            }
        }
        rig.update(this);
        rig.draw(pG);

        glove.drawView(pG);
    }

    Set<ESOjBullet> chainHeads = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        rig.keyPressed(e, glove, chainHeads);

        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
            println("reset glove");
        }

        if(KeyEvent.VK_G == e.getKeyCode()) {
            glove.invert();
            println("invert glove");
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            camera.inverseControls = !camera.inverseControls;
            println("invert spacenav");
        }

        super.keyPressed(e);
    }

}
