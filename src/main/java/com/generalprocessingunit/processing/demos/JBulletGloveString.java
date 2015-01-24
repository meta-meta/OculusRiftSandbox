package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.SpaceNavCamera;
import com.generalprocessingunit.processing.demos.jBulletGloveString.BallChain;
import com.generalprocessingunit.processing.demos.jBulletGloveString.ESOjBullet;
import com.generalprocessingunit.processing.demos.jBulletGloveString.marionetteRig;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import com.generalprocessingunit.processing.vr.controls.HandSpatialized;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.util.*;


public class JBulletGloveString extends PAppletBufferedHeadModel {

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen", "--display=3",*/ JBulletGloveString.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

    SpaceNavCamera camera;

    HandSpatialized glove;

    marionetteRig rig = new marionetteRig();

    @Override
    public void setup() {
        size(1900, 1000, OPENGL);
//        frame.setLocation(1051 , 10);

        super.setup();

        glove = new HandSpatialized(this, this);
        glove.drawArm = false;

        camera = new SpaceNavCamera(this);
        camera.setLocation(0, 0, 1);
        camera.rotate(new YawPitchRoll(PI, 0, 0));
        frameRate(60);


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
        neck.setOrientation(camera.getOrientation());

        camera.camera(pG);

        pG.background(30, 20, 30);
        pG.lights();

        glove.update();
        glove.drawView(pG);

        if(!rig.esOjBullets.isEmpty()){
            for(ESOjBullet frst : chainHeads) {
                // I think this has to do with the way hand location is set each update.
                // setLocation SHOULD be getting called
                frst.setLocation(frst.getLocation());
            }
        }
        rig.draw(pG, this);

    }

    Set<ESOjBullet> chainHeads = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
            println("reset glove");
        }

        if(KeyEvent.VK_S == e.getKeyCode()) {

            List<PVector> locs = Arrays.asList(new PVector(0, 0, .1f), new PVector(0, 0, -.1f), new PVector(.1f, 0, 0), new PVector(-.1f, 0, 0));

            List<PVector> worldLocs = new ArrayList<>();
            for(PVector loc: locs) {
                worldLocs.add(PVector.add(loc, PVector.mult(glove.razerHydraSensor.getLocation(), .01f)));
            }

            List<BallChain> chains = rig.spawnMarionette(worldLocs);

            int i = 0;
            for(BallChain chain: chains) {
                chainHeads.add(chain.head);
                glove.razerHydraSensor.addChild(chain.head, locs.get(i));
                i++;
            }
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
