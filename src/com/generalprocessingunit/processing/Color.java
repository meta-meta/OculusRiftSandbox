package com.generalprocessingunit.processing;

public class Color {
	public int R;
	public int G;
	public int B;
	public int A;

	public Color(int hex) {
		this(hex, 255);
	}

    public Color(int hex, int alpha) {
		this((hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, (hex & 0xFF), alpha);
	}

    public Color(int red, int green, int blue) {
		this(red, green, blue, 255);
	}

    public Color(int red, int green, int blue, int alpha) {
		R = red;
		G = green;
		B = blue;
		A = alpha;
	}

    // TODO: not sure if this works or if Processing's fill(int rgb) is not recognizing it
    public int get() {
        return (A << 24) | (R << 16) | (G << 8) | B;
    }

    public static Color grey(int luminance) {
        return new Color(luminance, luminance, luminance);
    }

    public static Color greyA(int luminance, int alpha) {
        return new Color(luminance, luminance, luminance, alpha);
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }

    public static Color rgba(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }
}
