package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
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
        CollisionShape floorBoxC = new BoxShape(new Vector3f(500f, 10f, 500f));
        ESOjBullet floor = new FloorBox(dynamicsWorld, floorBoxC, 0, new PVector(0, -.5f, 0), new Orientation());
        floor.body.setFriction(.9f);
        rigObjects.add(floor);
    }

    List<BallChain> chains = new ArrayList<>();

    public List<BallChain> spawnMarionette(List<PVector> locs, PVector hydraLoc) {

        int i = 0;
        for(PVector loc: locs) {
            BallChain chain = new BallChain(dynamicsWorld, i==0? 4 : (i < 2 ? 12: 16), PVector.add(loc, hydraLoc), true);
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
        CollisionShape bodyBoxC = new BoxShape(new Vector3f(3f, 2f, 6f));
        ESOjBullet bodyBox = new BodyBox(dynamicsWorld, bodyBoxC, 2f, center, new Orientation());
        rigObjects.add(bodyBox);
        addP2PConstraint(chains.get(1).tail, bodyBox, 0, 0, 0, 0, 0, .06f);


        // Head
        CollisionShape sphereColl = new SphereShape(3f);
        BigBall head = new BigBall(dynamicsWorld, sphereColl, 2, center, new Orientation());
        rigObjects.add(head);
        addP2PConstraint(chains.get(0).tail, head, 0, -.015f, 0, 0, .03f, 0);


        // Neck
        BallChain neck = new BallChain(dynamicsWorld, 4, center, false);
        rigObjects.addAll(neck.balls);
        addP2PConstraint(head, neck.head, 0, 0, .03f, 0, .01f, 0);
        addP2PConstraint(neck.tail, bodyBox, 0, 0, 0, 0, 0, -.06f);


        CollisionShape footBoxC = new BoxShape(new Vector3f(1.5f, 1f, 3f));

        //Legs
        for (int i = 0; i < 2; i++) {
            int neg = i == 0 ? -1 : 1;

            PVector leftOfCenter = PVector.add(center, new PVector(0, 0, .03f * neg));
            BallChain leg = new BallChain(dynamicsWorld, 5, leftOfCenter, false);
            rigObjects.addAll(leg.balls);
            addP2PConstraint(leg.head, bodyBox, 0, 0, 0, -.03f * neg, 0, .01f);

            ESOjBullet foot = new FootBox(dynamicsWorld, bodyBoxC, 5f, leftOfCenter, new Orientation());
            foot.body.setFriction(.9f);
            rigObjects.add(foot);
            addP2PConstraint(leg.tail, foot, 0, 0, 0, 0, .01f, .03f);

            addP2PConstraint(chains.get(i == 0 ? 3 : 2).tail, foot, 0, 0, 0, 0, .01f, 0);
        }





    }

    int prevMillis = 0;
    public void draw(PGraphics pG, PApplet p5) {
        int currMillis = p5.millis();
        dynamicsWorld.stepSimulation((currMillis - prevMillis) / 500f, 20);
        System.out.println("grav:" + dynamicsWorld.getGravity(new Vector3f()));
        prevMillis = currMillis;
        for(ESOjBullet eso: rigObjects) {
            eso.update();
            eso.draw(pG);
        }

    }
}
