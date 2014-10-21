package com.generalprocessingunit.processing.music;

import java.util.ArrayList;
import java.util.List;

public class MeasureQueue {
    List<Measure> measures = new ArrayList<>();

    public Measure currentMeasure(){
        if(measures.size() < 2) {
            return new Measure(new MusicElementSeq());
        } else {
            return measures.get(1);
        }
    }
    public Measure prevMeasure(){
        if(measures.size() < 1) {
            return new Measure(new MusicElementSeq());
        } else {
            return measures.get(0);
        }
    }
    public Measure nMeasuresFromCurrent(int n){
        if(measures.size() < 2 + n) {
            return new Measure(new MusicElementSeq());
        } else {
            return measures.get(1 + n);
        }
    }

    public void addMeasure(Measure m) {
        measures.add(m);
    }

    public void removeMeasure() {
        measures.remove(0);
    }
}
