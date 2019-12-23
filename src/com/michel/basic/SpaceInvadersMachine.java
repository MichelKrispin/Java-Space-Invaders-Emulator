package com.michel.basic;

import javax.swing.*;

// TODO: FEHLER BEI 42433

public class SpaceInvadersMachine {
    private CPU8080 CPU;
    private CPUView View;
    private JPanel rootPanel;
    private Screen Canvas;
    private JButton StepButton;
    private JTextField StepField;
    private JButton StopButton;
    private JButton RunButton;
    private boolean isRunning = false;
    private Instruction InstructionInformation;

    SpaceInvadersMachine(CPU8080 CPU) {
        this.CPU = CPU;
        View = new CPUView(CPU);
        View.Show();
        View.Update("");

        // Add step to button
        StepButton.addActionListener(actionEvent -> {
            // Run for Steps times
            String InstructionDescription = "";
            int Steps = 1;
            try {
                Steps = Integer.parseInt(StepField.getText());
                if (Steps < 1)
                    Steps = 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < Steps; i++) {
                InstructionInformation = CPU.Run();
            }

            Canvas.repaint();
            View.Update(InstructionInformation.getDescription());
        });

        RunButton.addActionListener(actionEvent -> {
            isRunning = true;
            Update();
        });

        StopButton.addActionListener(actionEvent -> isRunning = false);

        // Set hover to work after clicking
        StopButton.setRequestFocusEnabled(false);
        RunButton.setRequestFocusEnabled(false);
        StepButton.setRequestFocusEnabled(false);
    }

    private void Update() {
        int StepCounter = 0;
        while (isRunning) {
            // Only update the background once all 50 000 steps
            InstructionInformation = CPU.Run();

            if (StepCounter % 50000 == 49999) {
                View.Update(InstructionInformation.getDescription());
            }

            Canvas.repaint();
            StepCounter++;
        }
    }

    void Show() {
        JFrame frame = new JFrame("Space Invaders");
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
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        Canvas = new Screen();
    }
}
