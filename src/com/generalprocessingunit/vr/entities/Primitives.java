package com.generalprocessingunit.vr.entities;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Primitives {

    public static final String CYLINDER_TOP = "cylinder_top";
    public static final String CYLINDER_BOT = "cylinder_bot";
    public static final String CYLINDER_MID = "cylinder_mid";
    public static PShape cylinder(PApplet p5, float radius, float height, int sides){

        // TODO break this into method
        float[] x = new float[sides + 1];
        float[] z = new float[sides + 1];
        float step = PApplet.TWO_PI / sides;
        for(int i = 0; i <= sides; i++){
            x[i] = PApplet.sin(i * step) * radius;
            z[i] = PApplet.cos(i * step) * radius;
        }

        float y = height/2;

        PShape cyl = p5.createShape(PConstants.GROUP);

        PShape top = circle(p5, x, z, sides);
        top.setName(CYLINDER_TOP);
        top.translate(0, y, 0);
        cyl.addChild(top);

        PShape bot = circle(p5, x, z, sides);
        bot.setName(CYLINDER_BOT);
        bot.translate(0, -y, 0);
        cyl.addChild(bot);

        PShape mid = p5.createShape();
        mid.setName(CYLINDER_MID);
        mid.beginShape(PConstants.QUAD_STRIP);
        for(int i = 0; i <= sides; i++){
            mid.vertex(x[i], -y, z[i]);
            mid.vertex(x[i], y, z[i]);
        }
        mid.endShape();
        cyl.addChild(mid);

        return cyl;
    }

    public static PShape circle(PApplet p5, float radius, int sides){

        // TODO break this into method
        float[] x = new float[sides + 1];
        float[] z = new float[sides + 1];
        float step = PApplet.TWO_PI / sides;
        for(int i = 0; i <= sides; i++){
            x[i] = PApplet.sin(i * step) * radius;
            z[i] = PApplet.cos(i * step) * radius;
        }

        return circle(p5, x, z, sides);
    }

    private static PShape circle(PApplet p5, float[] x, float[] z, int sides){
        PShape cir = p5.createShape();
        cir.beginShape(PConstants.TRIANGLE_FAN);
        cir.stroke(255);

        cir.vertex(0, 0, 0);
        for(int i = 0; i <= sides; i++){
            cir.vertex(x[i], 0, z[i]);
        }
        cir.endShape();
        return cir;
    }

    public static PShape arc(PApplet p5, float innerRadius, float outerRadius, float length, int sides) {
        float myLength = PApplet.min(PConstants.TWO_PI, PApplet.max(0, length));
        float step = myLength / sides;
        float offset = -myLength / 2;

        PShape arc = p5.createShape();
        arc.beginShape(PConstants.QUAD_STRIP);

        for (int i = 0; i <= sides; i++) {
            float x = PApplet.sin(offset + i * step);
            float z = PApplet.cos(offset + i * step);
            arc.vertex(x * innerRadius, 0, z * innerRadius);
            arc.vertex(x * outerRadius, 0, z * outerRadius);
        }

        arc.endShape();
        return arc;
    }

}
