package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PGraphics;
import processing.core.PVector;

public class BigBall extends ESOjBullet {
    public BigBall(DiscreteDynamicsWorld dynamicsWorld, CollisionShape collisionShape, float mass, PVector location, Orientation orientation) {
        super(dynamicsWorld, collisionShape, mass, location, orientation);
    }

    @Override
    public void draw(PGraphics pG) {
        pushMatrixAndTransform(pG);
        {
            pG.sphere(.03f);
            // draw
        }
        pG.popMatrix();
    }
}
