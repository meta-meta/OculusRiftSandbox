package com.generalprocessingunit.processing.music;

import processing.core.PApplet;

import java.util.HashSet;
import java.util.Set;

public class MusicConductor {
    PApplet p5;
    int bpm;
    RhythmType resolution;
    TimeSignature timeSignature;

    private int millisAtMeasureStart;
    private boolean running = false;

    private Set<MusicElementTime> musicElementTimes;

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
        return ticksForRhythmType(timeSignature.getsOneBeat) * bpm;
    }

    private int ticksForRhythmType(RhythmType rhythm) {
        return (int)(rhythm.val / resolution.val);
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
        musicElementTimes = null;
    }

    public int millisForRhythmType(RhythmType rhythm) {
        return millisForRhythmVal(rhythm.val);
    }

    public int millisForRhythmVal(float rhythmVal) {
        return (int)(millisPerBeat() * (rhythmVal / timeSignature.getsOneBeat.val));
    }

    public void markPlayedAndMissedNotes(Measure measure, Set<Integer> currentlyPlayingNotes) {
        refreshNoteTimes(measure);

        int currentTick = getCurrentTick();

        for(MusicElementTime musicElementTime : musicElementTimes) {
            if(musicElementTime.isPlaying(currentTick)) {
                MusicElement mE = musicElementTime.mE;

                mE.wasPassed = true;

                if(musicElementTime.mE instanceof MusicNote) {
                    if(currentlyPlayingNotes.contains(((MusicNote)mE).noteNumber)) {
                        musicElementTime.mE.incrementPercentagePlayed( 1f / ticksForRhythmType(mE.rhythm) );
                    }
                } else {
                    if(currentlyPlayingNotes.isEmpty()) {
                        musicElementTime.mE.incrementPercentagePlayed( 1f / ticksForRhythmType(mE.rhythm) );
                    }
                }
            }
        }
    }

    private void refreshNoteTimes(Measure measure) {
        if(null != musicElementTimes) return;

        musicElementTimes = new HashSet<>();

        int ticksTotal = 0;

        for(MusicElement mE : measure.elementSeq) {
            // don't show what we played last time around
            mE.reset();

            musicElementTimes.add(new MusicElementTime(
                    ticksTotal,
                    ticksTotal + ticksForRhythmType(mE.rhythm),
                    mE
            ));

            ticksTotal += ticksForRhythmType(mE.rhythm);
        }
    }

    class MusicElementTime {
        int startTick;
        int endTick;
        MusicElement mE;

        MusicElementTime(int startTick, int endTick, MusicElement mE) {
            this.startTick = startTick;
            this.endTick = endTick;
            this.mE = mE;
        }

        boolean isPlaying(int currentTick) {
            return startTick <= currentTick && endTick > currentTick;
        }
    }

    //TODO: Pause?
}
