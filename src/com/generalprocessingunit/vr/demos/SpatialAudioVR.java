package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.audio.SineBalls;
import com.generalprocessingunit.hid.Glove;
import com.generalprocessingunit.hid.GloveManager;
import com.generalprocessingunit.processing.Quaternion;
import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.controls.GloveVR;
import com.generalprocessingunit.vr.controls.SpaceNavVR;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;

public class SpatialAudioVR extends PAppletVR {

    SineBalls sineBalls;
    SpaceNavVR spaceNav;
    GloveVR gloveVR = new GloveVR(this);
    GloveManager.Hand leftHand = gloveVR.leftHand;


    static final float roomSize = 25;


    public static void main(String[] args){
        PAppletVR.main(SpatialAudioVR.class);
    }

    @Override
    public void setup() {
        super.setup();

        sineBalls = new SineBalls(this, 15);
        for(SineBalls.SineBall ball : sineBalls.get()) {
            ball.setLocation(
                    random(-roomSize * .4f, roomSize * .4f),
                    random(-roomSize * .4f, roomSize * .4f),
                    random(-roomSize * .4f, roomSize * .4f)
            );
        }

        spaceNav = new SpaceNavVR(this, .001f, .1f);
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {
        pG.pushMatrix();
        {
            pG.translate(0, -.3f, 0);
            pG.noFill();
            pG.stroke(100, 50);
            for (float i = 0; i < 1; i+= .1f) {
                pG.box(i * .5f, .01f, i * .7f);
            }
            pG.stroke(255, 0, 255);
            pG.translate(0, 0, .35f);
            pG.box(.1f, .1f, .1f);
        }
        pG.popMatrix();
    }

    float radius;
    float azimuth;
    float elevation;

    @Override
    protected void updateState() {
        PVector prevLoc = headContainer.getLocation();
        spaceNav.update();
        PVector headLoc = headContainer.getLocation();

        gloveVR.update();



        if(!isInRoom(headLoc, .5f)) {
            float lim = roomSize / 2;
            if(abs(headLoc.x) > lim) {
                spaceNav.momentum.reverseX();
            }

            if(abs(headLoc.y) > lim) {
                spaceNav.momentum.reverseY();
            }

            if(abs(headLoc.z) > lim) {
                spaceNav.momentum.reverseZ();
            }
            headContainer.setLocation(prevLoc);
        }


        for(SineBalls.SineBall ball : sineBalls.get()) {
            ball.prevLocation.set(ball.getLocation());

            int vibe = 0;
            if(millis() % (10 / ball.freq) < (10 / ball.freq) / 3) {
//                float size = 0.05f + 0.01f * sin((ball.freq / 1000f) * millis() / 1000f);
                vibe = 0;
                if(ball.id == 1) {
                    println(vibe);
                }
            } else if(millis() % ball.freq < 2 * ball.freq / 3) {
//                float size = 0.05f + 0.01f * sin((ball.freq / 1000f) * millis() / 1000f);
                vibe = 2;
                if(ball.id == 1) {
                    println(vibe);
                }
            } else {
//                float size = 0.05f + 0.01f * sin((ball.freq / 1000f) * millis() / 1000f);
                vibe = 5;
                if(ball.id == 1) {
                    println(vibe);
                }
            }



            if(leftHand.getLocation().dist(ball.getLocation()) < (ball.size + .01f)){
                if(null == grabbedBall) { // no ball in hand
                    if(!wasGrabbing && leftHand.isGrabbing()) {
                        grabbedBall = ball;
                    }
                } else if(leftHand.isGrabbing()) { // ball in hand, still grabbing

                    leftHand.palm.vibrate(vibe);
                    grabbedBall.setLocation(leftHand.getLocation());
                    grabbedBall.momentum.set(PVector.mult(PVector.sub(grabbedBall.getLocation(), grabbedBall.prevLocation), 4));
                } else { // let go
                    grabbedBall = null;
                }

                if(null == grabbedBall) {
                    leftHand.palm.vibrate(vibe / 2);
                }
                wasGrabbing = leftHand.isGrabbing();
            }

            updateBallAudio(ball);
            if(null == grabbedBall) {
                tryMoveBall(ball);
            }
        }

        sineBalls.updateMaxPatch();
    }

    boolean wasGrabbing = false;
    SineBalls.SineBall grabbedBall = null;

    private void updateBallAudio(SineBalls.SineBall ball) {
        // fake doppler
        float prevDist = PVector.sub(ball.prevLocation, head.getLocation()).mag();
        float currDist = PVector.sub(ball.getLocation(), head.getLocation()).mag();
        float v = currDist - prevDist;
        ball.setTune(-v * 1000);

        // calculate euler angles to audio source
        //TODO: this is not quite right
        PVector headToObj = PVector.sub(ball.getLocation(), head.getLocation());
        radius = headToObj.mag();
        Quaternion q = head.getOrientation().getOrientationQuat();
        q = new Quaternion(-q.w, q.x, q.y, q.z);
        PVector rotatedVector = q.rotateVector(headToObj);
        azimuth = acos(rotatedVector.x / radius);
        elevation = atan2(rotatedVector.z, rotatedVector.y);

        if (rotatedVector.z < 0) {
            azimuth = PI + (PI - azimuth);
        }

        ball.setRadialCoords(azimuth, elevation, radius);
    }


    void tryMoveBall(SineBalls.SineBall ball) {
        PVector nextLoc = PVector.add(ball.getLocation(), ball.momentum.getValue());
        if(isInRoom(nextLoc, ball.size)) {
            ball.translate(ball.momentum.getValue());
        } else {
            float lim = roomSize / 2;
            if(abs(nextLoc.x) > lim) {
                ball.momentum.reverseX();
            }

            if(abs(nextLoc.y) > lim) {
                ball.momentum.reverseY();
            }

            if(abs(nextLoc.z) > lim) {
                ball.momentum.reverseZ();
            }
        }
    }

    boolean isInRoom(PVector v, float padding) {
        float lim = roomSize / 2;
        return abs(v.x) < lim && abs(v.y) < lim && abs(v.z) < lim;
    }

    @Override
    protected void drawView(int eye, PGraphics pG) {
        pG.background(100);

        pG.directionalLight(
                255, 0, 0,
                .3f, -1, .4f
        );

        pG.directionalLight(
                0, 255, 0,
                -.3f, -1, .4f
        );

        pG.directionalLight(
                0, 0, 255,
                .3f, -1, -.4f
        );

        pG.fill(200);
        pG.box(roomSize);

        sineBalls.draw(pG);

        gloveVR.drawView(pG);

//        pG.pushMatrix();
//        {
//            pG.translate(head.x(), head.y(), head.z() + 1f);
//            AxisAngle aa = head.getAxisAngle();
//            pG.rotate(aa.w, aa.x, aa.y, aa.z);
//
//            pG.box(.2f);
//        }
//        pG.popMatrix();

//        PVector locRelToHead = new PVector(sin(azimuth) * cos(elevation), sin(azimuth) * sin(elevation), cos(azimuth));
//        locRelToHead.mult(radius);
//        pG.pushMatrix();
//        {
//            PVector loc = locRelToHead;// head.getTranslationWRTObjectCoords(locRelToHead);
//            pG.translate(loc.x, loc.y, loc.z + 1f);
//            pG.fill(255, 0, 0);
//            pG.box(.1f);
//        }
//        pG.popMatrix();
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            gloveVR.reset();
        }

        if(KeyEvent.VK_G == e.getKeyCode()) {
            gloveVR.invert();
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            spaceNav.invertControl();
        }

        super.keyPressed(e);
    }
}
