package com.michel.basic;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel {
    // All video memory is located between 0x2400 - 0x3FFF
    // Everything is transferred but only this part should be asked about the graphic information
    private byte[] Memory;

    Screen(byte[] memory) {
        this.Memory = memory;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // The screen drawing goes from the bottom left to the top right
        // meaning it is rotated counter clockwise once.
        int y = 256; // = max
        int x = 0;
        byte pixelByte = 0;
        for (int memoryIndex = 0x2400; memoryIndex < 0x3FFF; memoryIndex++) {
            // Each bit is either 1 or 0 meaning white or black
            pixelByte = Memory[memoryIndex];
            for (int i = 0; i < 8; i++) {
                y--;
                if (y < 0) {
                    y = 255;
                    x++;
                }
                if (((pixelByte >> i) & 0x01) > 0) g.setColor(Color.WHITE);
                else g.setColor(Color.BLACK);
                g.drawLine(x, y, x, y);
            }
        }
    }
}
