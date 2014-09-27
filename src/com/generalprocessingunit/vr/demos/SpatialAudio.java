package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.audio.SineBalls;
import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.Quaternion;
import com.generalprocessingunit.vr.PAppletVR;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SpatialAudio extends PAppletVR {

    SineBalls sineBalls;
    SpaceNavigator spaceNav;
    PVector momentum = new PVector();

    static final float drag = 0.0125f;
    static final float speedCoef = 0.0004f;

    static boolean invertedControl = false;


    public static void main(String[] args){
        PAppletVR.main(SpatialAudio.class);
    }

    @Override
    public void setup() {
        super.setup();

        sineBalls = new SineBalls(this);

        spaceNav = new SpaceNavigator(this);
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {
    }

    float radius;
    float azimuth;
    float elevation;

    @Override
    protected void updateState() {
        spaceNav.poll();
        int i = invertedControl ? -1 : 1;
        momentum.add(PVector.mult(spaceNav.translation, i * speedCoef));

        momentum.x = momentum.x - momentum.x * drag;
        momentum.y = momentum.y - momentum.y * drag;
        momentum.z = momentum.z - momentum.z * drag;

        SineBalls.SineBall ball = sineBalls.balls.get(0);
        ball.translate(momentum);

        // fake doppler
        PVector nextPos = PVector.add(ball.getLocation(), momentum);
        float v = nextPos.mag() - ball.getLocation().mag();
        ball.setTune(-v * 1000);

        sineBalls.update();
//        sineBalls.updateMaxPatch();


        // calculate euler angles to audio source
        PVector headToObj = PVector.sub(ball.getLocation(), head.getLocation());
        radius = headToObj.mag();
        Quaternion q = head.getOrientation().getOrientationQuat();
        q = new Quaternion(-q.w, q.x, q.y, q.z);
        PVector rotatedVector = q.rotateVector(headToObj);
//        PVector rotatedVector =headToObj;
        azimuth = acos(rotatedVector.x / radius);
        elevation = atan2(rotatedVector.z, rotatedVector.y);
//        println(rotatedVector.z / radius);

        if(rotatedVector.z < 0) {
            azimuth = PI + (PI - azimuth);
        }
//        println("az ", azimuth, "el ", elevation, "r " + radius);
//        println(rotatedVector);

        ball.updateMaxPatch(azimuth, elevation, radius);
    }

    @Override
    protected void drawView(int eye, PGraphics pG) {
        pG.background(100);

        pG.directionalLight(255, 255, 255, .3f, -1, .4f);

//        sineBalls.draw(pG);

        sineBalls.balls.get(0).draw(pG);

        pG.pushMatrix();
        {
            pG.translate(head.x(), head.y(), head.z() + 1f);
            AxisAngle aa = head.getAxisAngle();
            pG.rotate(aa.w, aa.x, aa.y, aa.z);

            pG.box(.2f);
        }
        pG.popMatrix();



        PVector locRelToHead = new PVector(sin(azimuth) * cos(elevation), sin(azimuth) * sin(elevation), cos(azimuth));
        locRelToHead.mult(radius);
        pG.pushMatrix();
        {
            PVector loc = locRelToHead;// head.getTranslationWRTObjectCoords(locRelToHead);
            pG.translate(loc.x, loc.y, loc.z + 1f);
            pG.fill(255, 0, 0);
            pG.box(.1f);
        }
        pG.popMatrix();
    }

}
