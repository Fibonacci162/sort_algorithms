package org.example.main;

import org.example.shuffler.ShufflerFactory;
import org.example.sorter.Sorter;
import org.example.sorter.SorterFactory;
import org.example.painter.Theme;
import org.example.painter.ThemeFactory;
import org.example.shuffler.Shuffler;
import org.example.sorter.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class Main {

    public Main() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints gbc = new GridBagConstraints();

            var frame = new JFrame("Sort Visualiser Controller");
            frame.setLayout(new GridBagLayout());
            Map<String, List<Sorter>> ao = SorterFactory.alg_opts();
            List<Theme> thopts = ThemeFactory.themes();
            List<Shuffler> shufflers = ShufflerFactory.shufflers();

            JButton b = new JButton("Launch in Sequence");
            b.setBounds(380,650,200,50);
            JComboBox<String> cb = new JComboBox<>(new Vector<>(ao.keySet()));
            cb.setBounds(300,100,400,40);
            JLabel l1 = new JLabel("   Sorting algorithm visualiser   ");
            l1.setFont(new Font("Serif", java.awt.Font.PLAIN, 36));
            l1.setHorizontalAlignment(SwingConstants.CENTER);
            l1.setBounds(0,0, 960, 50);
            JLabel l2 = new JLabel("Algorithms: ");
            l2.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JLabel l3 = new JLabel("Number of elements: ");
            l3.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JLabel l4 = new JLabel("Range of elements: ");
            l4.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JLabel l5 = new JLabel("Delay(" + (char)0x03BC + "s): ");
            l5.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JSpinner s3 = new JSpinner(new SpinnerNumberModel(256, 64, 1024, 1));
            JSpinner s4 = new JSpinner(new SpinnerNumberModel(256,1,10000,1));
            JSlider s5 = new JSlider(0, 25000, 2500);
            s5.setPaintLabels(true);
            s5.setPaintTrack(true);
            s5.setPaintTicks(true);
            s5.setMinorTickSpacing(1000);
            s5.setMajorTickSpacing(10000);

            JCheckBox c6 = new JCheckBox("Change delay depending on algorithm");
            c6.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JCheckBox c61 = new JCheckBox("Change delay depending on array size");
            c61.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JComboBox<Theme> c7 = new JComboBox<>(new Vector<>(thopts));
            JLabel l7 = new JLabel("Visualisation type: ");
            l7.setFont(new Font("Serif", java.awt.Font.PLAIN, 12));
            JComboBox<Shuffler> c8 = new JComboBox<>(new Vector<>(shufflers));
            JLabel l8 = new JLabel("Shuffler type: ");

            JButton b2 = new JButton("Launch in Race");

            c8.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!c8.getItemAt(c8.getSelectedIndex()).respectLimit()) {
                        s4.setValue(s3.getValue());
                    }
                }
            });

            s4.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!c8.getItemAt(c8.getSelectedIndex()).respectLimit()) {
                        s4.setValue(s3.getValue());
                    }
                }
            });

            s3.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!c8.getItemAt(c8.getSelectedIndex()).respectLimit()) {
                        s4.setValue(s3.getValue());
                    }
                }
            });

            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int ndel = s5.getValue();
                    int n = (Integer)s3.getValue();
                    if (c61.isSelected()) {
                        double x = (double) ndel;
                        x /= n * Math.log(n) / Math.log(2);
                        x *= 2048.0;
                        ndel = (int)x;
                    }
                    new SortPanel(ao.get(cb.getItemAt(cb.getSelectedIndex())), n, (Integer)s4.getValue()+1, ndel, c6.isSelected(), (c7.getItemAt(c7.getSelectedIndex())), c8.getItemAt(c8.getSelectedIndex()));
                }
            });

            b2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int ndel = s5.getValue();
                    int n = (Integer)s3.getValue();
                    if (c61.isSelected()) {
                        double x = (double) ndel;
                        x /= n * Math.log(n) / Math.log(2);
                        x *= 2048.0;
                        ndel = (int)x;
                    }
                    new RacePanel(ao.get(cb.getItemAt(cb.getSelectedIndex())), n, (Integer)s4.getValue()+1, ndel, c6.isSelected(), (c7.getItemAt(c7.getSelectedIndex())), c8.getItemAt(c8.getSelectedIndex()));
                }
            });


            gbc.gridwidth = 2;
            gbc.ipady = 50;
            gbc.insets = new Insets(0,0,0,0);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            frame.add(l1, gbc);
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 1;
            frame.add(l2, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            frame.add(cb, gbc);
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 2;
            frame.add(l3, gbc);
            gbc.gridx = 1;
            frame.add(s3, gbc);
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 3;
            frame.add(l4, gbc);
            gbc.gridx = 1;
            frame.add(s4, gbc);
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 4;
            frame.add(l5, gbc);
            gbc.gridx = 1;
            frame.add(s5, gbc);
            gbc.ipady = 20;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 5;
            frame.add(c6, gbc);
            gbc.gridx = 1;
            gbc.gridy = 5;
            frame.add(c61, gbc);
            gbc.gridwidth = 1;
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 6;
            frame.add(l7, gbc);
            gbc.gridx = 1;
            gbc.gridy = 6;
            frame.add(c7, gbc);
            gbc.gridwidth = 1;
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 7;
            frame.add(l8, gbc);
            gbc.gridx = 1;
            gbc.gridy = 7;
            frame.add(c8, gbc);
            gbc.ipady = 20;
            gbc.gridx = 0;
            gbc.gridy = 8;
            frame.add(b, gbc);
            gbc.gridx = 1;
            gbc.gridy = 8;
            frame.add(b2, gbc);

            b.setSize(200,50);

            c6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (c6.isSelected()) {
                        if (c61.isSelected()) {
                            l5.setText("Delay(" + (char)0x03BC + "s for O(nlogn) and n=256): ");
                        }
                        else {
                            l5.setText("Delay(" + (char)0x03BC + "s for O(nlogn)): ");
                        }
                    }
                    else {
                        if (c61.isSelected()) {
                            l5.setText("Delay(" + (char)0x03BC + "s for n=256): ");
                        }
                        else {
                            l5.setText("Delay(" + (char)0x03BC + "s): ");
                        }
                    }
                }
            });

            c61.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (c6.isSelected()) {
                        if (c61.isSelected()) {
                            l5.setText("Delay(" + (char)0x03BC + "s for O(nlogn) and n=256): ");
                        }
                        else {
                            l5.setText("Delay(" + (char)0x03BC + "s for O(nlogn)): ");
                        }
                    }
                    else {
                        if (c61.isSelected()) {
                            l5.setText("Delay(" + (char)0x03BC + "s for n=256): ");
                        }
                        else {
                            l5.setText("Delay(" + (char)0x03BC + "s): ");
                        }
                    }
                }
            });

            frame.setSize(960, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
