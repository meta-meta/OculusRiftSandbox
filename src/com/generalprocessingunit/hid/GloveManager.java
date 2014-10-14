package com.generalprocessingunit.hid;


import java.util.Map;


public class GloveManager {
    private GloveDevice[] gloveDevices = new GloveDevice[2];

    private RazerHydraManager razerHydraManager = new RazerHydraManager(gloveDevices);

    GloveManagerSerialCom serialCom;

    public GloveManager() {
        for (int i = 0; i < 2; i++) {
            gloveDevices[i] = new GloveDevice(this);
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
                gloveDevices[i < 5 ? 0 : 1].bend[i] = Integer.parseInt(b);
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
    public void vibrate(Map<Integer, Integer> vibrate) {
        StringBuilder sb = new StringBuilder(30).append("V");
        for (int i = 0; i < 14; i++) {
            sb.append(vibrate.containsKey(i) ? String.format("%02d", vibrate.get(i)) : "00");
        }
        sb.append("\n");
        serialCom.write(sb.toString());
    }

    public Hand getLeftHand() {
        return new Hand(gloveDevices[0]);
    }

    public Hand getRightHand() {
        return new Hand(gloveDevices[1]);
    }
}
