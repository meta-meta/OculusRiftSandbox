package com.generalprocessingunit.hid;

import processing.core.PVector;

import java.util.Map;

public class Glove extends RazerHydra {
    private GloveManager gloveManager;

    protected int[] bend = new int[5];
    private int[] bendOffset = new int[5];

    private Glove(){};

    protected Glove(GloveManager gloveManager) {
        this.gloveManager = gloveManager;
    }

    protected int getBend(int i){
        return bendOffset[i] - bend[i];
    }

    protected void setVibrate(Map<Integer, Integer> vibrate) {
        // TODO: modify map: add 14 * gloveIndex so we can have a right hand
        gloveManager.setVibrate(vibrate);
    }

    @Override
    public boolean isGrabbing() {
        for (int i = 1; i < bend.length; i++) {
            if(bendOffset[i] - bend[i] < 150) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void reset(PVector landmark) {
        super.reset(landmark);
        System.arraycopy(bend, 0, bendOffset, 0, bend.length);
    }
}
