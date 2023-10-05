package org.example.main;

import javax.swing.*;
import java.awt.*;

public class BlackBox extends JPanel {
    private static final long serialVersionUID = 7148504528832137003L;

    public BlackBox() {
    }

    /**
     * Called by the runtime system whenever the panel needs painting.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
    }
}
