package com.michel.basic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] argv) {
        byte[] memory;
        try {
            byte[] file = Files.readAllBytes(Paths.get("./invaders/invaders"));
            memory = new byte[65536]; // 16 kilobytes
            System.arraycopy(file, 0, memory, 0, file.length);
        } catch (IOException ie) {
            ie.printStackTrace();
            return;
        }

        SpaceInvadersMachine Machine = new SpaceInvadersMachine(memory);
        Machine.Show();
    }
}
