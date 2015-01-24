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

    public List<ESOjBullet> balls = new ArrayList<>();

    public BallChain(DiscreteDynamicsWorld dynamicsWorld, int links, PVector location) {
        CollisionShape bead = new SphereShape(2f);

        ESOjBullet prevEso = null;
        for(int i =0; i < links; ++i) {
            ESOjBullet currEso = new Ball(dynamicsWorld, bead, i == 0 ? 0 : 1f - i/50f, PVector.add(location, new PVector(0, -2f * i, 0)), new Orientation());
            if(i == 0) {
                currEso.body.setCollisionFlags(currEso.body.getCollisionFlags() | CollisionFlags.KINEMATIC_OBJECT);
                currEso.body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
            }

            balls.add(currEso);

            if(i > 0) {
                //add constraint
                Point2PointConstraint p2p = new Point2PointConstraint(
                        prevEso.body, currEso.body, new Vector3f(0, -1f, 0), new Vector3f(0, 1f, 0) );
//                p2p.setting.impulseClamp = 3f;
                p2p.setting.damping = .8f;

                dynamicsWorld.addConstraint(p2p, true);
            }

            prevEso = currEso;
        }

        head = balls.get(0);
        tail = balls.get(balls.size() - 1);
    }

}
