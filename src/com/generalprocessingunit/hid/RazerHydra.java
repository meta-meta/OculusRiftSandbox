package com.generalprocessingunit.hid;

import com.generalprocessingunit.processing.space.EuclideanSpaceObject;
import com.generalprocessingunit.processing.space.Orientation;
import com.generalprocessingunit.processing.space.YawPitchRoll;
import com.sixense.ControllerData;
import com.sixense.EnumButton;
import processing.core.PVector;

public class RazerHydra extends EuclideanSpaceObject {
    public PVector locationAtGrab;
    public boolean btn1;
    public boolean btn2;
    public boolean btn3;
    public boolean btn4;
    public boolean btnStart;
    public boolean btnBumper;

    public boolean btnJoystick;
    public float joyX;
    public float joyY;

    public float trigger;

    public boolean inverted = false;

    private PVector origin = new PVector();

    private PVector rawLocation = new PVector();


    public void reset(PVector landmark) {
        origin.set(PVector.add(rawLocation, landmark));
    }

    public void toggleInvertedLocation() {
        inverted = !inverted;
    }

    public YawPitchRoll rotation = new YawPitchRoll();
    private float prevYaw, prevPitch, prevRoll;
    private long millisAtLastPoll = 0;
    private int millisSinceLastPoll = 0;

    protected void setData(ControllerData data){
        int i = inverted ? -1 : 1;

        rawLocation.set(
                data.pos[0] * -.001f * i,
                data.pos[1] * -.001f * i,
                data.pos[2] * .001f * i
        );

        setLocation(PVector.sub(origin, rawLocation));

        btn1 =          (data.buttons & EnumButton.BUTTON_1.mask()) == 1;
        btn2 =          (data.buttons & EnumButton.BUTTON_2.mask()) == 1;
        btn3 =          (data.buttons & EnumButton.BUTTON_3.mask()) == 1;
        btn4 =          (data.buttons & EnumButton.BUTTON_4.mask()) == 1;
        btnStart =      (data.buttons & EnumButton.START.mask()) == 1;
        btnBumper =     (data.buttons & EnumButton.BUMPER.mask()) == 1;
        btnJoystick =   (data.buttons & EnumButton.JOYSTICK.mask()) == 1;

        joyX = data.joystick_x;
        joyY = data.joystick_y;

        if( !isGrabbing() && data.trigger > 0 ) {
            locationAtGrab = getLocation();
        }

        trigger = data.trigger;


        prevYaw = rotation.yaw();
        prevPitch = rotation.pitch();
        prevRoll = rotation.roll();

        rotation.set(data.yaw, data.pitch + PI, data.roll);
        setOrientation(rotation);

        long millisNow = System.currentTimeMillis();
        millisSinceLastPoll = (int)(millisNow - millisAtLastPoll);
        millisAtLastPoll = millisNow;
    }

    public int millisSinceLastPoll() {
        return millisSinceLastPoll;
    }

    public YawPitchRoll deltaYawPitchRoll() {
        return new YawPitchRoll(
                rotation.yaw() - prevYaw,
                rotation.pitch() - prevPitch,
                rotation.roll() - prevRoll
        );
    }

    public boolean isGrabbing() {
        return trigger > 0;
    }
}
