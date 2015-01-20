//package com.generalprocessingunit.processing.demos;
//
//import com.generalprocessingunit.processing.PAppletBuffered;
//import processing.core.PApplet;
//import processing.core.PGraphics;
//import processing.video.Movie;
//
//// NOTE:   add this directory to dependencies for processing-video  processing\java\libraries\video\library\windows64
////TODO: perhaps this will help one day  http://www.sjkingston.com/blog/gstreamer-on-windows/
////TODO: check this too http://docs.gstreamer.com/display/GstSDK/Windows+deployment
//// setting GST_PLUGIN_PATH : processing\java\libraries\video\library\windows64\plugins  environment variable in intellij edit configurations got a step closer
//
//
//public class VideoTest extends PAppletBuffered {
//    public static void main(String[] args){
//        PApplet.main(new String[]{/*"--full-screen", "--display=1",*/ VideoTest.class.getCanonicalName()});
//        // if fullscreen works, --display=hmd.DisplayId
//    }
//
//    Movie myMovie;
//
//    @Override
//    public void setup() {
//        size(1280, 720, OPENGL);
////        size(displayWidth, displayHeight, OPENGL);
//        super.setup();
//
//        myMovie = new Movie(this, "P1120161.MOV");
//        myMovie.loop();
//    }
//
//    @Override
//    public void draw(PGraphics pG) {
//        background(127);
//        image(myMovie, 0, 0);
//    }
//
//    // Called every time a new frame is available to read
//    // called by Movie class via reflection
//    void movieEvent(Movie m) {
//        m.read();
//    }
//
//}
