package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;
import java.util.ArrayList;

public class DiametersP extends Painter{
    public DiametersP(WrappedArray arr, int mxval, String[] toDisplay, double offset) {
        super(arr, mxval, toDisplay);
        this.offset = offset;
    }

    private final ArrayList<Integer> iPointX = new ArrayList<>();
    private final ArrayList<Integer> vPointX = new ArrayList<>();
    private final ArrayList<Integer> iPointY = new ArrayList<>();
    private final ArrayList<Integer> vPointY = new ArrayList<>();

    private int pn = 0;
    private double pw = 0;
    private double ph = 0;

    private final double offset;

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLUE.darker().darker());
        double r = 0.49 * Math.min(width, height);
        double iDTheta = 2 * Math.PI / (double)n;
        double vDTheta = 2 * Math.PI / (double)mxval;
        g.setColor(Color.BLUE.darker().darker().darker());
        g.fillOval((int)(width/2-r), (int)(height/2-r),(int)(2*r),(int)(2*r));
        g.setColor(Color.WHITE);
        g.drawOval((int)(width/2-r), (int)(height/2-r),(int)(2*r),(int)(2*r));
        if (n != pn || width != pw || height != ph) {
            iPointX.clear();
            iPointY.clear();
            vPointX.clear();
            vPointY.clear();
            for(int i=0; i<n; i++) {
                double theta = (double) i * iDTheta;
                iPointX.add((int)(width/2 + r * Math.sin(theta)));
                iPointY.add((int)(height/2 + r * Math.cos(theta)));
            }
            for(int i=0; i<=mxval; i++) {
                double theta = offset + (double) i * vDTheta;
                vPointX.add((int)(width/2 + r * Math.sin(theta)));
                vPointY.add((int)(height/2 + r * Math.cos(theta)));
            }
            pn = n;
            pw = width;
            ph = height;
        }
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
        g.drawLine(iPointX.get(i), iPointY.get(i), vPointX.get(Math.min(v, mxval)), vPointY.get(Math.min(v, mxval)));
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
    }
}
