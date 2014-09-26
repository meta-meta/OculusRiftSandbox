package com.generalprocessingunit.hid;

import com.generalprocessingunit.processing.EuclideanSpaceObject;
import com.generalprocessingunit.processing.Orientation;
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

    private float prevYaw, prevPitch, prevRoll;
    protected void setData(ControllerData data){
        int i = inverted ? -1 : 1;

        rawLocation.set(
                data.pos[0] * -.0004f * i,
                data.pos[1] * -.0004f * i,
                data.pos[2] * .0004f * i
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

        setOrientation(data.yaw, data.pitch + PI, data.roll);


//        yaw(data.yaw - prevYaw);
//        pitch(data.pitch - prevPitch);
//        roll(-data.roll - prevRoll);
//
//        prevYaw = data.yaw;
//        prevPitch = data.pitch;
//        prevRoll = -data.roll;

    }

    public boolean isGrabbing() {
        return trigger > 0;
    }
}
