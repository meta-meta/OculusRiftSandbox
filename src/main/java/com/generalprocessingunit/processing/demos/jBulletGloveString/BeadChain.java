package com.generalprocessingunit.processing.demos.jBulletGloveString;


import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexInternalShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PGraphics;
import processing.core.PVector;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public abstract class BeadChain {
    public ESOjBullet head;
    public ESOjBullet tail;


    public List<ESOjBullet> balls = new ArrayList<>();

    public BeadChain(DiscreteDynamicsWorld dynamicsWorld, CollisionShape bead, int links, final float ballSize, PVector location, boolean kinematicHead) {

        final BeadChain self = this;

        ESOjBullet prevEso = null;
        for(int i = 0; i < links; ++i) {
            float ballMass = 1f;
            ESOjBullet currEso = new ESOjBullet(
                    dynamicsWorld, bead, i == 0 && kinematicHead ? 0 : ballMass,
                    PVector.add(location, new PVector(0, -(ballSize / ESOjBullet.scale) * i, 0)),
                    new Orientation()
            ) {
                @Override
                public void draw(PGraphics pG) {
                    pushMatrixAndTransform(pG);
                    {
                        self.draw(pG);
                    }
                    pG.popMatrix();
                }
            };

            // set the head ball as kinematic
            if(i == 0 && kinematicHead) {
                currEso.body.setCollisionFlags(currEso.body.getCollisionFlags() | CollisionFlags.KINEMATIC_OBJECT);
                currEso.body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
            }

            balls.add(currEso);

            // add constraints
            if(i > 0) {
                dynamicsWorld.addConstraint(new Point2PointConstraint(
                        prevEso.body,
                        currEso.body,
                        new Vector3f(0, -ballSize * 1.3f, 0),
                        new Vector3f(0, ballSize * 1.3f, 0)
                ), true);
            }

            prevEso = currEso;
        }

        head = balls.get(0);
        tail = balls.get(balls.size() - 1);
    }

    public abstract void draw(PGraphics pG);

}
