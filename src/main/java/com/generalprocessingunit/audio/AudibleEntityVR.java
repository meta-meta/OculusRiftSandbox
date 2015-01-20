package com.generalprocessingunit.audio;

import com.generalprocessingunit.io.OSC;
import com.generalprocessingunit.processing.MathsHelpers;

public abstract class AudibleEntityVR extends MathsHelpers {

    /*  http://spatium.ruipenha.pt/renderers/ambi/
    /spatium/#1/aer f1 f2 f3	with #1 being the channel, from 1 to 16, and f1 f2 f3 being, respectively, its azimuth, elevation and radius
    /spatium/#1/azimuth f 	with #1 being the channel, from 1 to 16, and f being the azimuth
    /spatium/#1/elevation f 	with #1 being the channel, from 1 to 16, and f being the elevation
    /spatium/#1/radius f 	with #1 being the channel, from 1 to 16, and f being the radius
    * */

    private static String BASE_MSG = "/spatium/%s";
    private static String AZIMUTH = BASE_MSG + "/azimuth";        // -180 - 180
    private static String ELEVATION = BASE_MSG + "/elevation";    // -90 - 90
    private static String RADIUS = BASE_MSG + "/radius";          // 0.0f - 1.0f
    private static String AER = BASE_MSG + "/aer";

    private float azimuth = 0;
    private float elevation = 0;
    private float radius = 0;

    private void updateSpatiumAmbi(int channel) {
        OSC.sendMsg(String.format(AER, channel), azimuth, elevation, radius);
    }

    protected void setVals(int channel, float azimuth, float elevation, float radius){

        float deg = (450 - degrees(azimuth)) % 360;
        deg = deg < 180 ? deg : -(180 - (deg % 180));

//        System.out.println(
//                "a: " + deg +
//                        " e: " + -map(degrees(elevation), 0, 180, -90, 90) +
//                        " r: " + radius
//        );

        this.azimuth = deg;
        this.elevation = -map(degrees(elevation), 0, 180, -90, 90);
        this.radius = constrain(radius, 0, 1);
        updateSpatiumAmbi(channel);
    }
}
