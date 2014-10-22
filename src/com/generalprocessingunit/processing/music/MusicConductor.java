package com.generalprocessingunit.processing.music;

import processing.core.PApplet;

public class MusicConductor {
    PApplet p5;
    int bpm;
    RhythmType resolution;
    TimeSignature timeSignature;

    private int millisAtMeasureStart;
    private boolean running = false;

    public MusicConductor(PApplet p5, int bpm, RhythmType resolution, TimeSignature timeSignature) {
        this.p5 = p5;
        this.bpm = bpm;
        this.resolution = resolution;
        this.timeSignature = timeSignature;
    }

    public void start() {
        if(!running){
            startNewMeasure();
            running = true;
        }
    }

    private int ticksPerMinute() {
        return (int)(timeSignature.getsOneBeat.val / resolution.val);
    }

    private int millisPerTick() {
        return 60000 / ticksPerMinute();
    }

    private int millisPerBeat() {
        return 60000 / bpm;
    }

    public int getCurrentTick() {
        return millisSinceMeasureStart() / millisPerTick();
    }

    public boolean isTimeForNextMeasure() {
        if(millisSinceMeasureStart() > millisPerMeasure()) {
            startNewMeasure();
            return true;
        } else {
            return false;
        }
    }

    public int millisPerMeasure() {
        return timeSignature.beatsPerMeasure * millisPerBeat();
    }

    public int millisSinceMeasureStart() {
        return p5.millis() - millisAtMeasureStart;
    }

    public float measureProgress() {
        return millisSinceMeasureStart() / (float)millisPerMeasure();
    }

    private void startNewMeasure() {
        millisAtMeasureStart = p5.millis();
    }

    //TODO: Pause?
}
