package com.michel.basic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] argv) {
        CPU8080 CPU;
        try {
            byte[] file = Files.readAllBytes(Paths.get("./invaders/invaders"));
            byte[] memory = new byte[65536]; // 16 kilobytes
            System.arraycopy(file, 0, memory, 0, file.length);
            CPU = new CPU8080(memory);
        } catch (IOException ie) {
            ie.printStackTrace();
            return;
        }

        SpaceInvadersMachine Machine = new SpaceInvadersMachine(CPU);
        Machine.Show();
    }
}
