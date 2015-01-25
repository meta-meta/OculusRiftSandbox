package com.generalprocessingunit.processing.demos.jBulletGloveString;


import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PVector;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class BallChain {
    public ESOjBullet head;
    public ESOjBullet tail;

    static final float ballSize = 1f;

    public List<ESOjBullet> balls = new ArrayList<>();

    public BallChain(DiscreteDynamicsWorld dynamicsWorld, int links, PVector location, boolean kinematicHead) {
        CollisionShape bead = new SphereShape(ballSize);

        ESOjBullet prevEso = null;
        for(int i = 0; i < links; ++i) {
            ESOjBullet currEso = new Ball(
                    dynamicsWorld, bead, i == 0 && kinematicHead ? 0 : .1f ,
                    PVector.add(location, new PVector(0, -(ballSize / ESOjBullet.scale) * i, 0)),
                    new Orientation()
            );

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
                        new Vector3f(0, -ballSize, 0),
                        new Vector3f(0, ballSize, 0)
                ), true);
            }

            prevEso = currEso;
        }

        head = balls.get(0);
        tail = balls.get(balls.size() - 1);
    }

}
