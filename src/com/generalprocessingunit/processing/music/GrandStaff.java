package com.generalprocessingunit.processing.music;

import com.generalprocessingunit.music.Key;
import com.generalprocessingunit.processing.ProcessingDelegateComponent;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Set;

public class GrandStaff extends ProcessingDelegateComponent {
    private int size;
    Conductor conductor;

    private Staff trebleStaff;
    private Staff bassStaff;

    MeasureQueue measureQueue = new MeasureQueue();

    public GrandStaff(PApplet p5, int size, Key keySig, Conductor conductor) {
        super(p5);

        this.size = size;
        this.conductor = conductor;
        trebleStaff = new Staff(p5, size, Clef.Treble, keySig, conductor);
        bassStaff = new Staff(p5, size, Clef.Bass, keySig, conductor);
    }

    public void setKeySig(Key keySig) {
        trebleStaff.setKeySig(keySig);
        bassStaff.setKeySig(keySig);
    }

    public void update(Set<Integer> currentlyPlayingNotes) {
        // TODO: currently only one measure is really supported by conductor
        conductor.markPlayedAndMissedNotes(measureQueue.currentMeasure(), currentlyPlayingNotes);
    }

    public void addMeasure(Measure measure) {
        ElementSequence trebleSeq = new ElementSequence();
        ElementSequence bassSeq = new ElementSequence();

        // TODO: polyphony
        for(Element e : measure.seq) {
            if(e instanceof Note) {
                if(((Note) e).noteNumber >= 60) {
                    trebleSeq.add(e);
                    bassSeq.add(new ElementBlank(e.rhythm));
                } else {
                    bassSeq.add(e);
                    trebleSeq.add(new ElementBlank(e.rhythm));
                }
            } else if (e instanceof Rest) {
                trebleSeq.add(e);
                bassSeq.add(e);
            }
        }


//        float trebleRest = 0;
//        float bassRest = 0;
//
//        // TODO: polyphony
//        for(Element e : measure.seq) {
//            if(e instanceof Note) {
//                if(((Note) e).noteNumber >= 60) {
//                    if(trebleRest > 0) {
//                        trebleSeq.addAll(Duration.restsForVal(trebleRest));
//                        trebleRest = 0;
//                    }
//
//                    trebleSeq.add(e);
//                    bassRest += e.rhythm.val;
//
////                    bassSeq.add(new Rest(e.rhythm));
//                } else {
//                    if(bassRest > 0) {
//                        bassSeq.addAll(Duration.restsForVal(bassRest));
//                        bassRest = 0;
//                    }
//
//                    bassSeq.add(e);
//                    trebleRest += e.rhythm.val;
//
////                    bassSeq.add(new Rest(e.rhythm));
//                }
//            } else if (e instanceof Rest) {
////                trebleSeq.add(e);
////                bassSeq.add(e);
//                bassRest += e.rhythm.val;
//                trebleRest += e.rhythm.val;
//            }
//        }
//
//        if(trebleRest > 0) {
//            trebleSeq.addAll(Duration.restsForVal(trebleRest));
//        }
//        padSequence(trebleSeq, measure.seq.size());
//
//        if(bassRest > 0) {
//            bassSeq.addAll(Duration.restsForVal(bassRest));
//        }
//        padSequence(bassSeq, measure.seq.size());


        measureQueue.addMeasure(measure);
        trebleStaff.measureQueue.addMeasure(new Measure(trebleSeq));
        bassStaff.measureQueue.addMeasure(new Measure(bassSeq));
    }

    void padSequence(ElementSequence seq, int totalElementsNeeded) {
        int padding = totalElementsNeeded - seq.size();
        for (int i = 0; i < padding / 2; i++) {
            seq.add(0, new ElementBlank(Duration.Whole));
        }

        for (int i = padding / 2; i < padding; i++) {
            seq.add(new ElementBlank(Duration.Whole));
        }
    }

    public void removeMeasure() {
        trebleStaff.measureQueue.removeMeasure();
        bassStaff.measureQueue.removeMeasure();
        measureQueue.removeMeasure();
    }

    @Override
    public void draw(PGraphics pG) {
        float halfHeight = pG.height / 2;
        float yOffset = size * .8f;

        pG.fill(127);
        pG.noStroke();
        pG.pushMatrix(); // light grey rectangle
        {
            pG.translate(0, halfHeight);
            pG.rect(0, -size * 4.3f, pG.width, size * 8.5f);
        }
        pG.popMatrix();

        pG.pushMatrix(); // Treble staff
        {
            pG.translate(0, halfHeight - yOffset);
            trebleStaff.draw(pG);
        }
        pG.popMatrix();

        pG.pushMatrix(); // Bass staff
        {
            pG.translate(0, halfHeight + yOffset);
            bassStaff.draw(pG);
        }
        pG.popMatrix();


        pG.pushMatrix(); // Clef Mask
        {
            pG.translate(0, halfHeight);

            pG.fill(64);
            pG.noStroke();
            pG.rect(0, -size * 4.3f, size * 4.05f, size * 8.5f);
        }
        pG.popMatrix();


        pG.pushMatrix(); // Treble clef
        {
            pG.translate(0, halfHeight - yOffset);
            trebleStaff.drawClef(pG);
        }
        pG.popMatrix();

        pG.pushMatrix(); // Bass clef
        {
            pG.translate(0, halfHeight + yOffset);
            bassStaff.drawClef(pG);
        }
        pG.popMatrix();
    }
}
