package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.*;
import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.controls.Dial;
import com.generalprocessingunit.vr.controls.GloveVR;
import com.generalprocessingunit.vr.controls.SpaceNavVR;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LightCubeControlPanel extends PAppletVR {
    GloveVR glove;

    SpaceNavVR spaceNav;

    List<Dial> dials = new ArrayList<>();
    EuclideanSpaceObject platform = new EuclideanSpaceObject();

    public static void main(String[] args){
        PAppletVR.main(LightCubeControlPanel.class);
    }


    @Override
    public void setup() {
        super.setup();

        spaceNav = new SpaceNavVR(this, .001f, .2f);
        glove = new GloveVR(this);

        for (int y = -3; y < 0; y++) {
            for (float x = -2.5f; x <= 2.5f; x++) {

                dials.add(
                        new Dial(
                                this,
                                new PVector(x * .08f, y * .07f - .1f, (y + 2) * .04f + .3f),
                                new YawPitchRoll(0, .5f, 0),
                                .03f, .02f,
                                new Color((int) ((x + 2) / 4f * 255), 64, (int) ((y + 3) / 3f * 255))
                        ).addAsChildTo(headContainer)
                );
            }
        }

        headContainer.addChild(platform, new PVector(), new YawPitchRoll());
    }

    @Override
    protected void updateState() {
        spaceNav.update();
        glove.update();

        for(Dial d: dials) {
            d.update(this, glove.leftHand);
        }


//        if(millis() < 50000) {
//            int i = (millis() / 400) % 14;
//            Map m = new HashMap<Integer, Integer>();
//            m.put(i, 1);
//            glove.leftHand.setVibrate(m);
//        }


//        if(headContainer.y() < 2) {
//            PVector location = headContainer.getLocation();
//            location.y = 2;
//            headContainer.setLocation(location);
//        }
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {}

    @Override
    protected void drawView(int eye, PGraphics pG) {

        background(0);

        Lighting.WhiteCube(pG,
                dials.get(0).val * 128,  dials.get(6).val * 255, dials.get(12).val * 255,
                dials.get(1).val * 128,  dials.get(7).val * 255, dials.get(13).val * 255,
                dials.get(2).val * 128,  dials.get(8).val * 255, dials.get(14).val * 255,
                dials.get(3).val * 128,  dials.get(9).val * 255, dials.get(15).val * 255,
                dials.get(4).val * 128, dials.get(10).val * 255, dials.get(16).val * 255,
                dials.get(5).val * 128, dials.get(11).val * 255, dials.get(17).val * 255
        );


        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(12).val * 255, dials.get(6).val * 255, dials.get(0).val * 255);
            pG.translate(-2.5f, 0, 0);
            pG.box(.01f, 5, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(13).val * 255, dials.get(7).val * 255, dials.get(1).val * 255);
            pG.translate(2.5f, 0, 0);
            pG.box(.01f, 5, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(14).val * 255, dials.get(8).val * 255, dials.get(2).val * 255);
            pG.translate(0, -2.5f, 0);
            pG.box(5, .01f, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(15).val * 255, dials.get(9).val * 255, dials.get(3).val * 255);
            pG.translate(0, 2.5f, 0);
            pG.box(5, .01f, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(16).val * 255, dials.get(10).val * 255, dials.get(4).val * 255);
            pG.translate(0, 0, -2.5f);
            pG.box(5, 5, .01f);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(dials.get(17).val * 255, dials.get(11).val * 255, dials.get(5).val * 255);
            pG.translate(0, 0, 2.5f);
            pG.box(5, 5, .01f);
        }
        pG.popMatrix();

        pG.emissive(0);


        pG.pushMatrix();
        {

            pG.translate(platform.x(), platform.y(), platform.z());
            AxisAngle aa = platform.getAxisAngle();
            pG.rotate(aa.w, aa.x, aa.y, aa.z);

            /* Panel */
            pG.pushMatrix();
            {
                pG.fill(0, 0, 31);
                pG.translate(0, -.235f, .32f);
                pG.rotateX(.5f);
                pG.box(.55f, .33f, .01f);
            }
            pG.popMatrix();

            /* Floor */
            pG.pushMatrix();
            {
                pG.fill(0, 0, 127);
                pG.translate(0, -.4f, 0);
                pG.box(.55f, .01f, .64f);
            }
            pG.popMatrix();
        }
        pG.popMatrix();

        for(Dial d: dials) {
            d.draw(pG);
        }

        pG.noStroke();
        glove.color.R = 64;
        glove.color.G = 64;
        glove.color.B = 48;
        glove.drawView(pG);
    }


    @Override
    public void keyPressed(KeyEvent e) {
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
