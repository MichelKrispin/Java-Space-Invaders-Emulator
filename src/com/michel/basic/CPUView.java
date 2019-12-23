package com.michel.basic;

import javax.swing.*;

public class CPUView {
    protected JPanel rootPanel;
    private JTextPane DisassemblerView;
    private JTextPane MemoryView;
    private JTextPane RegisterView;
    private JTextPane CommandView;
    private JTextPane StackView;
    private CPU8080 CPU;

    CPUView(CPU8080 CPU) {
        this.CPU = CPU;
    }

    void Update(String InstructionDescription) {
        CommandView.setText("(" + CPU.getInstructionCounter() + ") " + InstructionDescription);
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
