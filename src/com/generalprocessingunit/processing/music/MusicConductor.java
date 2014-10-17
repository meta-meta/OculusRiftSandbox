package com.generalprocessingunit.processing.music;

public class MusicConductor {
    int bpm;
    RhythmType resolution;
    TimeSignature timeSignature;

    private long millisAtStart;
    private boolean running = false;

    public MusicConductor(int bpm, RhythmType resolution, TimeSignature timeSignature) {
        this.bpm = bpm;
        this.resolution = resolution;
        this.timeSignature = timeSignature;
    }

    public void start() {
        if(!running){
            millisAtStart = System.currentTimeMillis();
            running = true;
        }
    }

    private int ticksPerMinute() {
        return (int)(timeSignature.getsOneBeat.val / resolution.val);
    }

    private int lengthOfTick() {
        return 60000 / ticksPerMinute();
    }

    public int getCurrentTick() {
        int ellapsedMillis = (int)(System.currentTimeMillis() - millisAtStart);
        return ellapsedMillis / lengthOfTick();
    }

    //TODO: Pause?
}
