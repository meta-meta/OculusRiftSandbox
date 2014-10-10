package com.generalprocessingunit.vr.controls;

import com.generalprocessingunit.hid.GloveManager;
import com.generalprocessingunit.hid.Hand;
import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.YawPitchRoll;
import com.generalprocessingunit.vr.PAppletVR;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class GloveVR {
    PAppletVR p5;

    GloveManager gloveManager = new GloveManager();
    public Hand leftHand = gloveManager.getLeftHand();

    EuclideanSpaceObject razerHydraSensor = new EuclideanSpaceObject();
    EuclideanSpaceObject palm = new EuclideanSpaceObject();
    EuclideanSpaceObject pinky = new EuclideanSpaceObject();
    EuclideanSpaceObject ring = new EuclideanSpaceObject();
    EuclideanSpaceObject middle = new EuclideanSpaceObject();
    EuclideanSpaceObject pointer = new EuclideanSpaceObject();
    EuclideanSpaceObject thumb = new EuclideanSpaceObject();

    EuclideanSpaceObject pinkyKnuckle = new EuclideanSpaceObject();
    EuclideanSpaceObject ringKnuckle = new EuclideanSpaceObject();
    EuclideanSpaceObject middleKnuckle = new EuclideanSpaceObject();
    EuclideanSpaceObject pointerKnuckle = new EuclideanSpaceObject();
    EuclideanSpaceObject thumbKnuckle = new EuclideanSpaceObject();

    PVector shoulderLocation;

    public GloveVR(PAppletVR p5) {
        this.p5 = p5;

        leftHand.glove.addChild(razerHydraSensor, new PVector(0, 0, -.025f), new YawPitchRoll(0, 0, 0));
        leftHand.glove.addChild(palm, new PVector(0, -.0125f, -.03f), new YawPitchRoll(0, 0, 0));

        leftHand.glove.addChild(pinkyKnuckle,      new PVector(-.0175f, -.0125f,   .0f), new YawPitchRoll(0, 0, 0));
        leftHand.glove.addChild(ringKnuckle,       new PVector(-.0075f, -.0125f,   .0f), new YawPitchRoll(0, 0, 0));
        leftHand.glove.addChild(middleKnuckle,     new PVector( .005f, -.0125f,    .0f), new YawPitchRoll(0, 0, 0));
        leftHand.glove.addChild(pointerKnuckle,    new PVector( .0175f, -.0125f,   .0f), new YawPitchRoll(0, 0, 0));
        leftHand.glove.addChild(thumbKnuckle,      new PVector( .0275f,  -.025f, -.025f), new YawPitchRoll(.6f, 0, -1.2f));
        
        // TODO: these should not need to be offset from the knuckle. something is wrong
        pinkyKnuckle.addChild(pinky,      new PVector(-.0175f, -.0125f, .0175f), new YawPitchRoll(0, 0, 0));
        ringKnuckle.addChild(ring,        new PVector(-.0075f, -.0125f, .02f), new YawPitchRoll(0, 0, 0));
        middleKnuckle.addChild(middle,    new PVector( .005f, -.0125f, .0225f), new YawPitchRoll(0, 0, 0));
        pointerKnuckle.addChild(pointer,  new PVector( .0175f, -.0125f, .02f), new YawPitchRoll(0, 0, 0));
        thumbKnuckle.addChild(thumb,      new PVector( .0275f,  -.025f, .0025f), new YawPitchRoll(0, 0, 0));
    }

    public void update() {
        gloveManager.poll();

        p5.headContainer.translateAndRotateObjectWRTObjectCoords(leftHand.glove);

        pinkyKnuckle.pitch(leftHand.pinky.getBend() / 350f);
        ringKnuckle.pitch(leftHand.ring.getBend() / 350f);
        middleKnuckle.pitch(leftHand.middle.getBend() / 350f);
        pointerKnuckle.pitch(leftHand.index.getBend() / 350f);
        thumbKnuckle.pitch(leftHand.thumb.getBend() / 350f);
    }

    public void drawView(PGraphics pG) {
        drawHand(leftHand, pG);
    }

    void drawHand(Hand hand, PGraphics pG) {

        shoulderLocation = p5.neck.getTranslationWRTObjectCoords( new PVector(-.01f, -.02f, -.01f));

        pG.colorMode(PConstants.RGB);
        pG.fill(255, 0, 0);

        drawHandPart(pG, razerHydraSensor, new PVector(.0125f, .0125f, .05f));

        pG.fill(10);

        drawHandPart(pG, palm,    new PVector(.0425f, .0125f, .05f));
        drawHandPart(pG, pinky,   new PVector(.005f, .01f, .035f));
        drawHandPart(pG, ring,    new PVector(.0075f, .0125f, .04f));
        drawHandPart(pG, middle,  new PVector(.0075f, .0125f, .045f));
        drawHandPart(pG, pointer, new PVector(.0075f, .0125f, .04f));
        drawHandPart(pG, thumb,   new PVector(.01f, .0125f, .03f));


        PVector wristLoc = hand.glove.getTranslationWRTObjectCoords(new PVector(0, 0, -0.05f));
        for (float i = 0; i < 1; i += .1f) {
            pG.pushMatrix();
            {
                PVector loc = PVector.lerp(shoulderLocation, wristLoc, i);
                pG.translate(loc.x, loc.y, loc.z);
                pG.sphere(.01f);
            }
            pG.popMatrix();
        }

        pG.pushMatrix();  // shoulder
        {
            PVector loc = shoulderLocation;
            pG.translate(loc.x, loc.y, loc.z);
            pG.fill(255, 0, 255);
            pG.sphere(.0025f);
        }
        pG.popMatrix();

    }

    void drawHandPart(PGraphics pG, EuclideanSpaceObject part, PVector dimensions) {
        pG.pushMatrix();
        {
            pG.translate(part.x(), part.y(), part.z());
            AxisAngle aa = part.getAxisAngle();
            pG.rotate(aa.w, aa.x, aa.y, aa.z);
            pG.box(dimensions.x, dimensions.y, dimensions.z);
        }
        pG.popMatrix();
    }

    public void reset() {
        leftHand.reset( PVector.sub(p5.head.getLocation(), p5.headContainer.getLocation()));
    }

    public void invert() {
        leftHand.toggleInvertedLocation();
    }
}
