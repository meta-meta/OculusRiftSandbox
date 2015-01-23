package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.processing.space.Quaternion;
import processing.core.PVector;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;


public class ESOjBullet extends EuclideanSpaceObject {
    RigidBody body;

    public ESOjBullet(DiscreteDynamicsWorld dynamicsWorld, CollisionShape collisionShape, float mass, PVector location, Orientation orientation) {
        super(location, orientation);

        initPhysics(dynamicsWorld, collisionShape, location, mass);
    }

    private void initPhysics(DiscreteDynamicsWorld dynamicsWorld, CollisionShape collisionShape, PVector initialTransform, float mass) {
        // rigidbody is dynamic if and only if mass is non zero,
        // otherwise static
        boolean isDynamic = (mass != 0f);

        Vector3f localInertia = new Vector3f(0, 0, 0);
        if (isDynamic) {
            collisionShape.calculateLocalInertia(mass, localInertia);
        }

        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set(new Vector3f(initialTransform.x, initialTransform.y, initialTransform.z));
//        Quaternion q = getOrientationQuat();
//        transform.setRotation(new Quat4f(q.w, q.x, q.y, q.z));

        // using motionstate is recommended, it provides interpolation
        // capabilities, and only synchronizes 'active' objects
        DefaultMotionState myMotionState = new DefaultMotionState(transform);

        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(
                mass, myMotionState, collisionShape, localInertia);
//        rbInfo.linearDamping = .5f;
        body = new RigidBody(rbInfo);

        dynamicsWorld.addRigidBody(body);
    }

    public void update() {
        if (body != null && body.getMotionState() != null) {
            Transform trans = new Transform();
            body.getMotionState().getWorldTransform(trans);
            setLocation(trans.origin.x / 100f, trans.origin.y / 100f, trans.origin.z / 100f);
        }
    }

    @Override
    public void setLocation(PVector v) {
        Transform trans = new Transform();
        trans.setIdentity();
        trans.origin.set(new Vector3f(v.x * 100f, v.y * 100f, v.z * 100f));
//        body.getMotionState().getWorldTransform(trans);
//

        body.setWorldTransform(trans);
        body.getMotionState().setWorldTransform(trans);
        body.setInterpolationAngularVelocity(new Vector3f(0, 0, 0));
        body.setInterpolationLinearVelocity(new Vector3f(0, 0, 0));


//        body.getMotionState().setWorldTransform(trans);
//        body.setCollisionFlags(body.getCollisionFlags() | CollisionFlags.STATIC_OBJECT);

//        body.translate(new Vector3f(.001f, 0, 0));//new Vector3f(v.x - trans.origin.x, v.y - trans.origin.y, v.z - trans.origin.z));
        super.setLocation(v);
    }
}
