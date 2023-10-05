package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class DefaultPainter extends Painter {

    public DefaultPainter(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    private double x;

    private double barWidth;

    private double barDist;

    private static final Color bgcl = new Color(0,5,30);

    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(bgcl);
        x = 0;
        barDist = width / (double) n;
        barWidth = Math.ceil(width / (double) n);
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
        double barHeight = height * (double)v / (double)mxval;
        g.fillRect((int)x,(int)(height-barHeight),(int)(barWidth),(int)barHeight);
        g.drawLine((int)x,(int)height,(int)x,(int)(height-barHeight));
        x += barDist;
    }

    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        g.setColor(Color.CYAN);
        g.fillOval((int)((double)la*barDist), (int)(barWidth/2), (int)barWidth+1, (int) barWidth+1);
        drawText(g, height);
    }
}
