package com.generalprocessingunit.processing.demos;

import cl.eye.*;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;

public class PS3EyeTest32bitOnly extends PApplet {

    public static void main(String[] args){
        PApplet.main(new String[]{/*ARGS_FULL_SCREEN, ARGS_DISPLAY + "=1",*/ PS3EyeTest32bitOnly.class.getCanonicalName()});
    }

    // Camera Variables
    int numCams;
    CLCamera myCameras[] = new CLCamera[2];
    PImage myImages[] = new PImage[2];
    int cameraWidth = 640;
    int cameraHeight = 480;
    int cameraRate = 60;

    // Animation Variables (not required)
    boolean animate = false;
    float zoomVal, zoomDelta;
    float rotateVal, rotateDelta;

    public void setup(){
//        size(800, 600, OPENGL);

        // Library loading via native interface (JNI)
        // If you see "UnsatisfiedLinkError" then target the library path otherwise leave it commented out.
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


        // Verifies the native library loaded
        if(!setupCameras()) exit();
        // Setups animated variables
        if (animate) setupAnimation();

    }

    public void draw() {
        background(255, 0, 0);
        // Loops through available cameras and updates
        for(int i = 0; i < numCams; i++)
        {
            // ---------------------   (image destination, wait timeout)
            myCameras[i].getCameraFrame(myImages[i].pixels, (i==0) ?  0:0);
            myImages[i].updatePixels();
            image(myImages[i], cameraWidth*i, 0);
        }
        // Updates the animation
        if(animate)updateAnimation();
    }

    boolean setupCameras(){
        println("Getting number of cameras");
        // Checks available cameras
        numCams = CLCamera.cameraCount();
        println("Found " + numCams + " cameras");
        if(numCams == 0)  return false;
        // create cameras and start capture
        for(int i = 0; i < numCams; i++)
        {
            // Prints Unique Identifier per camera
            println("Camera " + (i + 1) + " UUID " + CLCamera.cameraUUID(i));
            // New camera instance per camera
            myCameras[i] = new CLCamera(this);
            // ----------------------(i, CLEYE_GRAYSCALE/COLOR, CLEYE_QVGA/VGA, Framerate)
            myCameras[i].createCamera(i, CLCamera.CLEYE_COLOR_PROCESSED, CLCamera.CLEYE_VGA, cameraRate);
            myCameras[i].setCameraParam(CLCamera.CLEYE_GAIN, 30);
            // Starts camera captures
            myCameras[i].startCamera();
            myImages[i] = createImage(cameraWidth, cameraHeight, RGB);
        }
        // resize the output window
        size(cameraWidth*numCams, cameraHeight);
        println("Complete Initializing Cameras");
        return true;
    }

    void setupAnimation(){
        // General Animation Variables
        zoomVal = 0;
        zoomDelta = TWO_PI/75f;
        rotateVal = 0;
        rotateDelta = TWO_PI/125f;
    }

    void updateAnimation(){
//        myCameras[0].setCameraParam(CLCamera.CLEYE_HKEYSTONE, (int)(150 * sin(rotateVal)));
//        myCameras[0].setCameraParam(CLCamera.CLEYE_VKEYSTONE, (int)(200 * cos(rotateVal)));
        myCameras[0].setCameraParam(CLCamera.CLEYE_LENSCORRECTION1, (int)(75 * sin(rotateVal)));
        if(numCams>1)
        {
            myCameras[1].setCameraParam(CLCamera.CLEYE_ZOOM, (int)(200 * sin(zoomVal)));
        }
        rotateVal += rotateDelta;
        zoomVal += zoomDelta;
    }

    @Override
    public void dispose() {
        // this only gets called when pressing esc
        println("cock");
        for(int i = 0; i < numCams; i++)
        {
            myCameras[i].stopCamera();
        }
        super.dispose();
    }
}






