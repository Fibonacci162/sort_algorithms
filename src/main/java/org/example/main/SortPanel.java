package org.example.main;

import org.example.painter.DefaultTheme;
import org.example.painter.Theme;
import org.example.shuffler.RandomPermutation;
import org.example.sorter.BubbleS;
import org.example.sorter.Sorter;
import org.example.shuffler.Shuffler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class SortPanel  extends JPanel {
    private final List<Sorter> sortList;

    private final Shuffler shuffler;
    private final int n;
    private final int m;

    private final int del;

    private final boolean acc;

    private String[] toDisplay;

    private void shuffle(WrappedArray arr) {
        try {
            shuffler.shuffle(arr, m);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }

    private void invoke(WrappedArray arr) {
        for (Sorter sorter : sortList) {
            arr.cd(Math.max(del/2,1));
            toDisplay[0] = "Shuffling";
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
            toDisplay[0] = sorter.toString();
            sorter.sort(arr);
            arr.cd(del);
            arr.check();
        }
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public SortPanel(List<Sorter> sortList, int n, int m, int del, boolean acc, Theme theme, Shuffler shuffler) {
        this.sortList = new ArrayList<>(sortList);
        this.n = n;
        this.m = m;
        this.acc = acc;
        this.del = del;
        this.toDisplay = new String[]{"Debug"};
        this.shuffler = shuffler;
        SwingUtilities.invokeLater(() -> {
            Integer[] temp = new Integer[n];
            for (int i=0; i<n; i++) {
                temp[i] = i % m;
            }
            WrappedArray wr = new WrappedArray(del, temp);
            var panel = theme.painter(wr, m, toDisplay);
            panel.setBackground(Color.BLACK);
            var frame = new JFrame("Sort Visualiser");
            frame.setSize(800, 450);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
            Thread paint = new Thread(() -> {
                Thread th = Thread.currentThread();
                try {
                    while(!th.isInterrupted()) {
                        panel.repaint();
                        Thread.sleep(33);
                    }
                }
                catch (InterruptedException e) {
                    th.interrupt();
                }
            });
            paint.start();
            Thread sort = new Thread(() -> {
                invoke(wr);
                if (!Thread.currentThread().isInterrupted()) {
                    paint.interrupt();
                    frame.dispose();
                }
            });
            sort.start();
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    paint.interrupt();
                    sort.interrupt();
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
        new SortPanel(ls, 100, 100, 10, false, new DefaultTheme(), new RandomPermutation());
        new SortPanel(ls, 100, 150, 30, false, new DefaultTheme(), new RandomPermutation());
    }
}
