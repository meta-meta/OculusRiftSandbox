package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PGraphics;
import processing.core.PVector;

public class BodyBox extends ESOjBullet {
    public BodyBox(DiscreteDynamicsWorld dynamicsWorld, CollisionShape collisionShape, float mass, PVector location, Orientation orientation) {
        super(dynamicsWorld, collisionShape, mass, location, orientation);
    }

    @Override
    public void draw(PGraphics pG) {
        pushMatrixAndTransform(pG);
        {
            pG.box(.06f, .04f, .12f);
            // draw
        }
        pG.popMatrix();
    }
}
