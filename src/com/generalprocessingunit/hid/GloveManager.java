package com.generalprocessingunit.hid;


import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.YawPitchRoll;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import processing.core.PVector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;


public class GloveManager {
    private Glove[] gloves = new Glove[2];

    private RazerHydraManager razerHydraManager = new RazerHydraManager(gloves);

    GloveManagerSerialCom serialCom;

    public GloveManager() {
        for (int i = 0; i < 2; i++) {
            gloves[i] = new Glove();
        }

        serialCom = new GloveManagerSerialCom(this);
    }

    public synchronized void destroy() {
        serialCom.destroy();
        razerHydraManager.destroy();
    }


    protected void updateBendState(String s){
        int i = 0;
        for (String b : s.split(",")) {
            try {
                gloves[i < 5 ? 0 : 1].bend[i] = Integer.parseInt(b);
            } catch (Exception e) {
                //no-op
            }

            i++;

            if (i > 10) {
                break;
            }
        }

    }

    public void poll() {
        razerHydraManager.poll();
    }

    /* note: vibration patterns last for ~350 millis */
    public void setVibrate(Map<Integer, Integer> vibrate) {
        StringBuilder sb = new StringBuilder(30).append("V");
        for (int i = 0; i < 14; i++) {
            sb.append(vibrate.containsKey(i) ? String.format("%02d", vibrate.get(i)) : "00");
        }
        sb.append("\n");
        serialCom.write(sb.toString());
    }

    public LeftHand getLeftHand() {
        return new LeftHand(this);
    }

    public RightHand getRightHand() {
      return new RightHand(this);
    }

    public class LeftHand extends Hand{
        LeftHand(GloveManager gloveManager){
            super(gloveManager, 0);
        }
    }


    public class RightHand extends Hand{
        RightHand(GloveManager gloveManager){
            super(gloveManager, 1);
        }
    }


    public class Hand {
        public GloveManager gloveManager;
        private int gloveIndex;

        public Glove glove;

        public Finger thumb;
        public Finger index;
        public Finger middle;
        public Finger ring;
        public Finger pinky;

        public Palm palm;

        public Fingers fingers;
        public Fingertips fingertips;


        public Hand(GloveManager gloveManager, int gloveIndex){
            this.gloveManager = gloveManager;
            this.gloveIndex = gloveIndex;

            glove = gloveManager.gloves[gloveIndex];

            int zeroIndexBend = gloveIndex * 5;
            int zeroIndexVibrate = gloveIndex * 14;

            thumb = new Finger(gloveManager, zeroIndexBend);
            index = new Finger(gloveManager, zeroIndexBend + 1);
            middle = new Finger(gloveManager, zeroIndexBend + 2);
            ring = new Finger(gloveManager, zeroIndexBend + 3);
            pinky = new Finger(gloveManager, zeroIndexBend + 4);

            palm = new Palm(gloveManager, gloveIndex);

            fingers = new Fingers(Arrays.asList(thumb, index, middle, ring, pinky), gloveManager) ;
            fingertips = new Fingertips(Arrays.asList(thumb, index, middle, ring, pinky), gloveManager) ;
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
    }


    public static class Part {
        public GloveManager gloveManager;

        public Part(GloveManager gloveManager){
            this.gloveManager = gloveManager;
        }
    }

    public static class Finger extends Part {
        public int fingerIndex;

        public Finger(GloveManager gloveManager, int fingerIndex){
            super(gloveManager);
            this.fingerIndex = fingerIndex;
        }

        public int getBend(){
            return gloveManager.gloves[fingerIndex < 5 ? 0 : 1].getBend(fingerIndex % 5); //TODO: this obviously needs to be refactored to have a reference to the glove
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            m.put(fingerIndex, intensity);       //fingertip vibrators
            m.put(fingerIndex + 5, intensity);   //knuckle vibrators
            this.gloveManager.setVibrate(m);
        }
    }


    public static class Fingers extends ArrayList<Finger> {
        private GloveManager glove;

        public Fingers(List<Finger> fingerList, GloveManager glove){
            super(fingerList);
            this.glove = glove;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            for(Finger finger : this){
                m.put(finger.fingerIndex, intensity);      //fingertip vibrators
                m.put(finger.fingerIndex + 5, intensity);  //knuckle vibrators
            }

            glove.setVibrate(m);
        }
    }

    public static class Fingertips extends ArrayList<Finger> {
        private GloveManager glove;

        public Fingertips(List<Finger> fingerList, GloveManager glove){
            super(fingerList);
            this.glove = glove;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            for(Finger finger : this){
                m.put(finger.fingerIndex, intensity);      //fingertip vibrators
            }

            glove.setVibrate(m);
        }
    }



    public static class Palm extends Part {
        private int zeroIndex;

        public Palm(GloveManager glove, int zeroIndex){
            super(glove);
            this.zeroIndex = zeroIndex;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            m.put(zeroIndex + 10, intensity);       //index-middle
            m.put(zeroIndex + 11, intensity);   //ring-pinky
            m.put(zeroIndex + 12, intensity);       //thumb
            m.put(zeroIndex + 13, intensity);   //outer corner
            gloveManager.setVibrate(m);
        }
    }

}
