package com.generalprocessingunit.processing.music;

import java.util.ArrayList;
import java.util.ListIterator;

public class ElementSequence extends ArrayList<Element> {
    private ListIterator<Element> iter;
    boolean repeat = true;

    public Measure getNextMeasure(TimeSignature timeSig) {
        if(null == iter) {
            iter = listIterator();
        }

        ElementSequence seq = new ElementSequence();
        float totalRhythm = 0;
        while(iter.hasNext() && totalRhythm < timeSig.totalValOfMeasure()) {
            Element e = iter.next();
            totalRhythm += e.rhythm.val;
            seq.add(e);
        }

        if(!iter.hasNext() && repeat) {
            iter = listIterator();
        }
        return new Measure(seq);
    }
}
