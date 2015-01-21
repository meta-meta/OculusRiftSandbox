package com.generalprocessingunit.processing.demos;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.generalprocessingunit.processing.PAppletBuffered;
import com.generalprocessingunit.processing.SpaceNavCamera;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import processing.core.PApplet;
import processing.core.PGraphics;

import javax.vecmath.Vector3f;


public class JBulletHelloWorld extends PAppletBuffered {

    public static void main(String[] args){
        PApplet.main(new String[]{/*"--full-screen", "--display=3",*/ JBulletHelloWorld.class.getCanonicalName()});
        // if fullscreen works, --display=hmd.DisplayId
    }

    SpaceNavCamera camera;

    static DiscreteDynamicsWorld dynamicsWorld;
    static {

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
        Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
        Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
        int maxProxies = 1024;
        AxisSweep3 overlappingPairCache =
                new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
        //BroadphaseInterface overlappingPairCache = new SimpleBroadphase(
        //		maxProxies);

        // the default constraint solver. For parallel processing you can use a
        // different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(
                dispatcher, overlappingPairCache, solver,
                collisionConfiguration);

        dynamicsWorld.setGravity(new Vector3f(0, -20, 0));

        // create a few basic rigid bodies
        CollisionShape groundShape = new BoxShape(new Vector3f(50.f, 5f, 50.f));

        // keep track of the shapes, we release memory at exit.
        // make sure to re-use collision shapes among rigid bodies whenever
        // possible!
        ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();

        collisionShapes.add(groundShape);

        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(new Vector3f(0.f, -56.f, 0.f));

        {
            float mass = 0f;

            // rigidbody is dynamic if and only if mass is non zero,
            // otherwise static
            boolean isDynamic = (mass != 0f);

            Vector3f localInertia = new Vector3f(0, 0, 0);
            if (isDynamic) {
                groundShape.calculateLocalInertia(mass, localInertia);
            }

            // using motionstate is recommended, it provides interpolation
            // capabilities, and only synchronizes 'active' objects
            DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
            RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(
                    mass, myMotionState, groundShape, localInertia);
            RigidBody body = new RigidBody(rbInfo);

            // add the body to the dynamics world
            dynamicsWorld.addRigidBody(body);
        }

        {
            // create a dynamic rigidbody

            // btCollisionShape* colShape = new
            // btBoxShape(btVector3(1,1,1));
            CollisionShape colShape = new SphereShape(1.f);
            collisionShapes.add(colShape);

            // Create Dynamic Objects
            Transform startTransform = new Transform();
            startTransform.setIdentity();

            float mass = 1f;

            // rigidbody is dynamic if and only if mass is non zero,
            // otherwise static
            boolean isDynamic = (mass != 0f);

            Vector3f localInertia = new Vector3f(0, 0, 0);
            if (isDynamic) {
                colShape.calculateLocalInertia(mass, localInertia);
            }

            startTransform.origin.set(new Vector3f(2, 20, 0));

            // using motionstate is recommended, it provides
            // interpolation capabilities, and only synchronizes
            // 'active' objects
            DefaultMotionState myMotionState = new DefaultMotionState(startTransform);

            RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(
                    mass, myMotionState, colShape, localInertia);
            RigidBody body = new RigidBody(rbInfo);

            dynamicsWorld.addRigidBody(body);
        }
    }


    @Override
    public void setup() {
        size(1200, 800, OPENGL);
        super.setup();

        camera = new SpaceNavCamera(this);
        camera.setLocation(0, 0, 100);
        camera.rotate(new YawPitchRoll(PI, 0, 0));
    }

    @Override
    public final void draw(PGraphics pG) {
        camera.update();
        camera.camera(pG);

        pG.background(30, 20, 30);
        pG.lights();

//        pG.box(10);

        dynamicsWorld.stepSimulation(1.f / frameRate, 10);

        // print positions of all objects
        for (int j=dynamicsWorld.getNumCollisionObjects()-1; j>=0; j--)
        {
            CollisionObject obj = dynamicsWorld.getCollisionObjectArray().getQuick(j);
            RigidBody body = RigidBody.upcast(obj);
            if (body != null && body.getMotionState() != null) {
                Transform trans = new Transform();
                body.getMotionState().getWorldTransform(trans);
                System.out.printf("world pos = %f,%f,%f\n", trans.origin.x,
                        trans.origin.y, trans.origin.z);

                pG.pushMatrix();
                {
                    pG.translate(trans.origin.x, trans.origin.y, trans.origin.z);
                    pG.box(5);
                }
                pG.popMatrix();
            }
        }
    }

}
