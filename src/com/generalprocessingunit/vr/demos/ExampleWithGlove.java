package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.hid.GloveManager;
import com.generalprocessingunit.hid.RazerHydraManager;
import com.generalprocessingunit.vr.PAppletVR;
import javafx.scene.input.KeyCode;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;

public class ExampleWithGlove extends Example {

    GloveManager gloveManager = new GloveManager();
    GloveManager.LeftHand leftHand = gloveManager.getLeftHand();
    GloveManager.RightHand rightHand = gloveManager.getRightHand();
    //TODO: these calls should be condensed to GloveManger.getLeftHand and GloveManager should be a singleton

    public static void main(String[] args){
        PAppletVR.main(ExampleWithGlove.class);
    }


    @Override
    public void setup() {
        super.setup();

        gloveManager.init();
    }

    @Override
    protected void updateState() {

    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {

//        pG.translate(0f, 0f, -.25f);

        drawHand(leftHand, pG);
//        drawHand(rightHand, pG);

    }

    void drawHand(GloveManager.Hand hand, PGraphics pG) {
        pG.pushMatrix();
        PVector loc = hand.getLocation();
        pG.translate(loc.x, loc.y, loc.z);
        AxisAngle aa = hand.getAxisAngle();
        pG.rotate(aa.w, aa.x, aa.y, aa.z);
        pG.fill(255);
        pG.box(0.05f);
        pG.popMatrix();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            leftHand.reset();
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            leftHand.toggleInvertedLocation();
        }

        super.keyPressed(e);
    }
}
