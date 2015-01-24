package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
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
    static final float gravity = -8f;

    public Set<ESOjBullet> esOjBullets = new HashSet<>();

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

        dynamicsWorld.setGravity(new Vector3f(0, gravity, 0));
    }

    List<BallChain> chains = new ArrayList<>();

    public List<BallChain> spawnMarionette(List<PVector> locations) {
        for(PVector location: locations) {
            BallChain chain = new BallChain(dynamicsWorld, 10, location);
            chains.add(chain);
            esOjBullets.addAll(chain.balls);
        }



        return chains;
    }

    void attachModelToChains() {

    }

    public void draw(PGraphics pG, PApplet p5) {
        dynamicsWorld.stepSimulation(1f / 30, 5);
        for(ESOjBullet eso: esOjBullets) {
            eso.update();
            eso.draw(pG);
        }

    }
}
