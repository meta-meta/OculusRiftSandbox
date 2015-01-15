package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.Color;
import com.generalprocessingunit.processing.geometry.Cube;
import com.generalprocessingunit.vr.PAppletVR;
import processing.core.PGraphics;
import processing.core.PShape;


public class Example extends PAppletVR {

    public static void main(String[] args){
        PAppletVR.main(Example.class);
    }

    PShape cubes;
    PShape floor;

    @Override
    public void setup() {
        super.setup();

        headContainer.setLocation(0, 2, -1);

        cubes = createShape(GROUP);
        for(int y = 0; y < 100; y+= 10) {
            for (int x = -200; x < 200; x += 10) {
                for (int z = -200; z < 200; z += 10) {
                    PShape cube = Cube.createCube(this, x, y, z, 1f, 1f, 1f, Color.grey(127), null);
                    cubes.addChild(cube);
                }
            }
        }

        floor = createShape();
        floor.beginShape(QUADS);

        floor.fill(20);
        floor.vertex(-1000,0, -1000);
        floor.vertex(-1000,0, 1000);
        floor.vertex(1000,0, 1000);
        floor.vertex(1000,0, -1000);

        floor.endShape();
    }

    @Override
    protected void updateState() { }

    float hue = 0;

    @Override
    protected void drawView(int eye, PGraphics pG) {
        hue += millis() % 2000 < 200 ? 1f : .2f ;
        hue %= 255;

        pG.colorMode(HSB);
        pG.background(hue, 255, 40);

        pG.directionalLight(hue, 200, 200, 0, -1f, 0);
        pG.directionalLight((hue + 60) % 255, 127, 60, 1f, -1f, 0);
        pG.directionalLight((hue + 120) % 255, 127, 60, 1f, -1f, 1);

        pG.shape(floor);
        pG.shape(cubes);
    }
}
