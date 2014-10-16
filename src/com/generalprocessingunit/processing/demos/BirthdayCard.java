package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.processing.Camera;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.geometry.Icosahedron;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;

public class BirthdayCard extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{"--full-screen", BirthdayCard.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

//    @Override
//    public void init() {
//        // http://wiki.processing.org/w/Undecorated_frame
////        frame.removeNotify();
////        frame.setUndecorated(true);
//        super.init();
//    }


    Camera camera = new Camera();

    Set<PVector> aerogel = new HashSet<>();

    PShape balloon;

    String[] message = ("Hey honey :-) wow, can you believe it? You can read this entire message without moving your eyes! " +
            "Wow! But do you know what else? That's right. You sure love cubes! " +
            "But as it turns out it also happens to be true that presently it is your birthday!" +
            " ... Don't believe me? You must be kidding! Or perhaps you have more important things on your mind. " +
            "Perhaps you are wondering if you might have accidentally forgotten to turn off the stove. Maybe you are " +
            "full of anxiety over whether or not your vectors are normalized. Well, here's something you need not ever " +
            "wonder about: You light up the gazebo in my heart. <3 <3 <3... 1 4 1 5 9 2 6 5 ... This message will repeat ... now:").split(" ");

    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        super.setup();
//        frame.setLocation(0,1200);

        camera.setLocation(100, 90, -200);
        camera.pitch(.3f);
        camera.yaw(.1f);

        for (int i = 0; i < 3000; i++) {
            aerogel.add(new PVector(randShell(), random(-45, 45), random(-45, 45)));
            aerogel.add(new PVector(random(-45, 45), randShell(), random(-45, 45)));
            aerogel.add(new PVector(random(-45, 45), random(-45, 45), randShell()));
        }

        balloon= Icosahedron.createIcosahedron(4, loadImage("face.png"), this);
    }


    float randShell(){
        float x = random(-45, 45);
        x = (x < 0 ? -1 : 1) * max(abs(x), 25);
        return x;
    }

    @Override
    public void draw(PGraphics pG) {

        System.out.println(frameRate);

        camera.camera(pG);


        pG.background(63);


        pG.colorMode(RGB);
        pG.directionalLight(255, 240, 200, -1, -.3f, .4f);


        int delay = 4000;
        if(millis() > delay) {
            String word = message[((millis() - delay) / 240) % message.length].trim();
            int halfLen = word.length() / 2;
            String left = word.substring(0, halfLen) + " ";
            String center = word.substring(halfLen, halfLen + 1);
            String right = " " + word.substring(halfLen + 1);

            pG.pushMatrix();
            {
                pG.noStroke();

                pG.translate(70, 70, 100);
                pG.rotateX(PI);
                pG.textSize(60);

                pG.textAlign(RIGHT);
                pG.fill(255);
                pG.text(left, 0, 0, 0);

                pG.textAlign(CENTER);
                pG.fill(255, 0, 0);
                pG.text(center, 0, 0, 0);

                pG.textAlign(LEFT);
                pG.fill(255);
                pG.text(right, 0, 0, 0);

            }
            pG.popMatrix();
        }



        pG.pushMatrix();
        {
            if(millis() > 2000) {
                pG.noStroke();
                pG.fill(255, 255, 0);

                pG.translate(300, 90, 50);
                pG.rotateY(1 + (millis() / 10000f) % TWO_PI);
                pG.rotateZ(PI);

                float s = min(30, (millis() - 2000) / 200f );
                pG.scale(s, s, 19);

                pG.shape(balloon);
            }

        }
        pG.popMatrix();



        pG.rotateY((millis() / 5000f) % TWO_PI);
        pG.pushMatrix();
        {
            pG.noStroke();
            pG.fill(128, 255);
            pG.box(50);
        }
        pG.popMatrix();

        pG.colorMode(HSB);
        for(PVector v : aerogel) {
            pG.rotateY((millis() / 8000000f) % TWO_PI);

            pG.stroke(random(255), 128,  random(64, 255));
            pG.strokeWeight(random(2, 5));

            pG.point( v.x, v.y, v.z );
        }

    }


}
