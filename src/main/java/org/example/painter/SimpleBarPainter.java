package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class SimpleBarPainter extends DefaultPainter{
    public SimpleBarPainter(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        super.preDraw(g,width,height,n);
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        super.drawForEach(g,width,height,i,v,0);
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {

    }
}
