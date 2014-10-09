package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.controls.Dial;
import com.generalprocessingunit.vr.controls.GloveVR;
import com.generalprocessingunit.vr.controls.SpaceNavVR;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;

public class ExampleWithGlove extends Example {
    GloveVR glove;

    SpaceNavVR spaceNav;

    Dial dial1;
    Dial dial2;
    Dial dial3;

    public static void main(String[] args){
        PAppletVR.main(ExampleWithGlove.class);
    }


    @Override
    public void setup() {
        super.setup();

        spaceNav = new SpaceNavVR(this, .001f, .1f);
        glove = new GloveVR(this);

        // place these in the head container so that they are immobile but we can fly around
        dial1 = new Dial(this, new PVector(-.3f, 1.8f, 0), 0, .1f, .025f);
        dial2 = new Dial(this, new PVector(  0f, 1.8f, 0), 0, .1f, .025f);
        dial3 = new Dial(this, new PVector( .3f, 1.8f, 0), 0, .1f, .025f);
    }

    @Override
    protected void updateState() {
        spaceNav.update();
        glove.update();

        dial1.update(this, glove.leftHand); //TODO: ugh
        dial2.update(this, glove.leftHand); //TODO: ugh
        dial3.update(this, glove.leftHand); //TODO: ugh

        if(headContainer.y() < 2) {
            PVector location = headContainer.getLocation();
            location.y = 2;
            headContainer.setLocation(location);
        }
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {
        /* spacenav*/
        pG.colorMode(RGB);
        pG.fill(255);
        pG.noStroke();

        pG.sphereDetail(5);
        float shipDiameter = .5f;
        for (float x = -shipDiameter; x <= shipDiameter; x += shipDiameter) {
            for (float y = -shipDiameter; y <= shipDiameter; y += shipDiameter) {
                for (float z = -shipDiameter; z <= shipDiameter * 2; z += shipDiameter * 2) {
                    if (abs(x) + abs(y) + abs(z) < Float.MIN_VALUE) {
                        continue;
                    }
                    pG.pushMatrix();
                    pG.translate(x, y, z);
                    pG.sphere(0.01f);
                    pG.popMatrix();
                }

            }
        }
        /* spacenav*/

    }

    @Override
    protected void drawView(int eye, PGraphics pG) {
        super.drawView(eye, pG);
        glove.drawView(pG);


        dial1.draw(pG);
        dial2.draw(pG);
        dial3.draw(pG);

    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
            println("reset glove");
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
