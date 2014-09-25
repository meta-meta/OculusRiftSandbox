package com.generalprocessingunit.processing.ui;

import com.generalprocessingunit.processing.PAppletBuffered;

import java.util.HashSet;
import java.util.Set;

// TODO: these should be composable scala traits. doesn't make sense for PAppletTouch to be related to PAppletBuffered
public abstract class PAppletTouch extends PAppletBuffered {
    private static final Set<Touchable> touchables = new HashSet<>();

    // TODO: multitouch


    @Override
    public void setup() {
        super.setup();
    }

    public void addTouchable(Touchable t) {
//        registerMethod("update", this);
        registerPost(this);
        touchables.add(t);
    }

    public void post() {
        for (Touchable touchable : touchables) {
            touchable.update();
        }
    }
}
