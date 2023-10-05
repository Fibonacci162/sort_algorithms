package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class ColorCircle extends ColorPainter {

    public ColorCircle(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    protected double rx, ry, theta;

    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLACK);
        rx = 0.47 * width;
        ry = 0.47 * height;
        theta = 2 * Math.PI / (double) n;
    }

    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        g.setColor(fromHSV((double)v / (double) mxval, 1, 1));
        double tx = width/2 + rx*Math.sin((double)i*theta);
        double ty = height/2 - ry*Math.cos((double)i*theta);
        double nx = width/2 + rx*Math.sin((double)(i+1)*theta);
        double ny = height/2 - ry*Math.cos((double)(i+1)*theta);
        g.fillPolygon(new int[]{(int)(width/2), (int)tx, (int)nx}, new int[]{(int)(height/2), (int)ty, (int)ny}, 3);
        if (st == 0) {
            g.setColor(Color.WHITE);
        }
        else if (st == 1) {
            g.setColor(Color.GREEN.brighter());
        }
        else {
            g.setColor(Color.RED);
        }
        g.drawLine((int)tx, (int)ty, (int)nx, (int)ny);
    }

    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
    }
}
