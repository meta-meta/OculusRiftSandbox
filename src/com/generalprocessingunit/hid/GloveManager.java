package com.generalprocessingunit.hid;


import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.YawPitchRoll;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import processing.core.PVector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;


public class GloveManager {
    private Glove[] gloves = new Glove[2];

    private RazerHydraManager razerHydraManager = new RazerHydraManager(gloves);

    GloveManagerSerialCom serialCom;

    public GloveManager() {
        for (int i = 0; i < 2; i++) {
            gloves[i] = new Glove(this);
        }

        serialCom = new GloveManagerSerialCom(this);
    }

    public synchronized void destroy() {
        serialCom.destroy();
        razerHydraManager.destroy();
    }

    public void poll() {
        razerHydraManager.poll();
    }

    protected void updateBendState(String s){
        int i = 0;
        for (String b : s.split(",")) {
            try {
                gloves[i < 5 ? 0 : 1].bend[i] = Integer.parseInt(b);
            } catch (Exception e) {
                //no-op
            }

            i++;

            if (i > 10) {
                break;
            }
        }
    }

    /* note: vibration patterns last for ~350 millis */
    public void setVibrate(Map<Integer, Integer> vibrate) {
        StringBuilder sb = new StringBuilder(30).append("V");
        for (int i = 0; i < 14; i++) {
            sb.append(vibrate.containsKey(i) ? String.format("%02d", vibrate.get(i)) : "00");
        }
        sb.append("\n");
        serialCom.write(sb.toString());
    }

    public LeftHand getLeftHand() {
        return new LeftHand();
    }

    public RightHand getRightHand() {
      return new RightHand();
    }

    public class LeftHand extends Hand {
        LeftHand(){
            super(gloves[0]);
        }
    }

    public class RightHand extends Hand {
        RightHand(){
            super(gloves[1]);
        }
    }







}
