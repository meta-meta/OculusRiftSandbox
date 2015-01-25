package com.generalprocessingunit.processing.demos.jBulletGloveString;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Transform;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.processing.vr.controls.HandSpatialized;
import processing.core.*;

import javax.vecmath.Vector3f;
import java.awt.event.KeyEvent;
import java.util.*;

public class marionetteRig {
    DiscreteDynamicsWorld dynamicsWorld;
    static final float gravity = -11f;

    public Set<ESOjBullet> rigObjects = new HashSet<>();

    PApplet p5;

    public marionetteRig(PApplet p5) {

        this.p5 = p5;

        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();

        // use the default collision dispatcher. For parallel processing you
        // can use a diffent dispatcher (see Extras/BulletMultiThreaded)
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        Vector3f worldAabbMin = new Vector3f(-100, -100, -100);
        Vector3f worldAabbMax = new Vector3f(100, 100, 100);
        int maxProxies = 1024;
        AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);

        // the default constraint solver. For parallel processing you can use a
        // different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

        ESOjBullet lava = createLava();

        createFloor(lava);

        createBoxes();
    }

    private void createFloor(ESOjBullet lava) {
        final PShape subPlane = createGrid();

        CollisionShape floorBoxC = new BoxShape(new Vector3f(150f, 10f, 150f));


        ESOjBullet floor = new ESOjBullet(dynamicsWorld, floorBoxC, 2f, new PVector(0, -.4f, 0), new Orientation()) {
            @Override
            public void draw(PGraphics pG) {
                pushMatrixAndTransform(pG);
                {
                    pG.translate(0, .01f, 0);
                    pG.shape(subPlane);
//
//                    pG.fill(70, 50, 20);
//                    pG.box(3f, .2f, 3f);
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

        Generic6DofConstraint constr = new Generic6DofConstraint(lava.body, floor.body, t1, t2, false);
        constr.setLinearLowerLimit(new Vector3f(-100, 0, -100));
        constr.setLinearUpperLimit(new Vector3f(100, 0, 100));
        constr.setAngularLowerLimit(new Vector3f());
        constr.setAngularUpperLimit(new Vector3f());
        dynamicsWorld.addConstraint(constr, false);
    }

    private PShape createGrid() {
        // Ripped from Dog Dearth
        PGraphics grid = p5.createGraphics(5000, 5000);
        grid.beginDraw();
        grid.background(20, 255, 0, 20);

        int boldLine = grid.width / 10;
        for( int n = 0; n <= grid.width; n += grid.width / 100){
            grid.stroke(n % boldLine == 0 ? 0 : 50);
            grid.strokeWeight(n % boldLine == 0 ? 2 : 1);
            grid.line(n, 0, n, grid.width);
            grid.line(0, n, grid.width, n);
        }
        grid.endDraw();

        float w = 1.5f;

        final PShape subPlane = p5.createShape();
        subPlane.beginShape();
        subPlane.fill(255, 255);
        subPlane.noStroke();
        subPlane.texture(grid);
        subPlane.textureMode(PConstants.NORMAL);
        subPlane.vertex(-w , 0,  w, 0, 1);
        subPlane.vertex( w , 0,  w, 1, 1);
        subPlane.vertex( w , 0, -w, 1, 0);
        subPlane.vertex(-w , 0, -w, 0, 0);
        subPlane.endShape();
        return subPlane;
    }

    private ESOjBullet createLava() {
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

        return anchor;
    }

    private void createBoxes() {
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

    List<BeadChain> chains = new ArrayList<>();

    public List<BeadChain> spawnChains(List<PVector> locs, PVector hydraLoc) {
        float beadSize = 1.2f;
        CollisionShape bead = new SphereShape(beadSize);

        int i = 0;
        for(PVector loc: locs) {

            BeadChain chain = new BeadChain(
                    dynamicsWorld, bead,
                    i==0 ? 3 : (i < 2 ? 8: 12), beadSize,
                    PVector.add(loc, hydraLoc), true
            ){
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


    public void attachPuppetToChains(PVector hydraLoc) {

        PVector center = PVector.add(hydraLoc, new PVector(0, -.2f, 0f));

        ESOjBullet bodyBox = createBody(center);

        ESOjBullet head = createHead(center);

        createNeck(center, bodyBox, head);

        createLegs(center, bodyBox);
    }

    private void createLegs(PVector center, ESOjBullet bodyBox) {
        CollisionShape footBoxC = new BoxShape(new Vector3f(1.5f, 1f, 3f));

        float beadSize = 1f;
        CollisionShape bead = new SphereShape(beadSize);

        for (int i = 0; i < 2; i++) {
            int neg = i == 0 ? -1 : 1;

            PVector leftOfCenter = PVector.add(center, new PVector(0, 0, .03f * neg));
            BeadChain leg = new BeadChain(dynamicsWorld, bead, 7, beadSize, leftOfCenter, false) {
                @Override
                public void draw(PGraphics pG) {
                    pG.fill(50, 150, 200);
                    pG.sphere(1f / ESOjBullet.scale);
                }
            };

            rigObjects.addAll(leg.balls);

            addP2PConstraint(leg.head, bodyBox, 0, 0, 0, -.03f * neg, 0, .03f);

            createFoot(footBoxC, i, neg, leftOfCenter, leg);
        }
    }

    private void createFoot(final CollisionShape footBoxC, int i, int neg, final PVector leftOfCenter, BeadChain leg) {
        ESOjBullet foot = new ESOjBullet(dynamicsWorld, footBoxC, 5f, leftOfCenter, new Orientation()) {
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

        addP2PConstraint(chains.get(i == 0 ? 3 : 2).tail, foot, 0, 0, 0, 0, .01f, .01f);
        addP2PConstraint(leg.tail, foot, 0, 0, 0, neg * .015f, .01f, .03f);
    }

    private void createNeck(final PVector center, ESOjBullet bodyBox, ESOjBullet head) {
        float beadSize = 1.4f;
        CollisionShape bead = new SphereShape(beadSize);

        BeadChain neck = new BeadChain(dynamicsWorld, bead, 7, beadSize, center, false) {
            @Override
            public void draw(PGraphics pG) {
                pG.fill(50, 100, 200);
                pG.sphere(1.4f / ESOjBullet.scale);
            }
        };

        rigObjects.addAll(neck.balls);
        addP2PConstraint(head, neck.head, 0, 0, .03f, 0, .01f, 0);
        addP2PConstraint(neck.tail, bodyBox, 0, 0, 0, 0, 0, -.06f);
    }

    private ESOjBullet createHead(final PVector center) {
        CollisionShape sphereColl = new SphereShape(3f);

        ESOjBullet head = new ESOjBullet(dynamicsWorld, sphereColl, 4, PVector.add(center, new PVector(0, 0, .02f)), new Orientation()) {
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

        return head;
    }

    private ESOjBullet createBody(final PVector center) {
        CollisionShape bodyBoxC = new BoxShape(new Vector3f(3f, 2f, 3f));

        ESOjBullet bodyBox = new ESOjBullet(dynamicsWorld, bodyBoxC, 2f, center, new Orientation()) {
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

        return bodyBox;
    }

    public void draw(PGraphics pG) {
        for (ESOjBullet eso : rigObjects) {
            eso.draw(pG);
        }
    }

    float stepDivisor = 1000;
    int prevMillis = 0;
    public void update(PApplet p5) {
        int currMillis = p5.millis();
        dynamicsWorld.stepSimulation((currMillis - prevMillis) / stepDivisor, 10);
        prevMillis = currMillis;

        for (ESOjBullet eso : rigObjects) {
            eso.update();
        }
    }

    public void incStepDivisor(float inc) {
        stepDivisor += inc;
        System.out.println("stepDivisor: " + stepDivisor);
    }

    public void keyPressed(KeyEvent e, HandSpatialized glove, Set<ESOjBullet> chainHeads){
        if(java.awt.event.KeyEvent.VK_S == e.getKeyCode()) {

            List<PVector> locs = Arrays.asList(
                    new PVector(0, 0, .1f),
                    new PVector(0, 0, -.07f),
                    new PVector(.11f, 0, 0),
                    new PVector(-.11f, 0, 0)
            );


            List<BeadChain> chains = spawnChains(locs, glove.razerHydraSensor.getLocation());

            int i = 0;
            for(BeadChain chain: chains) {
                chainHeads.add(chain.head);
                glove.razerHydraSensor.addChild(chain.head, locs.get(i));
                i++;
            }
        }

        if(KeyEvent.VK_D == e.getKeyCode()) {
            attachPuppetToChains(glove.razerHydraSensor.getLocation());
        }

        if(java.awt.event.KeyEvent.VK_UP == e.getKeyCode()) {
            incStepDivisor(-10);
        }

        if(java.awt.event.KeyEvent.VK_DOWN == e.getKeyCode()) {
            incStepDivisor(10);
        }
    }

    void addP2PConstraint(ESOjBullet a, ESOjBullet b, float xA, float yA, float zA, float xB, float yB, float zB) {
        Point2PointConstraint p2p = new Point2PointConstraint(
                a.body,
                b.body,
                new Vector3f(xA * ESOjBullet.scale, yA * ESOjBullet.scale, zA * ESOjBullet.scale),
                new Vector3f(xB * ESOjBullet.scale, yB * ESOjBullet.scale, zB * ESOjBullet.scale));
        dynamicsWorld.addConstraint(p2p, true);
    }
}
