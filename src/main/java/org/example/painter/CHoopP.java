package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class CHoopP extends ColorPainter {
    public CHoopP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    double n;

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLACK);
        this.n = (double)n;
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
        double j = Math.pow((double)(i+1) / (n+10.), 0.9);
        double xoffset = width * 0.5 * (1. - j);
        double yoffset = height * 0.5 * (1. - j);
        g.drawOval((int)xoffset, (int)yoffset, (int)(width*j), (int)(height*j));
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
        g.setColor(Color.BLACK);
        double j = Math.pow((double)(la+1) / (n+10.), 0.9);
        double xoffset = width * 0.5 * (1. - j);
        double yoffset = height * 0.5 * (1. - j);
        g.drawOval((int)xoffset, (int)yoffset, (int)(width*j), (int)(height*j));
    }


}
