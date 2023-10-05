package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class WhiteDotCircleP extends Painter{
    public WhiteDotCircleP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    private double n;

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLACK);
        this.n = n;
    }

    @Override
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
        double thetaX = 2 * Math.PI * (double) i / n;
        double thetaY = 2 * Math.PI * (double) v / (double)mxval;
        double x = width / 2 + 0.49 * width * Math.sin(thetaX);
        double y = height / 2 - 0.49 * height * Math.cos(thetaY);
        g.fillRect((int)x-5, (int)y-5, 10, 10);
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        g.setColor(Color.CYAN);
        double thetaX = 2 * Math.PI * (double) la / n;
        double thetaY = 2 * Math.PI * (double) lav / (double)mxval;
        double x = width / 2 + 0.49 * width * Math.sin(thetaX);
        double y = height / 2 - 0.49 * height * Math.cos(thetaY);
        g.fillOval((int)x-5, (int)y-5, 10, 10);
        drawText(g, height);
    }
}
