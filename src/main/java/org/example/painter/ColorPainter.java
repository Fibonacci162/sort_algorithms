package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public abstract class ColorPainter extends Painter{

    public ColorPainter(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    protected Color fromHSV(double h, double s, double v) {
        double c = s*v;
        h *= 6;
        double x = c * (1 - Math.abs(h - 2 * Math.floor(h/2)-1));
        double r,g,b;
        if (h < 1) {
            r = c;
            g = x;
            b = 0;
        }
        else if (h < 2) {
            r = x;
            g = c;
            b = 0;
        }
        else if (h < 3) {
            r = 0;
            g = c;
            b = x;
        }
        else if (h < 4) {
            r = 0;
            g = x;
            b = c;
        }
        else if (h < 5) {
            r = x;
            g = 0;
            b = c;
        }
        else {
            r = c;
            g = 0;
            b = x;
        }
        double m = v - c;
        r = Math.min(1, r+m);
        g = Math.min(1, g+m);
        b = Math.min(1, b+m);
        return new Color((float)r,(float)g,(float)b);
    }

}
