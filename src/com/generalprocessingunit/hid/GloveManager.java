package com.generalprocessingunit.hid;


import com.generalprocessingunit.processing.AxisAngle;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import processing.core.PVector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;


public class GloveManager implements SerialPortEventListener  {
    public Glove[] gloves = new Glove[2];

    private RazerHydraManager razerHydraManager = new RazerHydraManager();

    public GloveManager() {
        for (int i = 0; i < 2; i++) {
            gloves[i] = new Glove();
//            gloves[i].setBaseIndex(i);
        }
    }

    SerialPort serialPort;

    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows my desktop
            "COM4"  // Windows my laptop
    };

    private BufferedReader input;
    private OutputStream output;

    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;

    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 115200;

    public void init() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void destroy() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }

        razerHydraManager.destroy();
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                if (inputLine.startsWith("B")) {
                    updateState(inputLine.substring(1));
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    private void updateState(String s){
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

        // TODO: this doesn't really belong here but at least it is not in the draw loop
        razerHydraManager.poll();
        for (int j = 0; j < 2; j++) {
            gloves[j].setLocation(razerHydraManager.razerHydras[j].getLocation());
            gloves[j].setOrientation(razerHydraManager.razerHydras[j].getOrientation());
        }
    }

    public void setVibrate(Map<Integer, Integer> vibrate) {
        StringBuilder sb = new StringBuilder(30).append("V");
        for (int i = 0; i < 14; i++) {
            sb.append(vibrate.containsKey(i) ? String.format("%02d", vibrate.get(i)) : "00");
        }
        sb.append("\n");
        try{
            output.write(sb.toString().getBytes());
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
    }

//    public PVector getGlovePosition(int gloveIndex){
//        // TODO: this might switch depending on the position of the glove relative to the base station
//        /* 0 means left, 1 means right but due to the orientation of the base station to the hands,
//         * they are reversed */
//        return razerHydraManager.position[gloveIndex == 0 ? 1 : 0 ];
//    }

//    public PVector getGloveRotation(int gloveIndex){
//        return razerHydraManager.rotation[gloveIndex == 0 ? 1 : 0];
//    }
//    public float getGloveRotZ(int gloveIndex){
//        return razerHydraManager.rotation[gloveIndex == 0 ? 1 : 0 ].z;
//    }
//    public float getGloveRotX(int gloveIndex){
//        return razerHydraManager.rotation[gloveIndex == 0 ? 1 : 0 ].x;
//    }
//    public float getGloveRotY(int gloveIndex){
//        return razerHydraManager.rotation[gloveIndex == 0 ? 1 : 0 ].y;
//    }

    public static void main(String[] args) throws Exception {
        GloveManager main = new GloveManager();
        main.init();
        Thread t=new Thread() {
            public void run() {
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
            }
        };
        t.start();
        System.out.println("Started");


        while(true){
            for(int i=0; i < 5; i++){
                System.out.print(main.gloves[0].bend[i]);
                System.out.print(i<4?",":"\n");
            }

            //Thread.sleep(50);
        }

//        for(int i = 0; i < 50; i++)
//        {
//            Thread.sleep(2000);
//            System.out.println("sending");
//            main.output.write("V1001330200030004000500060007\n".getBytes());
//        }
    }

    public LeftHand getLeftHand() {
        return new LeftHand(this);
    }

    public RightHand getRightHand() {
      return new RightHand(this);
    }

    public static class LeftHand extends Hand{
        LeftHand(GloveManager gloveManager){
            super(gloveManager, 0);
        }
    }


    public static class RightHand extends Hand{
        RightHand(GloveManager gloveManager){
            super(gloveManager, 1);
        }
    }


    public static class Hand {
        public GloveManager gloveManager;
        private int gloveIndex;

        public Finger thumb;
        public Finger index;
        public Finger middle;
        public Finger ring;
        public Finger pinky;

        public Fingers fingers;
        public Fingertips fingertips;

        public Hand(GloveManager gloveManager, int gloveIndex){
            this.gloveManager = gloveManager;
            this.gloveIndex = gloveIndex;

            int zeroIndexBend = gloveIndex * 5;
            int zeroIndexVibrate = gloveIndex * 14;

            thumb = new Finger(gloveManager, zeroIndexBend);
            index = new Finger(gloveManager, zeroIndexBend + 1);
            middle = new Finger(gloveManager, zeroIndexBend + 2);
            ring = new Finger(gloveManager, zeroIndexBend + 3);
            pinky = new Finger(gloveManager, zeroIndexBend + 4);

            fingers = new Fingers(Arrays.asList(thumb, index, middle, ring, pinky), gloveManager) ;
            fingertips = new Fingertips(Arrays.asList(thumb, index, middle, ring, pinky), gloveManager) ;
        }

        public PVector getLocation() {
            return gloveManager.gloves[gloveIndex].getLocation();
        }

        public AxisAngle getAxisAngle() {
            return gloveManager.gloves[gloveIndex].getAxisAngle();
        }
    }


    public static class Part {
        public GloveManager gloveManager;

        public Part(GloveManager gloveManager){
            this.gloveManager = gloveManager;
        }
    }

    public static class Finger extends Part {
        public int fingerIndex;

        public Finger(GloveManager gloveManager, int fingerIndex){
            super(gloveManager);
            this.fingerIndex = fingerIndex;
        }

        public int getBend(){
            return gloveManager.gloves[fingerIndex < 5 ? 0 : 1].getBend(fingerIndex % 5); //TODO: this obviously needs to be refactored to have a reference to the glove
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            m.put(fingerIndex, intensity);       //fingertip vibrators
            m.put(fingerIndex + 5, intensity);   //knuckle vibrators
            this.gloveManager.setVibrate(m);
        }
    }


    public static class Fingers extends ArrayList<Finger> {
        private GloveManager glove;

        public Fingers(List<Finger> fingerList, GloveManager glove){
            super(fingerList);
            this.glove = glove;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            for(Finger finger : this){
                m.put(finger.fingerIndex, intensity);      //fingertip vibrators
                m.put(finger.fingerIndex + 5, intensity);  //knuckle vibrators
            }

            glove.setVibrate(m);
        }
    }

    public static class Fingertips extends ArrayList<Finger> {
        private GloveManager glove;

        public Fingertips(List<Finger> fingerList, GloveManager glove){
            super(fingerList);
            this.glove = glove;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            for(Finger finger : this){
                m.put(finger.fingerIndex, intensity);      //fingertip vibrators
            }

            glove.setVibrate(m);
        }
    }



    public static class Palm extends Part {
        private int zeroIndex;

        public Palm(GloveManager glove, int zeroIndex){
            super(glove);
            this.zeroIndex = zeroIndex;
        }

        public void vibrate(int intensity/*, int millis*/){
            Map m = new HashMap<Integer, Integer>();
            m.put(zeroIndex + 10, intensity);       //index-middle
            m.put(zeroIndex + 11, intensity);   //ring-pinky
            m.put(zeroIndex + 12, intensity);       //thumb
            m.put(zeroIndex + 13, intensity);   //outer corner
            gloveManager.setVibrate(m);
        }
    }

}
