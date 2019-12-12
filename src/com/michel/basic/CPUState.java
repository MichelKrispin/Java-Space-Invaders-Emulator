package com.michel.basic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CPUState {
    protected JPanel rootPanel;
    private JButton StepButton;
    private JTextPane DisassemblerView;
    private JTextField StepField;
    private JTextPane MemoryView;
    private JTextPane RegisterView;

    CPUState() {
        StepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DisassemblerView.setText(StepField.getText());
            }
        });
    }

    void Show() {
        JFrame frame = new JFrame("CPU - 8080");    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame.setContentPane(new CPUState().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
