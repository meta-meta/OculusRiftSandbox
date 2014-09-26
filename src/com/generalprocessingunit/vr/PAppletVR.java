package com.generalprocessingunit.vr;

import com.generalprocessingunit.processing.AxisAngle;
import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.Orientation;
import com.oculusvr.capi.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

import java.awt.event.KeyEvent;

import static com.oculusvr.capi.OvrLibrary.ovrDistortionCaps.*;
import static com.oculusvr.capi.OvrLibrary.ovrHmdCaps.ovrHmdCap_LowPersistence;
import static com.oculusvr.capi.OvrLibrary.ovrHmdType.ovrHmd_DK1;
import static com.oculusvr.capi.OvrLibrary.ovrRenderAPIType.ovrRenderAPI_OpenGL;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Orientation;
import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.ovrTrackingCap_Position;

public abstract class PAppletVR extends PApplet {
    private static final float PIXELS_PER_DISPLAY_PIXEL = 1f;

    private final Hmd hmd;

    private final PGraphicsOpenGL[] views = new PGraphicsOpenGL[2];
    private final EyeRenderDesc eyeRenderDescs[] = new EyeRenderDesc[2];
    private final Texture eyeTextures[] = (Texture[]) new Texture().toArray(2);
    private final FovPort fovPorts[] = (FovPort[]) new FovPort().toArray(2);
    private final Posef[] poses = (Posef[]) new Posef().toArray(2);
    private final PMatrix3D[] projections = new PMatrix3D[2];

    /** Can be thought of as a tank floating in space.
     * The Oculus rift is the homunculus inside.
     * */
    public EuclideanSpaceObject headContainer = new EuclideanSpaceObject();
    public PVector lookat = new PVector();
    public EuclideanSpaceObject head = new EuclideanSpaceObject();

    public int eyeTextureW, eyeTextureH;

    private static Hmd openFirstHmd() {
        Hmd hmd = Hmd.create(0);
        if (null == hmd) {
            hmd = Hmd.createDebug(ovrHmd_DK1);
        }
        return hmd;
    }

    public PAppletVR() {
        Hmd.initialize();

        // I guess this is to make sure we're initialized
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        hmd = openFirstHmd();

        if (0 == hmd.configureTracking(ovrTrackingCap_Orientation | ovrTrackingCap_Position, 0)) {
            throw new IllegalStateException("Unable to start the sensor");
        }
    }

    public static void main(Class clazz){
        Hmd.initialize();
        Hmd hmd = PAppletVR.openFirstHmd();
//        int displayId = hmd.DisplayId; DisplayId is -1  :-(

        //TODO: this is a total hack
        int displayId = Integer.parseInt(hmd.DisplayDeviceName.getString(0).substring(11, 12)) + 1;
        hmd.destroy();

        PApplet.main(new String[]{"--full-screen", "--display=" + displayId, clazz.getCanonicalName()});
    }

//    @Override
//    public void init() {
//        // http://wiki.processing.org/w/Undecorated_frame
//        frame.removeNotify();
//        frame.setUndecorated(true);
//        super.init();
//    }

    @Override
    public void setup() {
        /* Display Window
        * */
        size(hmd.Resolution.w + 2 /*for some reason this keeps it from crashing*/, hmd.Resolution.h, OPENGL);
//        frame.setLocation(hmd.WindowsPos.x, hmd.WindowsPos.y);
//        frame.setLocation(0,0);

        println("DisplayW: " + displayWidth + " DisplayH: " + displayHeight);
        println("hmdW: " + hmd.Resolution.w + " hmdH: " + hmd.Resolution.h);


        for(int eye = 0; eye < 2; ++eye) {
            fovPorts[eye] = hmd.DefaultEyeFov[eye];

            /* Eye Projections
            * */
            float[] M = Hmd.getPerspectiveProjection(fovPorts[eye], 0.001f, 1000000f, true).M;

            PMatrix3D pm3d = new PMatrix3D();
            pm3d.set(M);
            projections[eye] = pm3d;


            /* Eye Textures
            * */
            TextureHeader header = eyeTextures[eye].Header;
            header.API = ovrRenderAPI_OpenGL;
            header.TextureSize = hmd.getFovTextureSize(eye, fovPorts[eye], PIXELS_PER_DISPLAY_PIXEL);
            header.RenderViewport.Size = header.TextureSize;
            header.RenderViewport.Pos = new OvrVector2i(0, 0);

            views[eye] = (PGraphicsOpenGL)createGraphics(header.TextureSize.w, header.TextureSize.h, OPENGL);
            eyeTextures[eye].TextureId = views[eye].getTexture().glName;

            eyeTextureW = views[0].width;
            eyeTextureH = views[0].height;
        }


        /* Setup OVR Rendering Distortion
        * */
        RenderAPIConfig rc = new RenderAPIConfig();
        rc.Header.RTSize = hmd.Resolution;
        rc.Header.Multisample = 1;

        int distortionCaps = ovrDistortionCap_Chromatic | ovrDistortionCap_TimeWarp | ovrDistortionCap_Vignette;
        EyeRenderDesc[] ERDs = hmd.configureRendering(rc, distortionCaps, fovPorts);
        eyeRenderDescs[0] = ERDs[0];
        eyeRenderDescs[1] = ERDs[1];

        // Processing defaults to 60
        frameRate(75);
        hmd.setEnabledCaps(hmd.getEnabledCaps() | OvrLibrary.ovrHmdCaps.ovrHmdCap_NoVSync);
    }

    @Override
    public final void draw() {
        updateState();

        hmd.beginFrame(frameCount);

        for(int i = 0; i < 2; ++i) {

            int eye = hmd.EyeRenderOrder[i];

            /* Get Sensor Data
            * */
            Posef pose = hmd.getEyePose(eye);
            poses[eye].Orientation = pose.Orientation;
            poses[eye].Position = pose.Position;



            /* Location Vector
            * */
            float s = .5f; // scale down a bit
             PVector eyeLocation = new PVector(
                    - pose.Position.x * s,
                    + pose.Position.y * s,
                    - pose.Position.z * s
            );


            /* Head Location Object
            * */
            PVector headLocation = eyeLocation.get();
            headLocation.x = -headLocation.x;
            head.setLocation(headLocation);
            head.setOrientation(new Orientation());
            headContainer.translateAndRotateObjectWRTObjectCoords(head);


            /* Lookat Vector
            * */
            PMatrix3D rotationMatrix = OvrQuatToPMatrix3D(pose.Orientation);

            PVector lookat = new PVector(0, 0, 1);
            rotationMatrix.mult(lookat, lookat).normalize(lookat);
            lookat.y = -lookat.y;

            this.lookat = lookat.get();
            this.lookat.x = -this.lookat.x;

            lookat.add(eyeLocation);


            /* Up Vector
            * */
            PVector up = new PVector(0, 1, 0);
            rotationMatrix.mult(up, up).normalize(up);
            up.x = -up.x;
            up.z = -up.z;

//            println("H: " + head + " L: " + this.lookat);
//            println("E: " + eyeLocation + " L: " + lookat + " U: " + up);

            /* View Adjust  ????
                documentation reads: How many display pixels will fit in tan(angle) = 1.
            * */
//            OvrVector3f trans = eyeRenderDescs[eye].ViewAdjust;
//            views[eye].translate(trans.x, trans.y, trans.z);



            /* Draw Eye
            * */
            views[eye].beginDraw();

            drawViewPreCamera(eye, views[eye]);

            views[eye].setProjection(projections[eye]);

            /* It may end up being faster directly applying transform matrices instead of using camera
            * */
            views[eye].camera(
                    eyeLocation.x, eyeLocation.y, eyeLocation.z,
                    lookat.x, lookat.y, lookat.z,
                    up.x, up.y, up.z
            );

            // weird stuff is going on with camera and coordinate system. this flips the image
            views[eye].scale(-1, 1);

            views[eye].pushMatrix();
                AxisAngle aa = headContainer.getAxisAngle();
                views[eye].rotate(-aa.w, aa.x, aa.y, aa.z); // -w because we want to rotate the opposite way
                views[eye].translate(-headContainer.x(), -headContainer.y(), -headContainer.z());
                drawView(eye, views[eye]);
            views[eye].popMatrix();

            drawHeadContainerView(eye, views[eye]);

            views[eye].endDraw();
        }

        hmd.endFrame(poses, eyeTextures);

//        println(frameRate);
    }


    /**
     * Adapted from org.saintandreas.math.Quaternion
     * thanks Mark Powell and Joshua Slack
     * */
    private PMatrix3D OvrQuatToPMatrix3D(OvrQuaternionf q) {
        float norm = sq(q.w) + sq(q.x) + sq(q.y) + sq(q.z);

        // we explicitly test norm against one here, saving a division
        // at the cost of a test and branch. Is it worth it?
        float s = (norm == 1f) ? 2f : (norm > 0f) ? 2f / norm : 0;

        // compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
        // will be used 2-4 times each.
        float xs = q.x * s;
        float ys = q.y * s;
        float zs = q.z * s;
        float xx = q.x * xs;
        float xy = q.x * ys;
        float xz = q.x * zs;
        float xw = q.w * xs;
        float yy = q.y * ys;
        float yz = q.y * zs;
        float yw = q.w * ys;
        float zz = q.z * zs;
        float zw = q.w * zs;

        // using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here
        return new PMatrix3D( //
                1 - (yy + zz), (xy - zw), (xz + yw), 0,
                (xy + zw), 1 - (xx + zz), (yz - xw), 0,
                (xz - yw), (yz + xw), 1 - (xx + yy), 0,
                0, 0, 0, 1);
    }

    /**
     * Provides a way to render stable landmarks that are relative to Rift head tracking.
     * Appropriate for drawing a spaceship interior. The user can look around and move inside.
     *
     * called twice per draw cycle
     * */
    protected void drawHeadContainerView(int eye, PGraphics pG){}

    /**
     * For drawing the outside world. Location and orientation of headContainer are accounted for.
     *
     * called twice per draw cycle
     * */
    protected abstract void drawView(int eye, PGraphics pG);


    protected void drawViewPreCamera(int eye, PGraphics pG){}

    /**
     * Use this to update game logic.
     *
     * called once per draw cycle
     * */
    protected abstract void updateState();

    @Override
    public void keyPressed(KeyEvent e) {
        if (0 != hmd.getHSWDisplayState().Displayed) {
            hmd.dismissHSWDisplay();
            return;
        }

        if(e.getKeyChar() == 'p') {
            /* Low Persistence
            * */
            int caps = hmd.getEnabledCaps();
            if (0 != (caps & ovrHmdCap_LowPersistence)) {
                hmd.setEnabledCaps(caps & ~ovrHmdCap_LowPersistence);
            } else {
                hmd.setEnabledCaps(caps | ovrHmdCap_LowPersistence);
            }
        }

        if(e.getKeyChar() == 'v') {
            /* VSYNC
            * */
            int caps = hmd.getEnabledCaps();
            if (0 != (caps & OvrLibrary.ovrHmdCaps.ovrHmdCap_NoVSync)) {
                hmd.setEnabledCaps(caps & ~OvrLibrary.ovrHmdCaps.ovrHmdCap_NoVSync);
            } else {
                hmd.setEnabledCaps(caps | OvrLibrary.ovrHmdCaps.ovrHmdCap_NoVSync);
            }
        }


        if(e.getKeyChar() == 't') {
            /* Dynamic Prediction   Timewarp?
            * */
            int caps = hmd.getEnabledCaps();
            if (0 != (caps & OvrLibrary.ovrHmdCaps.ovrHmdCap_DynamicPrediction)) {
                hmd.setEnabledCaps(caps & ~OvrLibrary.ovrHmdCaps.ovrHmdCap_DynamicPrediction);
            } else {
                hmd.setEnabledCaps(caps | OvrLibrary.ovrHmdCaps.ovrHmdCap_DynamicPrediction);
            }
        }

        super.keyPressed(e);
    }

    @Override
    public void destroy() {
        hmd.destroy();
        Hmd.shutdown();
        super.destroy();
    }


    public static void println(Object o) {
        System.out.println(o);
    }



    /* // this may or may not be a good idea

    public void pollHardware() {}


    private int pollMillis = 1;
    private boolean running = false;
    private Runnable pollThread = new Thread() {
        public void run() {
            try {
                while(running) {
                    pollHardware();
                    Thread.sleep(pollMillis);
                }
            } catch (InterruptedException e) { }
        }
    };*/
}
