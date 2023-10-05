package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;

public class PulseP extends ColorPainter{
    public PulseP(WrappedArray arr, int mxval, String[] toDisplay) {
        super(arr, mxval, toDisplay);
    }

    double n;

    @Override
    protected void preDraw(Graphics g, double width, double height, int n) {
        this.setBackground(Color.BLACK);
        this.n = n;
    }

    private double pulse(double x) {
        return 0 + 0.05  * Math.exp(-2000*(x-0.05)*(x-0.05))
                 - 0.2   * Math.exp(-1000*(x-0.1)*(x-0.1))
                 + 1     * Math.exp(-900*(x-0.2)*(x-0.2))
                 - 0.75  * Math.exp(-700*(x-0.35)*(x-0.35))
                 + 0.04  * Math.exp(-2000*(x-0.55)*(x-0.55))
                 - 0.15  * Math.exp(-1000*(x-0.6)*(x-0.6))
                 + 0.95  * Math.exp(-900*(x-0.7)*(x-0.7))
                 - 0.7   * Math.exp(-700*(x-0.85)*(x-0.85));
    }

    @Override
    protected void drawForEach(Graphics g, double width, double height, int i, int v, int st) {
        if (st == 0) {
            g.setColor(fromHSV(0.3+0.06*((double)v / n), 1, 1));
        }
        else if (st == 1) {
            g.setColor(fromHSV(0.63+0.06*((double)v / n), 1, 1));
        }
        else {
            g.setColor(fromHSV(0.06*((double)v / n), 1, 1));
        }
        double d = Disparity.cyclic(i,v,(int)n);
        d = 1 - d * d * pulse((double)i / n);
        g.fillRect((int)(width * (double) i / n)-5, (int)(height/2 * d)-5, 10, 10);
    }

    @Override
    protected void afterDraw(Graphics g, double width, double height, int n, int la, int lav) {
        drawText(g, height);
        g.setColor(Color.DARK_GRAY.darker());
        double d = Disparity.cyclic(la,lav,n);
        d = 1 - d * d * pulse((double)la / this.n);
        g.fillRect((int)(width * (double) la / this.n)-5, (int)(height/2 * d)-5, 10, 10);
    }
}
