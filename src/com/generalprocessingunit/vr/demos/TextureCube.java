package com.generalprocessingunit.vr.demos;

import com.generalprocessingunit.processing.*;
import com.generalprocessingunit.processing.space.AxisAngle;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.vr.PAppletVR;
import com.generalprocessingunit.vr.controls.HandSpatialized;
import com.generalprocessingunit.vr.controls.SpaceNavVR;
import com.google.common.base.Objects;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class TextureCube extends PAppletVR {
    HandSpatialized glove;

    SpaceNavVR spaceNav;

    EuclideanSpaceObject textureBox = new EuclideanSpaceObject(new PVector(0, 0, .3f), new Orientation());
    float boxSize = .15f;
    static int gridSize = 4;
    int numVoxels = 18;

    Set<TextureVoxel> textureVoxels = new HashSet<>();

    Set<Tuple> taken = new HashSet<>();

    public static void main(String[] args){
        PAppletVR.main(TextureCube.class);
    }


    @Override
    public void setup() {
        super.setup();

        spaceNav = new SpaceNavVR(this, .0007f, .03f);
        glove = new HandSpatialized(this);

        chooseNewVoxels();
    }


    void chooseNewVoxels() {
        textureBox.clearChildren();
        textureVoxels.clear();
        taken.clear();

        float voxelSize = boxSize / gridSize;

        
        for (int i = 0; i < numVoxels; i++) {
            TextureVoxel tv = new TextureVoxel(random(10) > 1 ? (int)random(1, 2) : (int)random(2, 11), voxelSize);
            textureBox.addChild(tv, randomVec(voxelSize));
            textureVoxels.add(tv);
        }
    }

    PVector randomVec(float voxelSize) {
        Tuple t = new Tuple(0, 0, 0);
        while(taken.contains(t)) {
            int a = (int)random(gridSize);
            int b = (int)random(gridSize);
            int c = (int)random(gridSize);
            t = new Tuple(a, b, c);
        }

        taken.add(t);
        return new PVector(t.a * voxelSize - boxSize / 2 + voxelSize / 2, t.b * voxelSize - boxSize / 2 + voxelSize / 2, t.c * voxelSize - boxSize / 2 + voxelSize / 2);
    }

    @Override
    protected void updateState() {
        spaceNav.update();
        glove.update();

        for(TextureVoxel tv : textureVoxels) {
            tv.update();
        }
        glove.leftHand.doVibrate();

        if(headContainer.getDistFrom(textureBox) < .3f) {
            springBack();
        }

        if(headContainer.x() > 2.2) {
            headContainer.setLocation(2.2f, headContainer.y(), headContainer.z());
//            spaceNav.momentum.add(-.001f, 0, 0);
        } else if (headContainer.x() < -2.2) {
            headContainer.setLocation(-2.2f, headContainer.y(), headContainer.z());
//            spaceNav.momentum.add(.001f, 0, 0);
        }

        if(headContainer.y() > 2.2) {
            headContainer.setLocation(headContainer.x(), 2.2f, headContainer.z());
//            spaceNav.momentum.add(0, -.001f, 0);
        } else if (headContainer.y() < -2.2) {
            headContainer.setLocation(headContainer.x(), -2.2f, headContainer.z());
//            spaceNav.momentum.add(0, .001f, 0);
        }

        if(headContainer.z() > 2.2) {
            headContainer.setLocation(headContainer.x(), headContainer.y(), 2.2f);
//            spaceNav.momentum.add(0, 0, -.001f);
        } else if (headContainer.z() < -2.2) {
            headContainer.setLocation(headContainer.x(), headContainer.y(), -2.2f);
//            spaceNav.momentum.add(0, 0, .001f);
        }
    }

    void springBack() {
        spaceNav.momentum.add(new PVector(0, 0, -.001f));
    }

    @Override
    protected void drawHeadContainerView(int eye, PGraphics pG) {}

    @Override
    protected void drawView(int eye, PGraphics pG) {
        background(0);

        Lighting.WhiteCube(pG, 50, 50, 10, 180, 180, 0);
        pG.emissive(0, 0, 0);

        glove.color.R = 64;
        glove.color.G = 64;
        glove.color.B = 48;
        glove.drawView(pG);

        for(TextureVoxel tv : textureVoxels) {
            tv.draw(pG);
        }

        pG.pushMatrix();
        {
            pG.translate(textureBox.x(), textureBox.y(), textureBox.z());
            AxisAngle aa = textureBox.getAxisAngle();
            pG.rotate(aa.w, aa.x, aa.y, aa.z);

            pG.fill(127, 64);
            pG.box(boxSize);
        }
        pG.popMatrix();


    }


    class TextureVoxel extends EuclideanSpaceObject {
        Set<HandSpatialized.VibratorShell> vibratorsThisIsTouching = new HashSet<>();
        int density;
        float size;
        private float halfWidth;

        TextureVoxel(int density, float size) {
            this.density = min(11, max(1, density));
            this.size = size;
            halfWidth = size / 2f;
        }

        void update() {
            for(HandSpatialized.VibratorShell vs : glove.vibratorShells) {
                if(collision(vs.getLocation(), vs.r)) {
                    if(!vibratorsThisIsTouching.contains(vs)) {
                        vs.setVibrate(density);
                        vibratorsThisIsTouching.add(vs);
                    }
                } else {
                    vibratorsThisIsTouching.remove(vs);
                }
            }
        }

        void draw(PGraphics pG) {
            pG.colorMode(RGB);
            pG.emissive(0);
            pG.pushMatrix();
            {
                pG.translate(x(), y(), z());
                AxisAngle aa = getAxisAngle();
                pG.rotate(aa.w, aa.x, aa.y, aa.z);

                if(vibratorsThisIsTouching.size() > 0) {
                    pG.fill(255, 0, 255 * (vibratorsThisIsTouching.size() / 14f));
                } else {
                    pG.colorMode(HSB);
                    pG.fill(180 - (density / 11f) * 180, 180, 255 );
                }
                pG.sphere(halfWidth);
//                pG.box(size);
            }
            pG.popMatrix();
        }
        
        boolean collision(PVector v, float r) {
            if(getDistFrom(v) > halfWidth + r) {
                return false;
            }

            //TODO: this only works if we don't rotate
            
//            if(x() - halfWidth > v.x + r) {
//                return false;
//            }
//
//            if(x() + halfWidth < v.x - r) {
//                return false;
//            }
//
//            if(y() - halfWidth > v.y + r) {
//                return false;
//            }
//
//            if(y() + halfWidth < v.y - r) {
//                return false;
//            }
//
//            if(z() - halfWidth > v.z + r) {
//                return false;
//            }
//
//            if(z() + halfWidth < v.z - r) {
//                return false;
//            }
            
            return true;
        }
    }

    class Tuple {
        public Integer a, b, c;
        Tuple(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public String toString() {
            return a + ", " + b + ", " + c;
        }

        @Override
        public int hashCode()
        {
            return Objects.hashCode(a, b, c);
        }


        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Tuple)){
                return false;
            }

            Tuple t = (Tuple)obj;
            return a == t.a && b == t.b && c == t.c;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_SPACE == e.getKeyCode()) {
            glove.reset();
            println("reset glove");

            recenterPose();
        }

        if(KeyEvent.VK_SHIFT == e.getKeyCode()) {
            chooseNewVoxels();
            println("new voxels");
        }

        if(KeyEvent.VK_UP == e.getKeyCode()) {
            gridSize = pow(gridSize - 1, 3) >= numVoxels ? gridSize - 1 : gridSize;
            println("gridSize: " + gridSize);
        }

        if(KeyEvent.VK_DOWN == e.getKeyCode()) {
            gridSize = min(100, gridSize + 1);
            println("gridSize: " + gridSize);
        }

        if(KeyEvent.VK_LEFT == e.getKeyCode()) {
            numVoxels = max(1, numVoxels - 1);
            println("numVoxels: " + numVoxels);
        }

        if(KeyEvent.VK_RIGHT == e.getKeyCode()) {
            numVoxels =  pow(gridSize, 3) >= numVoxels + 1 ? numVoxels + 1 : numVoxels;
            println("numVoxels: " + numVoxels);
        }

        if(KeyEvent.VK_G == e.getKeyCode()) {
            glove.invert();
            println("invert glove");
        }

        if(KeyEvent.VK_I == e.getKeyCode()) {
            spaceNav.invertControl();
            println("invert spacenav");
        }

        super.keyPressed(e);
    }
}
