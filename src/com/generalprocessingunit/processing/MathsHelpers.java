package com.generalprocessingunit.processing;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public abstract class MathsHelpers implements PConstants {

    public static PVector add(PVector ... pVectors) {
        PVector v = new PVector();
        for(PVector pVector : pVectors) {
            v.add(pVector);
        }
        return v;
    }

    public static float abs(float n) {
        return PApplet.abs(n);
    }

    public static float abs(int n) {
        return PApplet.abs(n);
    }

    public static float sq(float n) {
        return PApplet.sq(n);
    }

    public static float sqrt(float n) {
        return PApplet.sqrt(n);
    }

    public static float log(float n) {
        return PApplet.log(n);
    }

    public static float exp(float n) {
        return PApplet.exp(n);
    }

    public static float pow(float n, float e) {
        return PApplet.pow(n, e);
    }

    public static float max(float a, float b) {
        return PApplet.max(a, b);
    }

    public static int max(int a, int b) {
        return PApplet.max(a, b);
    }

    public static int max(int[] ints) {
        return PApplet.max(ints);
    }

    public static float max(float[] floats) {
        return PApplet.max(floats);
    }

    public static float min(float a, float b) {
        return PApplet.min(a, b);
    }

    public static int min(int a, int b) {
        return PApplet.min(a, b);
    }

    public static int min(int[] ints) {
        return PApplet.min(ints);
    }

    public static float min(float[] floats) {
        return PApplet.min(floats);
    }

    public static int constrain(int amt, int low, int high) {
        return PApplet.constrain(amt, low, high);
    }

    public static float constrain(float amt, float low, float high) {
        return PApplet.constrain(amt, low, high);
    }

    public static float sin(float n) {
        return PApplet.sin(n);
    }

    public static float cos(float n) {
        return PApplet.cos(n);
    }

    public static float tan(float n) {
        return PApplet.tan(n);
    }

    public static float asin(float n) {
        return PApplet.asin(n);
    }

    public static float acos(float n) {
        return PApplet.acos(n);
    }

    public static float atan(float n) {
        return PApplet.atan(n);
    }

    public static float atan2(float y, float x) {
        return PApplet.atan2(y, x);
    }

    public static float degrees(float radians) {
        return PApplet.degrees(radians);
    }

    public static float radians(float degrees) {
        return PApplet.radians(degrees);
    }

    //TODO: ceil on

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return PApplet.map(value, start1, stop1, start2, stop2);
    }

}
