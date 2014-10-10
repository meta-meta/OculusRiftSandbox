package com.generalprocessingunit.processing.geometry;

import com.generalprocessingunit.processing.Color;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public class Cube implements PConstants {
    public static PShape createCube(PApplet p5, float x, float y, float z, float w, float h, float d, Color fill, Color stroke) {
        PShape cube = p5.createShape();
        cube.beginShape(QUADS);

        if(null == fill) {
            cube.noFill();
        } else {
            cube.fill(fill.R, fill.G, fill.B, fill.A);
        }

        if(null == stroke) {
            cube.noStroke();
        } else {
            cube.stroke(stroke.R, stroke.G, stroke.B, stroke.A);
        }

        cube.translate(x, y, z);
        box(w, h, d, cube);
        return cube;
    }

    /** Ripped from PApplet.box */
    private static void box(float w, float h, float d, PShape pS) {
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

