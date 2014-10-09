package com.generalprocessingunit.hid;

import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.YawPitchRoll;
import processing.core.PVector;

import java.util.*;

public class Hand {
    public Glove glove;

    public Finger thumb;
    public Finger index;
    public Finger middle;
    public Finger ring;
    public Finger pinky;

    public PartsCollection palm;

    public Fingers fingers;
    public PartsCollection fingertips;
    public PartsCollection knuckles;


    public Hand(Glove glove){
        this.glove = glove;

        thumb = new Finger(0);
        index = new Finger(1);
        middle = new Finger(2);
        ring = new Finger(3);
        pinky = new Finger(4);

        palm = new PartsCollection(Arrays.asList(10, 11, 12, 13));

        fingers = new Fingers(Arrays.asList(thumb, index, middle, ring, pinky));
        fingertips = new PartsCollection(Arrays.asList(0, 1, 2, 3, 4));
        knuckles = new PartsCollection(Arrays.asList(5, 6, 7, 8, 9));
    }

    /**
     * resets location of the glove to landmark. wearer should place glove at landmark (such as head)
     * @param landmark
     */
    public void reset(PVector landmark) {
        glove.reset(landmark);
    }

    public void toggleInvertedLocation() {
        glove.toggleInvertedLocation();
    }

    public PVector getLocation() {
        return glove.getLocation();
    }

    public YawPitchRoll getYawPitchRoll() {
        return glove.rotation;
    }

    public float getYaw() {
        return glove.rotation.yaw();
    }

    public float getPitch() {
        return glove.rotation.pitch();
    }

    public float getRoll() {
        return glove.rotation.roll();
    }

    public AxisAngle getAxisAngle() {
        return glove.getAxisAngle();
    }

    public boolean isGrabbing() {
        return glove.isGrabbing();
    }

    public class Finger {
        public int fingerIndex;
        public FingerTip fingerTip = new FingerTip();
        public Knuckle knuckle = new Knuckle();

        public Finger(int fingerIndex){
            this.fingerIndex = fingerIndex;
        }

        public int getBend(){
            return glove.getBend(fingerIndex);
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            m.put(fingerIndex, intensity);       //fingertip vibrators
            m.put(fingerIndex + 5, intensity);   //knuckle vibrators
            glove.setVibrate(m);
        }

        public class FingerTip {
            public void vibrate(int intensity/*, int millis*/){
                Map m = new HashMap<Integer, Integer>();
                m.put(fingerIndex, intensity);       //fingertip vibrators
                glove.setVibrate(m);
            }
        }

        public class Knuckle {
            public void vibrate(int intensity/*, int millis*/){
                Map m = new HashMap<Integer, Integer>();
                m.put(fingerIndex + 5, intensity);       //knuckle vibrators
                glove.setVibrate(m);
            }
        }
    }



    // TODO: generify this and use for Fingers
    public class PartsCollection extends ArrayList<Integer> {
        public PartsCollection(List<Integer> indexList) {
            super(indexList);
        }

        public void vibrate(int intensity){
            Map m = new HashMap<Integer, Integer>();
            for(Integer partIndex : this){
                m.put(partIndex, intensity);
            }

            glove.setVibrate(m);
        }
    }

    // This is useful because Fingers have bend info we might want to iterate through
    public class Fingers extends ArrayList<Finger> {
        public Fingers(List<Finger> fingerList){
            super(fingerList);
        }

        public void vibrate(int intensity){
            Map m = new HashMap<Integer, Integer>();
            for(Finger finger : this){
                m.put(finger.fingerIndex, intensity);      //fingertip vibrators
                m.put(finger.fingerIndex + 5, intensity);  //knuckle vibrators
            }

            glove.setVibrate(m);
        }
    }
}
