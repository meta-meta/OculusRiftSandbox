package com.generalprocessingunit.processing.geometry;

import com.generalprocessingunit.processing.Color;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Pentagon {
    public final PVector[] vertices = new PVector[5];
    public PVector center;

    public final Color color;

    Pentagon(int[] dodecahedronVertexIndexes, PVector[] dodecahedronVertices, Color color) {
        float x = 0;
        float y = 0;
        float z = 0;

        for (int i = 0; i < 5; i++) {
            vertices[i] = dodecahedronVertices[dodecahedronVertexIndexes[i]];
            x += vertices[i].x;
            y += vertices[i].y;
            z += vertices[i].z;
        }

        center = new PVector(x / 5, y / 5, z / 5);

        this.color = color;
    }
}
