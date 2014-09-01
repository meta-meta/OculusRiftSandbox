package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.entities.Orbitable;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

public class TvNoise extends PAppletVR {

    public static void main(String[] args){
        PAppletVR.main(TvNoise.class);
    }

    List<Square> squares = new ArrayList<>();

    Orbitable orbitable;

    @Override
    public void setup() {
        super.setup();

        for(int i = 0; i < 16; i++) {
            squares.add(new Square());
        }

        orbitable = new Orbitable(.2f, 0, 1, 10, 10, 10, 1, ADD);

    }


    @Override
    protected void updateState() {
        for(Square square: squares){
            square.updateState();
        }
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) { }




    @Override
    protected void drawView(int eye, PGraphics pG) {


        pG.stroke(50);
        pG.noFill();



        pG.translate(0, 1, 3);
        pG.box(2, 2, 2);

        orbitable.draw(pG, this, eye);
        pG.blendMode(BLEND);
    }

    protected void drawViewPreCamera(int eye, PGraphics pG){
        pG.camera();
        pG.perspective();

        pG.hint(DISABLE_DEPTH_MASK);
        pG.fill(0, 50);
        pG.rect(0, 0, pG.width, pG.height);
        pG.hint(ENABLE_DEPTH_MASK);

        drawRandom(pG);
        pG.tint(255, 128);
        for(Square square: squares){
            pG.image(square.square, square.sqX + (eye == 0 ? 0 : 150 * sin(square.speed + millis() / 1000f)), square.sqY);
        }
    }

    private void drawRandom(PGraphics pG) {
        int size = 6;
        pG.colorMode(HSB);
        for(int x = 0; x < pG.width; x+= size + size * (int)random(3)){
            for(int y = 0; y < pG.height; y+= size + size * (int)random(7)) {
                if(random(20) > 17){
                    pG.stroke(random(220), 255, random(30, 40), 200);
                    pG.noFill();
                    pG.rect(x, y, size*2, size*2);
                } else {
                    pG.fill(random(220), random(128), random(30, 40), 120);
                    pG.noStroke();
                    pG.rect(x, y, size, size);
                }
            }
        }
    }


    class Square {
        float speed = random(1, 10);
        float sqX, sqY;
        int millisAtLastFrame;
        boolean xNeg, yNeg;
        PGraphics square;

        void updateState() {

            square = createGraphics(100, 100);
            square.beginDraw();
            drawRandom(square);
            square.endDraw();

            int m = millis() - millisAtLastFrame;
            millisAtLastFrame = millis();

            speed = random(1, 10);
            sqX += (m / 10) * (xNeg ? -1 : 1) * speed;
            sqY += (m / 10) * (yNeg ? -1 : 1) * speed;

            float lX = eyeTextureW * .6f - 200;
            float hX = eyeTextureW * .6f + 100;
            float lY = eyeTextureH * .6f - 300;
            float hY = eyeTextureH * .6f + 200;
            if(sqX < lX || sqX > hX ) {
                xNeg = !xNeg;
                sqX = constrain(sqX, lX, hX);
            }
            if(sqY < lY || sqY > hY ) {
                yNeg = !yNeg;
                sqY = constrain(sqY, lY, hY);
            }
        }
    }
}
