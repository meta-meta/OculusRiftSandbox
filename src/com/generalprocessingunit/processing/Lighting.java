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

    public static void WhiteCube(PGraphics pG, float posXB, float negXB, float posYB, float negYB, float posZB, float negZB) {
        WhiteCube(pG, posXB, 0, 0, negXB, 0, 0, posYB, 0, 0, negYB, 0, 0, posZB, 0, 0, negZB, 0, 0);

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, posXB);
            pG.translate(-2.5f, 0, 0);
            pG.box(.01f, 5, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, negXB);
            pG.translate(2.5f, 0, 0);
            pG.box(.01f, 5, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, posYB);
            pG.translate(0, -2.5f, 0);
            pG.box(5, .01f, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, negYB);
            pG.translate(0, 2.5f, 0);
            pG.box(5, .01f, 5);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, posZB);
            pG.translate(0, 0, -2.5f);
            pG.box(5, 5, .01f);
        }
        pG.popMatrix();

        pG.pushMatrix();
        {
            pG.colorMode(RGB);
            pG.fill(255);
            pG.colorMode(HSB);

            pG.emissive(0, 0, negZB);
            pG.translate(0, 0, 2.5f);
            pG.box(5, 5, .01f);
        }
        pG.popMatrix();

        pG.emissive(0);
    }

    public static void WhiteCube(PGraphics pG) {
        WhiteCube(pG, 255,255, 255, 255, 255, 255);
    }

}
