package com.generalprocessingunit.processing.music;

import java.util.ArrayList;
import java.util.ListIterator;

public class MusicNoteSeq extends ArrayList<MusicNote> {
    ListIterator<MusicNote> iter;

    MusicNoteSeq getNextMeasure(TimeSignature ts) {
        if(null == iter) {
            iter = listIterator();
        }

        MusicNoteSeq seq = new MusicNoteSeq();
        float totalRhythm = 0;
        while(iter.hasNext() && totalRhythm < ts.totalValOfMeasure()) {
            MusicNote n = iter.next();
            totalRhythm += n.rhythm.val;
            seq.add(n);
        }
        return seq;
    }

}
