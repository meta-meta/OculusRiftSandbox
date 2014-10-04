package com.generalprocessingunit.processing.geometry;

import com.generalprocessingunit.processing.Color;
import com.google.common.collect.ImmutableList;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.Arrays;
import java.util.List;

public class Dodecahedron {
    private static final PVector[] vertices = new PVector[]{
            // coordinates of all the dodecahedron vertices
            new PVector(0.607f , 0.000f , 0.795f ),
            new PVector(0.188f , 0.577f , 0.795f ),
            new PVector(-0.491f , 0.357f , 0.795f ),
            new PVector(-0.491f , -0.357f , 0.795f ),
            new PVector(0.188f , -0.577f , 0.795f ),
            new PVector(0.982f , 0.000f , 0.188f ),
            new PVector(0.304f , 0.934f , 0.188f ),
            new PVector(-0.795f , 0.577f , 0.188f ),
            new PVector(-0.795f , -0.577f , 0.188f ),
            new PVector(0.304f , -0.934f , 0.188f ),
            new PVector(0.795f , 0.577f , -0.188f ),
            new PVector(-0.304f , 0.934f , -0.188f ),
            new PVector(-0.982f , 0.000f , -0.188f ),
            new PVector(-0.304f , -0.934f , -0.188f ),
            new PVector(0.795f , -0.577f , -0.188f ),
            new PVector(0.491f , 0.357f , -0.795f ),
            new PVector(-0.188f , 0.577f , -0.795f ),
            new PVector(-0.607f , 0.000f , -0.795f ),
            new PVector(-0.188f , -0.577f , -0.795f ),
            new PVector(0.491f , -0.357f , -0.795f )
    };

    //colors of the panels
    private static final Color[] colors = new Color[]{
            new Color(0xFF0000),
            new Color(0xff6c00),
            new Color(0xffe400),
            new Color(0x80ff00),
            new Color(0x06c229),
            new Color(0x00fea7),
            new Color(0x00ffff),
            new Color(0x008eff),
            new Color(0x0016ff),
            new Color(0x8000ff),
            new Color(0xf177ff),
            new Color(0xd20159)};

    private static final List<Pentagon> pentagons = ImmutableList.of(
            new Pentagon(new int[] { 0, 1, 2, 3, 4 }, vertices, colors[0]),
            new Pentagon(new int[] { 0, 1, 6, 10, 5 }, vertices, colors[1]),
            new Pentagon(new int[] { 1, 2, 7, 11, 6 }, vertices, colors[2]),
            new Pentagon(new int[] { 2, 3, 8, 12, 7 }, vertices, colors[3]),
            new Pentagon(new int[] { 3, 4, 9, 13, 8 }, vertices, colors[4]),
            new Pentagon(new int[] { 4, 0, 5, 14, 9 }, vertices, colors[5]),
            new Pentagon(new int[] { 15, 16, 17, 18, 19 }, vertices, colors[6]),
            new Pentagon(new int[] { 18, 19, 14, 9, 13 }, vertices, colors[7]),
            new Pentagon(new int[] { 17, 18, 13, 8, 12 }, vertices, colors[8]),
            new Pentagon(new int[] { 16, 17, 12, 7, 11 }, vertices, colors[9]),
            new Pentagon(new int[] { 15, 16, 11, 6, 10 }, vertices, colors[10]),
            new Pentagon(new int[] { 19, 15, 10, 5, 14 }, vertices, colors[11])
    );


    public static List<Pentagon> getPentagons() {
        return pentagons;
    }

    public static PVector getVertex(int i) {
        return vertices[i];
    }
}
