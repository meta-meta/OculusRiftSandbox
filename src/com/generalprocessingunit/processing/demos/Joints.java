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
import java.util.List;

public class Joints extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", Joints.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }


    Camera camera = new Camera();

    Segment seg;
    Segment seg2;

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();
//        frame.setLocation(0,1200);

        camera.setLocation(0, 150, -300);
        camera.pitch(.3f);
        camera.yaw(.1f);

        p5 = this;
        seg = new Segment(25, 20);
        seg2 = new Segment(25, 20);

        seg2.setLocation(50, 0, 0);
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
        seg.draw(pG);
        seg2.draw(pG);

    }

    PApplet p5;
    List<Segment> segs = new ArrayList<>();
PGraphics pG;

    class Segment extends EuclideanSpaceObject {
        float radius;
        int descendents;
        int index;

        Segment(float radius, int descendents) {
            this.radius = radius;
            this.descendents = descendents;

            if(descendents > 0) {
                addChild(new Segment(radius * .85f, descendents - 1), new PVector(0, radius + radius * .7f, 0));
            }


            adjustChildren();
            index = segs.size();
            segs.add(this);
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
//                mR.add(0, 0, -mR.getValue().z * 1.4f);
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
            for(Segment s : segs.subList(index + 1, segs.size())){
                PVector v = s.getLocation();// PVector.sub(s.getLocation(), getLocation());
                v = o.getOrientationQuat().rotateVector(v); // where this child will be if we rotate

                for(Segment s2 : segs) {
                    if(abs(s2.index - s.index) < 2) { // don't worry about colliding with parent or child
                        continue;
                    }
                    if(v.dist(s2.getLocation()) < s2.radius + s.radius) {
//                        pG.pushMatrix();
//                        {
//                            pG.translate(getLocation().x, getLocation().y, getLocation().z);
//                            pG.translate(100 + v.x, v.y, v.z);
//                            pG.sphere(2);
//                        }
//                        pG.popMatrix();

                        return true;

                    }
                }
            }
            return false;
        }

    }

}
