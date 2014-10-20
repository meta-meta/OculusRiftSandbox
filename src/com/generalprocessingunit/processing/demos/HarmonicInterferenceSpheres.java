package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.space.Camera;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;

public class HarmonicInterferenceSpheres extends PAppletBuffered {



    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen",/* "--display=1",*/ HarmonicInterferenceSpheres.class.getCanonicalName()});
    }


    Camera camera = new Camera();

    Set<ToneSphere> toneSpheres = new HashSet<>();

    public static final int POINT_SIZE = 40;
    public static final int RADIUS = 50;


    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();

        camera.setLocation(0, 60, -150);
        camera.pitch(.4f);


        float detail = 20;
        int alpha = 200;
//        float f1 = 1;
//        float f2 = 2.5f;
//        float f3 = 1.5f;
//        float fCoef = .1f;

        float f1 = 3;
        float f2 = 7;
        float f3 = 5;
        float fCoef = .06f;

//        float f1 = 13;
//        float f2 = 17;
//        float f3 = 19;
//        float fCoef = .01f;

        createToneSphere(f1 * fCoef, color(255, 0, 0, alpha), detail);
        createToneSphere(f2 * fCoef, color(0, 255, 0, alpha), detail);
        createToneSphere(f3 * fCoef, color(0, 0, 255, alpha * 1.5f), detail);

    }

    private void createToneSphere(float freq, int color, float detail) {
        ToneSphere t = new ToneSphere(freq, color);
        toneSpheres.add(t);

        for (float g = 0; g <= PI; g += PI / detail) {
            float radius = RADIUS * cos(g-HALF_PI);
            float y = RADIUS * sin(g - HALF_PI);
            for (float f = 0; f < TWO_PI; f += TWO_PI / detail) {
                float x = cos(f);
                float z = sin(f);
                t.addChild(new EuclideanSpaceObject(), new PVector(radius * x, y, radius * z));
            }
        }
    }

    int prevMillis = 0;

    public void update() {
        int ellapsedMillis = millis() - prevMillis;
        prevMillis = millis();

        int i = 0;
        for(ToneSphere ts : toneSpheres) {
//            if(i % 3 == 1) {
//                ts.yaw((TWO_PI * (ts.freq * ellapsedMillis / 1000f)) % TWO_PI);
//                ts.pitch((TWO_PI * (ts.freq / 7 * ellapsedMillis / 1000f)) % TWO_PI);
//
//            } else if (i % 3 == 1) {
//
//                ts.pitch(( TWO_PI * (ts.freq * ellapsedMillis / 1000f) ) % TWO_PI );
//                ts.roll((TWO_PI * (ts.freq / 7 * ellapsedMillis / 1000f)) % TWO_PI);
//
//            } else {
//
//                ts.roll((TWO_PI * (ts.freq * ellapsedMillis / 1000f)) % TWO_PI);
//                ts.yaw((TWO_PI * (ts.freq / 7 * ellapsedMillis / 1000f)) % TWO_PI);
//
//            }
//            i++;
            ts.yaw((TWO_PI * (ts.freq * ellapsedMillis * .001f)));

        }
    }

    @Override
    public void draw(PGraphics pG) {
        update();
        pG.background(0, 5);

//        Lighting.WhiteCube(pG);

        camera.camera(pG);

        pG.blendMode(ADD);
        for(ToneSphere ts : toneSpheres) {
            pG.stroke(ts.color);
            pG.fill(ts.color);
            pG.strokeWeight(POINT_SIZE);
            for(EuclideanSpaceObject eso : ts.children) {
                eso.pushMatrixAndTransform(pG);
                {
//                    pG.translate(eso.x(), eso.y(), eso.z());
//                    pG.sphere(POINT_SIZE / 2f);
                    pG.point(eso.x(), eso.y(), eso.z());
                }
                pG.popMatrix();
            }
        }


    }

    class ToneSphere extends EuclideanSpaceObject {
        float freq;
        int color;

        ToneSphere(float freq, int color) {
            super();
            this.freq = freq;
            this.color = color;
        }
    }
}
