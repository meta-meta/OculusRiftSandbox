package com.generalprocessingunit.vr.demos;

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
                    PShape cube = createShape();
                    cube.beginShape(QUADS);
                    cube.fill(127);
                    cube.translate(x, y, z);
                    box(1f, 1f, 1f, cube);
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

    /** Ripped from PApplet.box */
    private void box(float w, float h, float d, PShape pS) {
        float x1 = -w/2f; float x2 = w/2f;
        float y1 = -h/2f; float y2 = h/2f;
        float z1 = -d/2f; float z2 = d/2f;

        // TODO not the least bit efficient, it even redraws lines
        // along the vertices. ugly ugly ugly!

        // front
        pS.normal(0, 0, 1);
        pS.vertex(x1, y1, z1);
        pS.vertex(x2, y1, z1);
        pS.vertex(x2, y2, z1);
        pS.vertex(x1, y2, z1);

        // right
        pS.normal(1, 0, 0);
        pS.vertex(x2, y1, z1);
        pS.vertex(x2, y1, z2);
        pS.vertex(x2, y2, z2);
        pS.vertex(x2, y2, z1);

        // back
        pS.normal(0, 0, -1);
        pS.vertex(x2, y1, z2);
        pS.vertex(x1, y1, z2);
        pS.vertex(x1, y2, z2);
        pS.vertex(x2, y2, z2);

        // left
        pS.normal(-1, 0, 0);
        pS.vertex(x1, y1, z2);
        pS.vertex(x1, y1, z1);
        pS.vertex(x1, y2, z1);
        pS.vertex(x1, y2, z2);

        // top
        pS.normal(0, 1, 0);
        pS.vertex(x1, y1, z2);
        pS.vertex(x2, y1, z2);
        pS.vertex(x2, y1, z1);
        pS.vertex(x1, y1, z1);

        // bottom
        pS.normal(0, -1, 0);
        pS.vertex(x1, y2, z1);
        pS.vertex(x2, y2, z1);
        pS.vertex(x2, y2, z2);
        pS.vertex(x1, y2, z2);

        pS.endShape();
    }
}
