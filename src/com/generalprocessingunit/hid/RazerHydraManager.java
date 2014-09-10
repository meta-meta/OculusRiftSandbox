package com.generalprocessingunit.hid;

import com.sixense.ControllerData;
import com.sixense.Sixense;

public class RazerHydraManager {
    private static final int numControllers = 2;
    private ControllerData[] data = new ControllerData[4];


    public RazerHydra[] razerHydras = new RazerHydra[2];

    public RazerHydraManager()
    {
        if (Sixense.LoadLibrary(null)) {
            Sixense.init();
        }

        for (int i = 0; i < 2; i++) {
            razerHydras[i] = new RazerHydra();
        }

        for(int i = 0; i < 4; i++){
            data[i] = new ControllerData();
        }
    }

    public void poll() {

        if(!Sixense.getAllNewestData(data))
        {
            return;
        }

        for (int i = 0; i < numControllers; i++) {
            razerHydras[1- i /* at least for now glove is 1*/].setData(data[i]);
        }
    }

    public void destroy() {
        Sixense.exit();
    }
}