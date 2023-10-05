package org.example.painter;

import org.example.main.WrappedArray;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Painter extends JPanel{
    private static final long serialVersionUID = 7148504528832137003L;

    private final WrappedArray arr;
    protected final int mxval;

    protected String[] toDisplay;

    public Painter(WrappedArray arr, int mxval, String[] toDisplay) {
        this.arr = arr;
        this.mxval = mxval;
        this.toDisplay = toDisplay;
    }

    protected abstract void preDraw(Graphics g, double width, double height, int n);

    protected abstract void drawForEach(Graphics g, double width, double height, int i, int v, int st);

    protected abstract void afterDraw(Graphics g, double width, double height, int n, int la, int lav);

    protected void drawText(Graphics g, double height) {
        String word = toDisplay[0];
        int wordh = Math.max(30, (int)height / 15);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, wordh));
        g.setColor(Color.LIGHT_GRAY);
        g.drawChars(word.toCharArray(), 0, word.length(), 5, wordh);
    }

    /**
     * Called by the runtime system whenever the panel needs painting.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int n = arr.size();
        double width = getWidth();
        double height = getHeight();
        preDraw(g, width, height, n);

        for (int i = 0; i<n; i++) {
            int st = arr.state(i);
            int j = arr.rAccess(i);
            drawForEach(g, width, height, i, j, st);
        }
        int la = arr.lAccess();

        int lav = la >= 0 && la < n ? arr.rAccess(la) : 0;
        afterDraw(g, width, height, n, la, lav);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Integer[] arr = new Integer[100];
            for (int i=0; i<100; i++) {
                arr[i] = 1+ThreadLocalRandom.current().nextInt(199);
            }
            var panel = new DefaultPainter(new WrappedArray(1,arr), 200, new String[]{"Test"});
            panel.setBackground(Color.BLACK);
            var frame = new JFrame("A simple graphics program");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
            Thread t = new Thread(() -> {
                Thread th = Thread.currentThread();
                try {
                    while(!th.isInterrupted()) {
                        int i = ThreadLocalRandom.current().nextInt(100);
                        int j = ThreadLocalRandom.current().nextInt(100);
                        int tmp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = tmp;
                        panel.repaint();
                        Thread.sleep(1000);
                    }
                }
                catch (InterruptedException e) {
                    th.interrupt();
                }
            });
            t.start();
        });
    }
}
