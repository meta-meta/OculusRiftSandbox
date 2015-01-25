package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Transform;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class marionetteRig {
    DiscreteDynamicsWorld dynamicsWorld;
    static final float gravity = -15f;

    public Set<ESOjBullet> rigObjects = new HashSet<>();

    public marionetteRig() {

        // collision configuration contains default setup for memory, collision
        // setup. Advanced users can create their own configuration.
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();

        // use the default collision dispatcher. For parallel processing you
        // can use a diffent dispatcher (see Extras/BulletMultiThreaded)
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        // the maximum size of the collision world. Make sure objects stay
        // within these boundaries
        // Don't make the world AABB size too large, it will harm simulation
        // quality and performance
        Vector3f worldAabbMin = new Vector3f(-1000, -1000, -1000);
        Vector3f worldAabbMax = new Vector3f(1000, 1000, 1000);
        int maxProxies = 1024;
        AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
        //BroadphaseInterface overlappingPairCache = new SimpleBroadphase(
        //		maxProxies);

        // the default constraint solver. For parallel processing you can use a
        // different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);


        //Floor
        CollisionShape anchorC = new BoxShape(new Vector3f(300f, 10f, 300f));
        ESOjBullet anchor = new ESOjBullet(dynamicsWorld, anchorC, 0, new PVector(0, -.51f, 0), new Orientation()) {
            @Override
            public void draw(PGraphics pG) {
                pushMatrixAndTransform(pG);
                {
                    pG.fill(255, 0, 0);
                    pG.box(6f, .2f, 6f);
                }
                pG.popMatrix();
            }
        };
        anchor.body.setFriction(.1f);
        rigObjects.add(anchor);

        CollisionShape floorBoxC = new BoxShape(new Vector3f(150f, 10f, 150f));
        ESOjBullet floor = new ESOjBullet(dynamicsWorld, floorBoxC, 2f, new PVector(0, -.4f, 0), new Orientation()) {
            @Override
            public void draw(PGraphics pG) {
                pushMatrixAndTransform(pG);
                {
                    pG.fill(100, 200, 255);
                    pG.box(3f, .2f, 3f);
                }
                pG.popMatrix();
            }
        };
        floor.body.setFriction(.9f);
        floor.body.setRestitution(0);
        rigObjects.add(floor);

        Transform t1 = new Transform();
        t1.setIdentity();
        t1.origin.set(0, 10.1f, 0);

        Transform t2 = new Transform();
        t2.setIdentity();
        t2.origin.set(0, -10, 0);

        Generic6DofConstraint constr = new Generic6DofConstraint(anchor.body, floor.body, t1, t2, false);
        constr.setLinearLowerLimit(new Vector3f(-100, 0, -100));
        constr.setLinearUpperLimit(new Vector3f(100, 0, 100));
        constr.setAngularLowerLimit(new Vector3f());
        constr.setAngularUpperLimit(new Vector3f());
        dynamicsWorld.addConstraint(constr, false);
    }

    List<BallChain> chains = new ArrayList<>();

    public List<BallChain> spawnMarionette(List<PVector> locs, PVector hydraLoc) {

        int i = 0;
        for(PVector loc: locs) {
            BallChain chain = new BallChain(dynamicsWorld, i==0 ? 6 : (i < 2 ? 17: 25), .6f, PVector.add(loc, hydraLoc), true){
                @Override
                public void draw(PGraphics pG) {
                    pG.fill(255, 127);
                    pG.sphere(.6f/ ESOjBullet.scale);
                }
            };
            chains.add(chain);
            rigObjects.addAll(chain.balls);
            ++i;
        }

        dynamicsWorld.setGravity(new Vector3f(0, gravity, 0));

        return chains;
    }

    void addP2PConstraint(ESOjBullet a, ESOjBullet b, float xA, float yA, float zA, float xB, float yB, float zB) {
        Point2PointConstraint p2p = new Point2PointConstraint(
                a.body,
                b.body,
                new Vector3f(xA * ESOjBullet.scale, yA * ESOjBullet.scale, zA * ESOjBullet.scale),
                new Vector3f(xB * ESOjBullet.scale, yB * ESOjBullet.scale, zB * ESOjBullet.scale));
        dynamicsWorld.addConstraint(p2p, true);
    }

    public void attachModelToChains(PVector hydraLoc) {

        PVector center = PVector.add(hydraLoc, new PVector(0, -.2f, 0f));


        // Body
        CollisionShape bodyBoxC = new BoxShape(new Vector3f(3f, 2f, 3f));
        ESOjBullet bodyBox = new ESOjBullet(dynamicsWorld, bodyBoxC, 2f, center, new Orientation()){
            @Override
            public void draw(PGraphics pG) {
                pushMatrixAndTransform(pG);
                {
                    pG.fill(100, 255, 200, 150);
                    pG.box(.06f, .04f, .06f);
                }
                pG.popMatrix();
            }
        };
        rigObjects.add(bodyBox);
        addP2PConstraint(chains.get(1).tail, bodyBox, 0, 0, 0, 0, .02f, .01f);


        // Head
        CollisionShape sphereColl = new SphereShape(3f);
        ESOjBullet head = new ESOjBullet(dynamicsWorld, sphereColl, 4, center, new Orientation()) {
            @Override
            public void draw(PGraphics pG) {
                pushMatrixAndTransform(pG);
                {
                    pG.sphere(.03f);
                }
                pG.popMatrix();
            }
        };
        rigObjects.add(head);
        addP2PConstraint(chains.get(0).tail, head, 0, -.015f, 0, 0, .03f, 0);


        // Neck
        BallChain neck = new BallChain(dynamicsWorld, 7, 1.4f, center, false){
            @Override
            public void draw(PGraphics pG) {
                pG.fill(50, 100, 200);
                pG.sphere(1.4f / ESOjBullet.scale);
            }
        };
        rigObjects.addAll(neck.balls);
        addP2PConstraint(head, neck.head, 0, 0, .03f, 0, .01f, 0);
        addP2PConstraint(neck.tail, bodyBox, 0, 0, 0, 0, 0, -.06f);


        CollisionShape footBoxC = new BoxShape(new Vector3f(1.5f, 1f, 3f));

        //Legs
        for (int i = 0; i < 2; i++) {
            int neg = i == 0 ? -1 : 1;

            PVector leftOfCenter = PVector.add(center, new PVector(0, 0, .03f * neg));
            BallChain leg = new BallChain(dynamicsWorld, 7, 1f, leftOfCenter, false){
                @Override
                public void draw(PGraphics pG) {
                    pG.fill(50, 150, 200);
                    pG.sphere(1f / ESOjBullet.scale);
                }
            };
            rigObjects.addAll(leg.balls);
            addP2PConstraint(leg.head, bodyBox, 0, 0, 0, -.03f * neg, 0, .03f);

            ESOjBullet foot = new ESOjBullet(dynamicsWorld, footBoxC, 5f, leftOfCenter, new Orientation()){
                @Override
                public void draw(PGraphics pG) {
                    pushMatrixAndTransform(pG);
                    {
                        pG.fill(200, 30, 0);
                        pG.box(.03f, .02f, .06f);
                    }
                    pG.popMatrix();
                }
            };
            foot.body.setFriction(.9f);
            foot.body.setRestitution(0f);
            rigObjects.add(foot);
            addP2PConstraint(leg.tail, foot, 0, 0, 0, neg * .015f, .01f, .03f);

            addP2PConstraint(chains.get(i == 0 ? 3 : 2).tail, foot, 0, 0, 0, 0, .01f, .01f);
        }


        // Boxes
        CollisionShape kickBox = new BoxShape(new Vector3f(2f, 4f, 3f));
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                ESOjBullet aBall = new ESOjBullet(dynamicsWorld, kickBox, 3f,
                        new PVector(x  * .1f - .2f, 0, y * .1f - .2f), new Orientation()){
                    @Override
                    public void draw(PGraphics pG) {
                        pushMatrixAndTransform(pG);
                        {
                            pG.fill(255, 100, 150);
                            pG.box(.04f, .08f, .015f);
                            // draw
                        }
                        pG.popMatrix();
                    }
                };
                aBall.body.setDamping(.1f, .1f);
                aBall.body.setFriction(.6f);
                rigObjects.add(aBall);
            }
        }

    }

    int prevMillis = 0;
    public void draw(PGraphics pG, PApplet p5) {
        int currMillis = p5.millis();
        dynamicsWorld.stepSimulation((currMillis - prevMillis) / 500f, 10);
        System.out.println("grav:" + dynamicsWorld.getGravity(new Vector3f()));
        prevMillis = currMillis;
        for(ESOjBullet eso: rigObjects) {
            eso.update();
            eso.draw(pG);
        }

    }
}
