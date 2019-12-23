package com.michel.basic;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(50, 50, 100, 100);
    }
}
