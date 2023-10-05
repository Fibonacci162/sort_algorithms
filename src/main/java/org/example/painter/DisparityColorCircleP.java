package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class DisparityColorCircleP extends ColorCircle{

    public DisparityColorCircleP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    protected int n;

    protected void preDraw(Graphics g, double width, double height, int n) {
        super.preDraw(g,width,height,n);
        this.n = n;
    }

    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        g.setColor(fromHSV((double)v / (double) mxval, 1, 1));
        double d = Disparity.cyclic(i, v, n);
        double tx = width/2 + d*rx*Math.sin((double)i*theta);
        double ty = height/2 - d*ry*Math.cos((double)i*theta);
        double nx = width/2 + d*rx*Math.sin((double)(i+1)*theta);
        double ny = height/2 - d*ry*Math.cos((double)(i+1)*theta);
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
}
