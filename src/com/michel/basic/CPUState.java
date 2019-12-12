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
    private JTextPane StackView;
    private CPU8080 CPU;

    CPUState(CPU8080 CPU) {
        this.CPU = CPU;
        // Do everything once
        Update();

        // Add everything to button
        StepButton.addActionListener(actionEvent -> {
            Update();
        });
    }

    private void Update() {
        int Steps = 1;
        try {
            Steps = Integer.parseInt(StepField.getText());
            if (Steps < 1)
                Steps = 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Steps; i++) {
            CommandView.setText("(" + CPU.getInstructionCounter() + ") " + CPU.Run(true));
        }
        MemoryView.setText(CPU.getMemoryView());
        StackView.setText(CPU.getStackView());
        RegisterView.setText(CPU.getRegisterView());
        DisassemblerView.setText(CPU.Disassemble(16));
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
        frame.setSize(700, 350);
        frame.setVisible(true);
    }
}
