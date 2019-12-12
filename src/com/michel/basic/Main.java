package com.michel.basic;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] argv) {
        CPU8080 CPU;
        try {
            byte[] file = Files.readAllBytes(Paths.get("./invaders/invaders"));
            byte[] memory = new byte[16000];
            System.arraycopy(file, 0, memory, 0, file.length);
            CPU = new CPU8080(memory);
        } catch (IOException ie) {
            ie.printStackTrace();
            return;
        }

        CPUState Window = new CPUState(CPU);
        Window.Show();
    }
}
