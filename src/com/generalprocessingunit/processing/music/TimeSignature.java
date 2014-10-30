package com.generalprocessingunit.processing.music;

public class TimeSignature {
    int beatsPerMeasure;
    Duration getsOneBeat;

    public TimeSignature(int beatsPerMeasure, Duration getsOneBeat) {
        this.beatsPerMeasure = beatsPerMeasure;
        this.getsOneBeat = getsOneBeat;
    }

    public float totalValOfMeasure() {
        return beatsPerMeasure * getsOneBeat.val;
    }

    public static final TimeSignature FourFour = new TimeSignature(4, Duration.Quarter);
    public static final TimeSignature ThreeFour = new TimeSignature(3, Duration.Quarter);
}
