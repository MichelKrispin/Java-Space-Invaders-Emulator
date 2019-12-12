package com.michel.basic;

import javax.swing.*;

public class CPUState {
    protected JPanel rootPanel;
    private JButton StepButton;
    private JTextPane DisassemblerView;
    private JTextField StepField;
    private JTextPane MemoryView;
    private JTextPane RegisterView;
    private JTextPane CommandView;

    CPUState(CPU8080 CPU) {
        StepButton.addActionListener(actionEvent -> {
            CommandView.setText("(" + CPU.getInstructionCounter() + ") " + CPU.Run(true));
            MemoryView.setText(CPU.getMemoryView());
            RegisterView.setText(CPU.getRegisterView());
            DisassemblerView.setText(CPU.Disassemble(16));
        });
    }

    void Show() {
        JFrame frame = new JFrame("CPU - 8080");
        try { // Set everything to look like Windows native
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350);
        frame.setVisible(true);
    }
}
