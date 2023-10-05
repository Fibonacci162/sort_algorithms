package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class HyperSpiralP extends SpiralP{
    public HyperSpiralP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    @Override
    protected void drawSpiralSegment(Graphics g, double cx, double cy, double xr, double yr, double n, double i) {
        int spirals = 100;
        double theta = (double) spirals * 2 * Math.PI * i / n;
        double dtheta = 2 * Math.PI / n;
        double dr = (1. / n) / (double) spirals;
        double r = i / n;
        double px1 = cx + r * xr * Math.cos(theta);
        double py1 = cy - r * yr * Math.sin(theta);
        for (int j=0; j<spirals; j++) {
            r += dr;
            double px2 = cx + r * xr * Math.cos(theta + dtheta);
            double py2 = cy - r * yr * Math.sin(theta + dtheta);
            g.drawLine((int)px1, (int)py1, (int)px2, (int)py2);
            dtheta += 2 * Math.PI / n;
            px1 = px2;
            py1 = py2;
        }
    }
}
