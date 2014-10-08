package com.generalprocessingunit.processing.demos;

import com.generalprocessingunit.hid.SpaceNavigator;
import com.generalprocessingunit.processing.*;
import com.generalprocessingunit.processing.geometry.Cube;
import com.generalprocessingunit.processing.geometry.Dodecahedron;
import com.generalprocessingunit.processing.geometry.Pentagon;
import processing.core.*;

public class DodecahedronTree extends PAppletBuffered {

    public static void main(String args[]) {
        PApplet.main(new String[] { "--full-screen", "--display=0", DodecahedronTree.class.getCanonicalName() });
    }

    SpaceNavigator spaceNavigator;

    MomentumVector momentum = new MomentumVector(this, .001f);
    MomentumYawPitchRoll rotMomentum = new MomentumYawPitchRoll(this, .001f);

    EuclideanSpaceObject camera = new EuclideanSpaceObject();

    public void setup() {

        size(displayWidth, displayHeight, P3D);

        createCubes();
        createDodecarrangement();

        spaceNavigator = new SpaceNavigator(this);

        camera.setLocation(0, 0, -50);
        super.setup();
    }

    public void draw(PGraphics pG) {
        pG.blendMode(BLEND);
        pG.background(20, 20, 20, 180);

//        updateScene();

        PVector cam = camera.getLocation();
        PVector lookat = camera.getOrientation().zAxis();
        lookat.add(cam);
        PVector up = camera.getOrientation().yAxis();
        up.mult(-1);

        pG.camera(
                cam.x, cam.y, cam.z,
                lookat.x, lookat.y, lookat.z,
                up.x, up.y, up.z
        );


        pG.perspective(PI / 2.8f, width / height, 0.1f, 100f);

        pG.rotateY((millis() / 5000f) % TWO_PI);
        pG.scale(10);
        drawDodecahedron(1);
    }

    static int maxIters = 3;

    PShape[] cubes = new PShape[12];
    PShape dodecarrangement;

    void drawDodecahedron(int iter) {
        pG.blendMode(PConstants.ADD);
        if(iter < 3) {
            pG.pointLight(255, 255, 255, 0, 0, 0);
        }


//        pG.shape(dodecarrangement);

        pG.strokeWeight(.02f);
        pG.sphereDetail(6);

        int c = 0;
        for(Pentagon p : Dodecahedron.getPentagons()) {
            if(random(1) < .01f) {
                continue;
            }

            if(iter < 3){
                for(int i = 0; i < 12; i++) {
                    pG.pushMatrix();
                    {
                        PVector v = Dodecahedron.getVertex(i);
                        pG.translate(v.x, v.y, v.z);
//                        pG.fill(200, 20);
//                        pG.noStroke();
                        pG.noFill();
                        pG.stroke(0, 0, 255);
                        pG.sphere(.4f);
                    }
                    pG.popMatrix();
                }
            }

            pG.pushMatrix();
            {
                pG.translate(p.center.x, p.center.y, p.center.z);
                pG.rotate(.04f, random(.1f), random(.1f), random(.1f));
//                pG.strokeWeight(.02f);
//                pG.stroke(p.color.R, p.color.G, p.color.B);
                pG.noStroke();
                pG.fill(p.color.R / 2, p.color.G / 2, p.color.B / 2, 50);
//                pG.noFill();
//                pG.sphere(.3f);
//                pG.ellipse(.3f, .3f, .3f, .3f);

//                pG.shape(cubes[c]); //TODO: why is this slower than calling pG.box???
                pG.box(.4f);

                if(iter < maxIters){
                    pG.translate(p.center.x, p.center.y, p.center.z);
                    pG.scale(.43f);
                    drawDodecahedron(iter + 1);
                }
            }
            pG.popMatrix();
            c++;
        }
    }

    private void createDodecarrangement() {
        dodecarrangement = createShape(GROUP);
        for(Pentagon p : Dodecahedron.getPentagons()) {
            Color fill = Color.rgba(p.color.R / 2, p.color.G / 2, p.color.B / 2, 255);
            PShape cube = Cube.createCube(this, p.center.x, p.center.y, p.center.z, .4f, .4f, .4f, fill, null);
            dodecarrangement.addChild(cube);
        }
    }

    private void createCubes() {
        int c = 0;
        for(Pentagon p : Dodecahedron.getPentagons()) {
            Color fill = Color.rgba(p.color.R / 2, p.color.G/2, p.color.B / 2, 50);
            cubes[c] = Cube.createCube(this, 0, 0, 0, .4f, .4f, .4f, fill, null);
            c++;
        }
    }

    public void updateScene() {
        spaceNavigator.poll();

        PVector t = PVector.mult(spaceNavigator.translation, .1f);
        momentum.add(t);

        rotMomentum.add(PVector.mult(spaceNavigator.rotation, .1f));

        momentum.friction();
        rotMomentum.friction();

        camera.translateWRTObjectCoords(momentum.getValue());
        camera.rotate(rotMomentum.getValue());
    }
}