package com.generalprocessingunit.hid;

import processing.core.PVector;

public class Glove extends RazerHydra {
    protected int[] bend = new int[5];
    private int[] bendOffset = new int[5];

    protected int getBend(int i){
        return bendOffset[i] - bend[i];
    }

    @Override
    public boolean isGrabbing() {
        for (int i = 0; i < bend.length; i++) {
            if(getBend(i) < 200) {
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
