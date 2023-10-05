package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class SpiralP extends ColorPainter{
    public SpiralP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    private double n;

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLACK);
        this.n = (double)n;
    }

    protected void drawSpiralSegment(Graphics g, double cx, double cy, double xr, double yr, double n, double i) {
        double theta = 4 * Math.PI * i / n;
        double dtheta = 4 * Math.PI / n;
        double r = i / n;
        double px1 = cx + r * xr * Math.cos(theta);
        double py1 = cy - r * yr * Math.sin(theta);
        r = (i + 1.) / n;
        double px2 = cx + r * xr * Math.cos(theta + dtheta);
        double py2 = cy - r * yr * Math.sin(theta + dtheta);
        g.drawLine((int)px1, (int)py1, (int)px2, (int)py2);
    }

    @Override
    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        if (st == 0) {
            g.setColor(fromHSV((double)v / (double)mxval, 1, 1));
        }
        else if (st == 1) {
            g.setColor(fromHSV((double)v / (double)mxval, 0.9, 1));
        }
        else {
            g.setColor(fromHSV((double)v / (double)mxval, 0.9, 0.5));
        }
        drawSpiralSegment(g, width/2, height/2, width * 0.45, height * 0.45, n, (double) i);
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
        g.setColor(Color.BLACK);
        drawSpiralSegment(g, width/2, height/2, width * 0.45, height * 0.45, n, (double) la);
    }
}
