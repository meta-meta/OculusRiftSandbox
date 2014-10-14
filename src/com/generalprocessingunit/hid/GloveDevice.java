package com.generalprocessingunit.hid;

import processing.core.PVector;

import java.util.Map;

public class GloveDevice extends RazerHydra {
    private GloveManager gloveManager;

    protected int[] bend = new int[5];
    private int[] bendOffset = new int[5];

    private GloveDevice(){};

    protected GloveDevice(GloveManager gloveManager) {
        this.gloveManager = gloveManager;
    }

    protected int getBend(int i){
        return bendOffset[i] - bend[i];
    }

    protected void vibrate(Map<Integer, Integer> vibes) {
        // TODO: modify map: add 14 * gloveIndex so we can have a right hand
        gloveManager.vibrate(vibes);
    }

    @Override
    public boolean isGrabbing() {
        for (int i = 1; i < 4; i++) {
            if(getBend(i) < 100) {
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
