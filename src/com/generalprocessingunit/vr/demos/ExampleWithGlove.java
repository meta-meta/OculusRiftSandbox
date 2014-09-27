package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.*;
import com.generalprocessingunit.hid.GloveManager;
import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.controls.GloveVR;
import com.generalprocessingunit.vr.controls.SpaceNavVR;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;

public class ExampleWithGlove extends Example {



    GloveVR glove;

    SpaceNavVR spaceNav;

    public static void main(String[] args){
        PAppletVR.main(ExampleWithGlove.class);
    }


    @Override
    public void setup() {
        super.setup();

        spaceNav = new SpaceNavVR(this, .001f, 1);
        glove = new GloveVR(this);

    }

    @Override
    protected void updateState() {
        spaceNav.update();
        glove.update();

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
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
        }

        if(KeyEvent.VK_G == e.getKeyCode()) {
            glove.invert();
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            spaceNav.invertControl();
        }

        super.keyPressed(e);
    }
}
