package com.generalprocessingunit.processing.music;

import java.util.ArrayList;
import java.util.ListIterator;

public class MusicElementSeq extends ArrayList<MusicElement> {
    ListIterator<MusicElement> iter;
    boolean repeat = true;

    public Measure getNextMeasure(TimeSignature timeSig) {
        if(null == iter) {
            iter = listIterator();
        }

        MusicElementSeq seq = new MusicElementSeq();
        float totalRhythm = 0;
        while(iter.hasNext() && totalRhythm < timeSig.totalValOfMeasure()) {
            MusicElement mE = iter.next();
            totalRhythm += mE.rhythm.val;
            seq.add(mE);
        }

        if(!iter.hasNext() && repeat) {
            iter = listIterator();
        }
        return new Measure(seq);
    }
}
