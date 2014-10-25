package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.MomentumYawPitchRoll;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.space.Camera;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Joints extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", Joints.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }


    Camera camera = new Camera();

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();
//        frame.setLocation(0,1200);

        camera.setLocation(0, 150, -300);
        camera.pitch(.3f);
        camera.yaw(.1f);

        p5 = this;

        new Tentacle(25, 20, new PVector(0, 0, 0));
        new Tentacle(25, 20, new PVector(45, 0, 0));
//        new Tentacle(25, 20, new PVector(45, 0, 45));
//        new Tentacle(25, 20, new PVector(0, 0, 45));
    }


    void update() {

    }

    @Override
    public void draw(PGraphics pG) {
        update();

        camera.camera(pG);


        pG.background(63);


        pG.colorMode(RGB);
        pG.directionalLight(255, 240, 200, -1, -.3f, .4f);
        pG.directionalLight(64, 96, 192, 1, .3f, .4f);
        pG.specular(255);
        pG.shininess(255);
        pG.fill(127);
        pG.noStroke();

        this.pG = pG;

        for(Tentacle t : tentacles) {
            t.draw(pG);
        }
    }

    PApplet p5;
    Set<Tentacle> tentacles = new HashSet<>();

    class Tentacle extends EuclideanSpaceObject {
        int index;
        List<Segment> segs = new ArrayList<>();
        Segment baseSeg;

        Tentacle(float radius, int segments, PVector location) {
            index = tentacles.size();
            tentacles.add(this);

            baseSeg = new Segment(this, radius, segments - 1);
            baseSeg.setLocation(location);
        }

        void draw(PGraphics pG) {
            baseSeg.draw(pG);
        }
    }

    PGraphics pG;

    class Segment extends EuclideanSpaceObject {
        Tentacle tent;
        float radius;
        int descendents;
        int index;

        int color = p5.color(127);

        Segment(Tentacle tent, float radius, int descendents) {
            this.tent = tent;
            this.radius = radius;
            this.descendents = descendents;

            index = tent.segs.size();
            tent.segs.add(this);

            if(descendents > 0) {
                addChild(new Segment(tent, radius * .88f, descendents - 1), new PVector(0, radius + radius * .6f, 0));
            }

            adjustChildren();

        }

        MomentumYawPitchRoll mR = new MomentumYawPitchRoll(p5, .003f);

        void draw(PGraphics pG) {

            if(random(1) < .015f) {
                mR.add(random(-.06f, .06f), random(-.06f, .06f), random(-.06f, .06f));
            }

            mR.friction();

            tryYaw(mR.getValue().yaw());
            tryPitch(mR.getValue().pitch());
            tryRoll(mR.getValue().roll());


            for(EuclideanSpaceObject child : children) {
                ((Segment)child).draw(pG);
            }

            pushMatrixAndTransform(pG);
            {
                pG.fill(color);
                pG.sphere(radius);
            }
            pG.popMatrix();


        }


        void tryYaw(float y) {
            Orientation o = getOrientation();
            o.yaw(y);

            o = new Orientation();
            o.yaw(y);

            if(collision(o)) {
//                mR.reverseZ();
                mR.add(-mR.getValue().yaw() * 1.4f, 0, 0);
            } else {
                yaw(y);
            }
        }

        void tryPitch(float p) {
            Orientation o = getOrientation();
            o.pitch(p);

            boolean outOfBounds = o.yAxis().dist(parent == null ? new PVector(0, 1, 0) : parent.getOrientation().yAxis()) > .5f;

            o = new Orientation();
            o.pitch(p);

            if(outOfBounds || collision(o)) {
//                mR.reverseZ();
                mR.add(0, -mR.getValue().pitch() * 1.4f, 0);
            } else {
                pitch(p);
            }
        }

        void tryRoll(float r) {
            Orientation o = getOrientation();
            o.roll(r);

            boolean outOfBounds = o.yAxis().dist(parent == null ? new PVector(0, 1, 0) : parent.getOrientation().yAxis()) > .5f;


            o = new Orientation();
            o.roll(r);

            if(outOfBounds || collision(o)) {
//                mR.reverseZ();
                mR.add(0, 0, -mR.getValue().roll() * 1.4f);
            } else {
                roll(r);
            }
        }

        boolean collision(Orientation o) {
            for(Segment thisSeg : tent.segs.subList(index + 1, tent.segs.size())){
                PVector v = PVector.sub(thisSeg.getLocation(), getLocation());
                v = o.getOrientationQuat().rotateVector(v); // where this child will be if we rotate
                v = PVector.add(v, getLocation());

                for(Segment thatSeg : tent.segs) {
                    if(abs(thatSeg.index - thisSeg.index) < 2) { // don't worry about colliding with parent or child
                        continue;
                    }
                    if(collision(v, thatSeg.getLocation(), thisSeg.radius, thatSeg.radius)) {
//                        pG.pushMatrix();
//                        {
//                            pG.fill(255, 0, 0);
//                            pG.translate(v.x, v.y, v.z);
//                            pG.box(thisSeg.radius * 2);
//                        }
//                        pG.popMatrix();
//                        thisSeg.color = p5.color(255, 0, 0);
//                        thatSeg.color = p5.color(255, 0, 0);
                        return true;
                    }
                }

                for(Tentacle thatTent : tentacles) {
                    if(thatTent.index == tent.index) {
                        continue;
                    }

                    for(Segment thatSeg : thatTent.segs) {
                        if(collision(v, thatSeg.getLocation(), thisSeg.radius, thatSeg.radius)) {
//                            pG.pushMatrix();
//                            {
//                                pG.fill(255, 0, 0);
//                                pG.translate(v.x, v.y, v.z);
//                                pG.box(thisSeg.radius * 2);
//                            }
//                            pG.popMatrix();
//
//                            pG.pushMatrix();
//                            {
//                                pG.fill(0, 255, 255);
//                                PVector ts = thatSeg.getLocation();
//                                pG.translate(ts.x, ts.y, ts.z);
//                                pG.box(thatSeg.radius * 2);
//                            }
//                            pG.popMatrix();

//                            thisSeg.color = p5.color(255, 0, 0);
//                            thatSeg.color = p5.color(255, 0, 0);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        boolean collision(PVector a, PVector b, float r1, float r2) {
            return PVector.dist(a, b) < r1 + r2;
        }

    }

}
