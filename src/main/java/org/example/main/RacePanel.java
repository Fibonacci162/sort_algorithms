package org.example.main;

import org.example.painter.Painter;
import org.example.sorter.BubbleS;
import org.example.painter.DefaultTheme;
import org.example.painter.Theme;
import org.example.shuffler.RandomPermutation;
import org.example.shuffler.Shuffler;
import org.example.sorter.InsertionS;
import org.example.sorter.QuickS;
import org.example.sorter.Sorter;
import org.example.sorter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class RacePanel extends JPanel {
    private final List<Sorter> sortList;

    private final int numOfAlgs;

    private final int w;

    private final int h;

    private final Shuffler shuffler;
    private final int n;
    private final int m;

    private final int del;

    private final boolean acc;

    private void shuffle(WrappedArray arr) {
        try {
            shuffler.shuffle(arr, m);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }

    public RacePanel(List<Sorter> sortList, int n, int m, int del, boolean acc, Theme theme, Shuffler shuffler) {
        this.sortList = new ArrayList<>(sortList);
        this.n = n;
        this.m = m;
        this.acc = acc;
        this.del = del;
        this.shuffler = shuffler;
        this.numOfAlgs = sortList.size();
        int sq = (int)Math.ceil(Math.sqrt(numOfAlgs));
        if (numOfAlgs <= (sq-2) * (sq+1)) {
            this.w = sq+1;
            this.h = sq-2;
        }
        else if (numOfAlgs <= sq*(sq-1)) {
            this.w = sq;
            this.h = sq-1;
        }
        else {
            this.w = sq;
            this.h = sq;
        }
        SwingUtilities.invokeLater(() -> {
            Integer[] temp = new Integer[n];
            for (int i=0; i<n; i++) {
                temp[i] = i % m;
            }

            Sorter[][] sorters = new Sorter[h][w];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        sorters[i][j] = sortList.get(i*w + j);
                    }
                }
            }
            WrappedArray[][] arrays = new WrappedArray[h][w];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        arrays[i][j] = new WrappedArray(del, temp);
                    }
                }
            }

            String[][][] strings = new String[h][w][1];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        strings[i][j][0] = "Debug";
                    }
                }
            }

            Painter[][] panels = new Painter[h][w];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        panels[i][j] = theme.painter(arrays[i][j],m,strings[i][j]);
                    }
                }
            }

            Thread[][] painters = new Thread[h][w];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        int finalI = i;
                        int finalJ = j;
                        painters[i][j] = new Thread(() -> {
                            Thread th = Thread.currentThread();
                            try {
                                while(!th.isInterrupted()) {
                                    panels[finalI][finalJ].repaint();
                                    Thread.sleep(33);
                                }
                            }
                            catch (InterruptedException e) {
                                th.interrupt();
                            }
                        });
                    }
                }
            }

            Thread[][] threads = new Thread[h][w];
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        int finalI = i;
                        int finalJ = j;
                        threads[i][j] = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            Sorter sorter = sorters[finalI][finalJ];
                            WrappedArray arr = arrays[finalI][finalJ];
                            arr.cd(Math.max(del/2,1));
                            strings[finalI][finalJ][0] = "Shuffling";
                            shuffle(arr);
                            if(Thread.currentThread().isInterrupted()) {
                                return;
                            }
                            if(acc) {
                                arr.cd(sorter.adjust(del, arr.size()));
                            }
                            else {
                                arr.cd(del);
                            }
                            strings[finalI][finalJ][0] = sorter.toString();
                            sorter.sort(arr);
                            arr.cd(del);
                            arr.check();
                            try {
                                Thread.sleep(5000);
                            }
                            catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            painters[finalI][finalJ].interrupt();
                        });
                    }
                }
            }

            var frame = new JFrame("Sort Visualiser");
            frame.setSize(1600, 900);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout(h, w));
            frame.setBackground(Color.BLACK);
            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        frame.add(panels[i][j], i*w+j);
                    }
                    else {
                        frame.add(new BlackBox(), i*w+j);
                    }
                }
            }
            frame.setVisible(true);

            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        painters[i][j].start();
                    }
                }
            }

            for (int i=0; i<h; i++) {
                for (int j=0; j<w; j++) {
                    if (i*w+j < numOfAlgs) {
                        threads[i][j].start();
                    }
                }
            }

            Thread cleaner = new Thread(() -> {
                try {
                    for (int i=0; i<h; i++) {
                        for (int j=0; j<w; j++) {
                            if (i*w+j < numOfAlgs) {
                                threads[i][j].join();
                            }
                        }
                    }
                }
                catch (InterruptedException ignored) {

                }
                finally {
                    for (int i=0; i<h; i++) {
                        for (int j=0; j<w; j++) {
                            if (i*w+j < numOfAlgs) {
                                if (threads[i][j].isAlive()) {
                                    threads[i][j].interrupt();
                                }
                                if (painters[i][j].isAlive()) {
                                    painters[i][j].interrupt();
                                }
                            }
                        }
                    }
                    frame.dispose();
                }
            });
            cleaner.start();
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    cleaner.interrupt();
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        });
    }

    public static void main(String[] args) {
        List<Sorter> ls = new ArrayList<>();
        ls.add(new BubbleS());
        ls.add(new InsertionS());
        ls.add(new QuickS());
        new RacePanel(ls, 100, 100, 10000, false, new DefaultTheme(), new RandomPermutation());
    }
}
