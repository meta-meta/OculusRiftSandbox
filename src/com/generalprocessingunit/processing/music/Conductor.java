package com.generalprocessingunit.processing.music;

import processing.core.PApplet;

import java.util.HashSet;
import java.util.Set;

public class Conductor {
    PApplet p5;
    int bpm;
    Duration resolution;
    TimeSignature timeSignature;

    private int millisAtMeasureStart;
    private boolean running = false;

    private Set<ElementTiming> elementTimings;

    public Conductor(PApplet p5, int bpm, Duration resolution, TimeSignature timeSignature) {
        this.p5 = p5;
        this.bpm = bpm;
        this.resolution = resolution;
        this.timeSignature = timeSignature;
    }

    public void start() {
        if (!running) {
            startNewMeasure();
            running = true;
        }
    }

    private int ticksPerMinute() {
        return ticksForRhythmType(timeSignature.getsOneBeat) * bpm;
    }

    private int ticksForRhythmType(Duration rhythm) {
        return (int) (rhythm.val / resolution.val);
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
        if (millisSinceMeasureStart() > millisPerMeasure()) {
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
        return millisSinceMeasureStart() / (float) millisPerMeasure();
    }

    private void startNewMeasure() {
        millisAtMeasureStart = p5.millis();
        elementTimings = null;
    }

    public int millisForRhythmType(Duration rhythm) {
        return millisForRhythmVal(rhythm.val);
    }

    public int millisForRhythmVal(float rhythmVal) {
        return (int) (millisPerBeat() * (rhythmVal / timeSignature.getsOneBeat.val));
    }

    int prevTick = 0;

    public void markPlayedAndMissedNotes(Measure measure, Set<Integer> currentlyPlayingNotes) {
        refreshElementTimings(measure);

        int currentTick = getCurrentTick();

        for (ElementTiming elementTiming : elementTimings) {
            if (elementTiming.isPlaying(currentTick)) {
                Element mE = elementTiming.e;

                mE.wasPassed = true;
                mE.percentagePassed = (float) (currentTick - elementTiming.startTick) / ticksForRhythmType(mE.rhythm);

                if (elementTiming.e instanceof Note) {
                    if (currentlyPlayingNotes.contains(((Note) mE).noteNumber) && currentTick != prevTick) {
                        elementTiming.e.incrementPercentagePlayed(1f / ticksForRhythmType(mE.rhythm));
                    }
                } else {
                    if (currentlyPlayingNotes.isEmpty() && currentTick != prevTick) {
                        elementTiming.e.incrementPercentagePlayed(1f / ticksForRhythmType(mE.rhythm));
                    }
                }
            }
        }

        prevTick = currentTick;
    }

    private void refreshElementTimings(Measure measure) {
        if (null != elementTimings) return;

        elementTimings = new HashSet<>();

        int ticksTotal = 0;

        for (Element mE : measure.seq) {
            // don't show what we played last time around
            mE.reset();

            elementTimings.add(new ElementTiming(
                    ticksTotal,
                    ticksTotal + ticksForRhythmType(mE.rhythm),
                    mE
            ));

            ticksTotal += ticksForRhythmType(mE.rhythm);
        }
    }

    class ElementTiming {
        int startTick;
        int endTick;
        Element e;

        ElementTiming(int startTick, int endTick, Element e) {
            this.startTick = startTick;
            this.endTick = endTick;
            this.e = e;
        }

        boolean isPlaying(int currentTick) {
            return startTick <= currentTick && endTick > currentTick;
        }
    }

    //TODO: Pause?
}
