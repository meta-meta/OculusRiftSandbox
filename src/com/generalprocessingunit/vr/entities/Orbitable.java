package com.generalprocessingunit.vr.entities;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

public class Orbitable
{
    float size;
    float phase;
    float orbitalRadius;
    float r, g, b;
    int rotAxis;
    List<Orbitable> satellites = new ArrayList();
    int blendMode;
    static int total = 0;
    static final int MAX = 1000;

    public Orbitable(float size, float phase, float orbitalRadius, float r, float g, float b, int rotAxis, int blendMode){
        this.size = size;
        this.phase = phase;
        this.orbitalRadius = orbitalRadius;
        this.r = r;
        this.g = g;
        this.b = b;
        this.rotAxis = rotAxis;
        this.blendMode = blendMode == PConstants.DARKEST ? PConstants.ADD : PConstants.DARKEST;
        total++;
    }


    public void draw(PGraphics pG, PApplet p5, int eye){
        pG.blendMode( blendMode  );

        if(eye == 0){
            spawn(p5);
        }

        pG.pushMatrix();

        pG.fill(r, g, b, 120);
        pG.noStroke();

//        pG.sphere(size);
        pG.pushMatrix();
        pG.rotateY(p5.millis()/ 10000f * (blendMode == PConstants.ADD ? 1 : -1));
        pG.ellipse(0,0,size,size);
        pG.popMatrix();


        for(Orbitable satellite : satellites){
            float direction1 = satellite.orbitalRadius * PApplet.sin((PApplet.TWO_PI + satellite.phase) * (p5.millis()/(10000f * satellite.orbitalRadius)));
            float direction2 = satellite.orbitalRadius * PApplet.cos((PApplet.TWO_PI + satellite.phase) * (p5.millis()/(10000f * satellite.orbitalRadius)));

            pG.translate(
                    rotAxis == 0 ? direction1 : rotAxis == 1 ? direction2 : 0,
                    rotAxis == 0 ? 0 : rotAxis == 1 ? direction1 : direction2,
                    rotAxis == 0 ? direction2 : rotAxis == 1 ? 0 : direction1);

            satellite.draw(pG, p5, eye);
        }

        pG.popMatrix();
    }

    void spawn(PApplet p5){
        boolean chance = total < MAX && p5.random(500) < 2 ;
        if( chance && p5.frameRate > 15){
            float satelliteSize = p5.random(size / 4, size * 0.7f);
            satellites.add(new Orbitable(satelliteSize, p5.random(PConstants.TWO_PI), p5.random(size + satelliteSize, (satelliteSize/10) * (size + satelliteSize) ), p5.random(r-50, r+50), p5.random(g-50, g+50), p5.random(b-50, b+50), PApplet.round(p5.random(2)), blendMode ));
        }
    }
}
