package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class ColorBars extends ColorPainter{
    public ColorBars(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    private double barDist;
    private double x;
    private double barWidth;

    protected void preDraw(Graphics g, double width, double height, int n) {
        x = 0;
        barDist = width / (double) n;
        barWidth = Math.ceil(width / (double) n);
    }

    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        double h = 0.9*(double)v / (double)mxval;
        if (st == 0) {
            g.setColor(fromHSV(h, 1, 1));
        }
        else if (st == 1) {
            g.setColor(fromHSV(h, 0.8, 1));
        }
        else {
            g.setColor(fromHSV(h, 0.7, 0.5));
        }
        g.fillRect((int)x,0,(int)(barWidth),(int)height);
        g.drawLine((int)x,0,(int)x,(int)height);
        x += barDist;
    }

    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        g.setColor(Color.BLACK);
        g.fillOval((int)((double)la*barDist), (int)(height/2), (int)barWidth+1, (int) barWidth+1);
        String word = toDisplay[0];
        int wordh = Math.max(30, (int)height / 15);
        int wordw = 10 + 3 * wordh * word.length() / 5;
        g.setColor(new Color(0,0,0,128));
        g.fillRect(0,0, wordw, 4*wordh/3);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, wordh));
        g.setColor(Color.LIGHT_GRAY);
        g.drawChars(word.toCharArray(), 0, word.length(), 5, wordh);
    }
}
