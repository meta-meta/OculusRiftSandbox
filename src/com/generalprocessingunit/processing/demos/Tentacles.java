package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.MomentumYawPitchRoll;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.space.Camera;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tentacles extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", Tentacles.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }


    Camera camera = new Camera();

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        camera.setLocation(0, 200, -150);
        camera.pitch(.8f);
        camera.yaw(.1f);

        p5 = this;

        new Tentacle(20, 26, new PVector(0, 0, 0));
        new Tentacle(20, 26, new PVector(50, 0, 0));
    }


    void update() {

    }

    @Override
    public void draw(PGraphics pG) {
        update();

        camera.camera(pG);

        pG.background(63);

        pG.sphereDetail(15);
        pG.colorMode(RGB);
        pG.directionalLight(64, 96, 128, 1, .3f, .4f);
        pG.lightSpecular(255, 255, 255);
        pG.directionalLight(255, 240, 200, -1, -.3f, .4f);
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

        int color;

        Segment(Tentacle tent, float radius, int descendents) {
            this.tent = tent;
            this.radius = radius;
            this.descendents = descendents;

            index = tent.segs.size();
            tent.segs.add(this);

            p5.colorMode(HSB);
            color = p5.color(tent.index * 32 + index * 24, 32, 127);

            if(descendents > 0) {
                addChild(new Segment(tent, radius * .82f, descendents - 1), new PVector(0, radius + radius * .4f, 0));

//                if(random(1) < .1f) {
//                    YawPitchRoll ypr = new YawPitchRoll(0, random(1f, 2f), random(1f, 2f));
//                    addChild(new Tentacle(radius * .81f, descendents + 3, PVector.add(getLocation(), new PVector(radius * .6f, 0, 0))), ypr);
//                }
            }


            adjustChildren();
        }

        MomentumYawPitchRoll mR = new MomentumYawPitchRoll(p5, .005f);

        void draw(PGraphics pG) {

            if(random(1) < .005f) {
                mR.add(random(-.06f, .06f), random(-.06f, .06f), random(-.06f, .06f));
            }

            mR.friction();

            tryYaw(mR.getValue().yaw());
            tryPitch(mR.getValue().pitch());
            tryRoll(mR.getValue().roll());


            for(EuclideanSpaceObject child : children) {
                if(child instanceof Segment) {
                    ((Segment) child).draw(pG);
                }
            }

            pushMatrixAndTransform(pG);
            {
                pG.fill(color);
                pG.sphere(radius);
            }
            pG.popMatrix();


        }


        void tryYaw(float y) {
            Orientation o = new Orientation();
            o.yaw(y);

            if(collision(o)) {
                mR.reverseYaw();
            } else {
                yaw(y);
            }
        }

        void tryPitch(float p) {
            Orientation o = getOrientation().clone();
            o.pitch(p);

            boolean outOfBounds = o.yAxis().dist(parent == null ? new PVector(0, 1, 0) : parent.getOrientation().yAxis()) > .9f;

            o = new Orientation();
            o.pitch(p);

            if(outOfBounds || collision(o)) {
                mR.reversePitch();
            } else {
                pitch(p);
            }
        }

        void tryRoll(float r) {
            Orientation o = getOrientation().clone();
            o.roll(r);

            boolean outOfBounds = o.yAxis().dist(parent == null ? new PVector(0, 1, 0) : parent.getOrientation().yAxis()) > .9f;

            o = new Orientation();
            o.roll(r);

            if(outOfBounds || collision(o)) {
                mR.reverseRoll();
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
