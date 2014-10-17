package com.generalprocessingunit.hid;

import com.generalprocessingunit.processing.space.AxisAngle;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import processing.core.PVector;

import java.util.*;

public class Hand {
    public GloveDevice gloveDevice;

    public Finger thumb;
    public Finger pointer;
    public Finger middle;
    public Finger ring;
    public Finger pinky;

    public List<Vibrator> vibrators = new ArrayList<>(14);

    public VibratorCollection palm;

    public Fingers fingers;
    public VibratorCollection fingertips;
    public VibratorCollection knuckles;


    public Hand(GloveDevice gloveDevice){
        this.gloveDevice = gloveDevice;

        thumb = new Finger(0);
        pointer = new Finger(1);
        middle = new Finger(2);
        ring = new Finger(3);
        pinky = new Finger(4);

        for (int i = 0; i < 14; i++) {
            vibrators.add(new Vibrator(i));
        }

        fingers = new Fingers(Arrays.asList(thumb, pointer, middle, ring, pinky));
        fingertips = new VibratorCollection(vibrators.subList(0, 5));
        knuckles = new VibratorCollection(vibrators.subList(5, 10));

        palm = new VibratorCollection(vibrators.subList(10, 14));
    }

    /**
     * resets location of the glove to landmark. wearer should place glove at landmark (such as head)
     * @param landmark
     */
    public void reset(PVector landmark) {
        gloveDevice.reset(landmark);
    }

    public void toggleInvertedLocation() {
        gloveDevice.toggleInvertedLocation();
    }

    public PVector getLocation() {
        return gloveDevice.getLocation();
    }

    public YawPitchRoll getYawPitchRoll() {
        return gloveDevice.rotation;
    }

    public float getYaw() {
        return gloveDevice.rotation.yaw();
    }

    public float getPitch() {
        return gloveDevice.rotation.pitch();
    }

    public float getRoll() {
        return gloveDevice.rotation.roll();
    }

    public AxisAngle getAxisAngle() {
        return gloveDevice.getAxisAngle();
    }

    public boolean isGrabbing() {
        return gloveDevice.isGrabbing();
    }

    public class Finger implements VibratingPart {
        public int fingerIndex;
        public FingerTip fingerTip = new FingerTip();
        public Knuckle knuckle = new Knuckle();

        public Finger(int fingerIndex){
            this.fingerIndex = fingerIndex;
        }

        public int getBend(){
            return gloveDevice.getBend(fingerIndex);
        }

        @Override
        public void setVibrate(int intensity) {
            fingerTip.setVibrate(intensity);
            knuckle.setVibrate(intensity);
        }

        @Override
        public void vibrate(int intensity) {
            fingerTip.vibrate(intensity);
            knuckle.vibrate(intensity);
        }

        public class FingerTip implements VibratingPart {
            @Override
            public void setVibrate(int intensity) {
                vibrators.get(fingerIndex).setVibrate(intensity);
            }

            @Override
            public void vibrate(int intensity){
                vibrators.get(fingerIndex).vibrate(intensity);
            }
        }

        public class Knuckle implements VibratingPart {
            @Override
            public void setVibrate(int intensity) {
                vibrators.get(5 + fingerIndex).setVibrate(intensity);
            }

            public void vibrate(int intensity){
                vibrators.get(5 + fingerIndex).vibrate(intensity);
            }
        }
    }

    // TODO: generify this and use for Fingers
    public class VibratorCollection extends ArrayList<Vibrator> implements VibratingPart {
        public VibratorCollection(List<Vibrator> vibrators) {
            super(vibrators);
        }

        @Override
        public void setVibrate(int intensity) {
            for(Vibrator v : this){
                v.setVibrate(intensity);
            }
        }

        @Override
        public void vibrate(int intensity) {
            setVibrate(intensity);
            doVibrate();
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

            gloveDevice.vibrate(m);
        }
    }

    public interface VibratingPart {
        public void setVibrate(int intensity);
        public void vibrate(int intensity);
    }

    Map<Integer, Integer> vibes = new HashMap();
    public class Vibrator {
        public int index;
        Vibrator(int index) {
            this.index = index;
        }

        public void setVibrate(int intensity) {
            vibes.put(index, intensity);
        }

        public void vibrate(int intensity) {
            setVibrate(intensity);
            doVibrate();
        }
    }

    public void doVibrate() {
        gloveDevice.vibrate(vibes);
        vibes.clear();
    }


}
