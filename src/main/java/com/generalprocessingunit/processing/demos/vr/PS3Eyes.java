package com.generalprocessingunit.processing.demos.vr;


import cl.eye.CLCamera;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;

public class PS3Eyes {
    private PImage myImages[] = new PImage[2];
    private CLCamera myCameras[] = new CLCamera[2];
    private int numCams;
    private int cameraWidth = 640;
    private int cameraHeight = 480;
    private int cameraRate = 75;

    private boolean gotCameras = false;

    public PS3Eyes(PApplet p5) {
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
            path += "/dll/ps3Eye/CLEyeMulticam.dll";
            println(path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CLCamera.loadLibrary(path);

        gotCameras = setupCameras(p5);
    }

    private boolean setupCameras(PApplet p5){
        println("Getting number of cameras");
        // Checks available cameras
        numCams = CLCamera.cameraCount();
        println("Found " + numCams + " cameras");
        if(numCams == 0) return false;
        // create cameras and start capture

        for(int i = 0; i < numCams; i++)
        {
            // Prints Unique Identifier per camera
            println("Camera " + (i + 1) + " UUID " + CLCamera.cameraUUID(i));
            // New camera instance per camera
            myCameras[i] = new CLCamera(p5);
            // ----------------------(i, CLEYE_GRAYSCALE/COLOR, CLEYE_QVGA/VGA, Framerate)
            myCameras[i].createCamera(i, CLCamera.CLEYE_COLOR_PROCESSED, CLCamera.CLEYE_VGA, cameraRate);
            myCameras[i].setCameraParam(CLCamera.CLEYE_GAIN, 30);
            // Starts camera captures
            myCameras[i].startCamera();
            myImages[i] = p5.createImage(cameraWidth, cameraHeight, PApplet.RGB);
        }

        return true;
    }

    public PImage getImage(int eye) {
        myCameras[eye].getCameraFrame(myImages[eye].pixels, eye == 0 ? 1000 : 0);
        myImages[eye].updatePixels();
        return myImages[eye];
    }

    public void dispose() {
        for (int i = 0; i < numCams; i++) {
            myCameras[i].stopCamera();
        }
    }

    static void println(Object o) {System.out.println(o);}

}
