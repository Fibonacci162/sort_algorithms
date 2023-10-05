package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class ScatterDots extends Painter{
    private double dotDist;
    private double x;

    public ScatterDots(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(new Color(0,30,10));
        x = 0;
        dotDist = width / (double) n;
    }

    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        if (st == 0) {
            g.setColor(Color.WHITE);
        }
        else if (st == 1) {
            g.setColor(Color.GREEN.brighter());
        }
        else {
            g.setColor(Color.RED);
        }
        double dotHeight = height * (double)v / (double)mxval;
        g.fillRect((int)x,(int)(height-dotHeight),10,10);
        x += dotDist;
    }

    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
    }
}
