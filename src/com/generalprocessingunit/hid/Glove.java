package com.generalprocessingunit.hid;

public class Glove extends RazerHydra {
    protected int[] bend = new int[5];

//    private int baseIndex;
//
//    protected void setBaseIndex(int baseIndex) {
//        this.baseIndex = baseIndex;
//    }

    protected int getBend(int i){
        return Math.abs(800 - bend[i]);
    }
}
