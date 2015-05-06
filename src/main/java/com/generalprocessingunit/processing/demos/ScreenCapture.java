package com.generalprocessingunit.processing.demos;

import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;

public class ScreenCapture extends PApplet {
    Robot robot;

    Rectangle rectangle;
    PImage screenshot;

    public static void main(String[] args){
        PApplet.main(new String[]{/*ARGS_FULL_SCREEN, ARGS_DISPLAY + "=3",*/ ScreenCapture.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

    class Screencap implements Runnable {
        @Override
        public void run() {
            while(true) {
                robot.createScreenCapture(rectangle).getRGB(0, 0, screenshot.width, screenshot.height, screenshot.pixels, 0, screenshot.width);
                screenshot.updatePixels();
            }
        }
    }

    @Override
    public void setup() {

        size(1200, 800, OPENGL);
//        frame.setLocation(0,1200);

        frame.getMaximizedBounds();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        GraphicsDevice[] g = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        rectangle = g[1].getConfigurations()[0].getBounds();
//        rectangle.setSize(500, 500);
        screenshot = new PImage(rectangle.width, rectangle.height, ARGB);

        new Thread(new Screencap()).start();
    }

    @Override
    public final void draw() {

        image(screenshot, 0, 0);


        pushMatrix();
        {
            translate(width/2, height/2);
            rotateY(millis() / 1000f);
            texturedCube(screenshot, 100);
        }
        popMatrix();

        println(frameRate);

    }

    void texturedCube(PImage tex, float size) {
        beginShape(QUADS);
        textureMode(NORMAL);
        texture(tex);

        // http://processing.org/examples/texturecube.html
        // Given one texture and six faces, we can easily set up the uv coordinates
        // such that four of the faces tile "perfectly" along either u or v, but the other
        // two faces cannot be so aligned.  This code tiles "along" u, "around" the X/Z faces
        // and fudges the Y faces - the Y faces are arbitrarily aligned such that a
        // rotation along the X axis will put the "top" of either texture at the "top"
        // of the screen, but is not otherwised aligned with the X/Z faces. (This
        // just affects what type of symmetry is required if you need seamless
        // tiling all the way around the cube)

        // +Z "front" face
        vertex(-size, -size,  size, 0, 0);
        vertex( size, -size,  size, 1, 0);
        vertex( size,  size,  size, 1, 1);
        vertex(-size,  size,  size, 0, 1);

        // -Z "back" face
        vertex( size, -size, -size, 0, 0);
        vertex(-size, -size, -size, 1, 0);
        vertex(-size,  size, -size, 1, 1);
        vertex( size,  size, -size, 0, 1);

        // +Y "bottom" face
        vertex(-size,  size,  size, 0, 0);
        vertex( size,  size,  size, 1, 0);
        vertex( size,  size, -size, 1, 1);
        vertex(-size,  size, -size, 0, 1);

        // -Y "top" face
        vertex(-size, -size, -size, 0, 0);
        vertex( size, -size, -size, 1, 0);
        vertex( size, -size,  size, 1, 1);
        vertex(-size, -size,  size, 0, 1);

        // +X "right" face
        vertex( size, -size,  size, 0, 0);
        vertex( size, -size, -size, 1, 0);
        vertex( size,  size, -size, 1, 1);
        vertex( size,  size,  size, 0, 1);

        // -X "left" face
        vertex(-size, -size, -size, 0, 0);
        vertex(-size, -size,  size, 1, 0);
        vertex(-size,  size,  size, 1, 1);
        vertex(-size,  size, -size, 0, 1);

        endShape();
    }


}
