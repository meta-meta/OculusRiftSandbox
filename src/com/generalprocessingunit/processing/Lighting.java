package com.generalprocessingunit.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Lighting implements PConstants {

    static float hue;
    public static void SpecialRGBLightCycle(PGraphics pG, PApplet p5) {
        hue += p5.millis() % 2000 < 200 ? 1f : .2f ;
        hue %= 255;

        pG.colorMode(HSB);

        pG.directionalLight(hue, 200, 200, 0, -1f, 0);
        pG.directionalLight((hue + 60) % 255, 127, 60, 1f, -1f, 0);
        pG.directionalLight((hue + 120) % 255, 127, 60, 1f, -1f, 1);
    }

    public static void SpecialRGBLightStatic(PGraphics pG) {
        pG.colorMode(HSB);

        pG.directionalLight(0, 200, 200, 0, -1f, 0);
        pG.directionalLight(60, 127, 60, 1f, -1f, 0);
        pG.directionalLight(120, 127, 60, 1f, -1f, 1);
    }

    public static void WhiteCube(PGraphics pG,
                                 float posXB, float posXS, float posXH,
                                 float negXB, float negXS, float negXH,
                                 float posYB, float posYS, float posYH,
                                 float negYB, float negYS, float negYH,
                                 float posZB, float posZS, float posZH,
                                 float negZB, float negZS, float negZH
    ) {
        pG.colorMode(HSB);

        pG.directionalLight(posXH, posXS, posXB,  1,  0,  0);
        pG.directionalLight(negXH, negXS, negXB, -1,  0,  0);
        pG.directionalLight(posYH, posYS, posYB,  0,  1,  0);
        pG.directionalLight(negYH, negYS, negYB,  0, -1,  0);
        pG.directionalLight(posZH, posZS, posZB,  0,  0,  1);
        pG.directionalLight(negZH, negZS, negZB,  0,  0, -1);
    }
}
