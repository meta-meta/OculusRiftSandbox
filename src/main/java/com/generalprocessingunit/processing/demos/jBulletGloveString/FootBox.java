package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PGraphics;
import processing.core.PVector;

public class FootBox extends ESOjBullet {
    public FootBox(DiscreteDynamicsWorld dynamicsWorld, CollisionShape collisionShape, float mass, PVector location, Orientation orientation) {
        super(dynamicsWorld, collisionShape, mass, location, orientation);
    }

    @Override
    public void draw(PGraphics pG) {
        pushMatrixAndTransform(pG);
        {
            pG.box(.03f, .02f, .06f);
            // draw
        }
        pG.popMatrix();
    }
}
