package com.michel.basic;

public class CPU8080 {
    private byte PSW = 0; // Flags
    private byte A = 0;
    private byte B = 0;
    private byte C = 0;
    private byte D = 0;
    private byte E = 0;
    private byte H = 0;
    private byte L = 0;
    private char SP = 0; // Stack pointer
    private char PC = 0; // Program counter
    private byte[] Memory; // A buffer to the memory

    protected CPU8080(byte[] memory) {
        Memory = memory;
    }

    protected void Run() {
        int counter = 0;
        while(counter < 8) {
            switch(Memory[PC]) {
                case 0x00:
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " NOP");
                    PC++;
                    break;
                case 0x01: // LXI B,D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LXI B");
                    B = (byte)(Memory[PC+2] << 8);
                    C = (byte)(Memory[PC+1] & 0x000000FF);
                    PC += 3;
                    break;
                case 0x05: // DCR B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x06: // MVI B, D8
                    B = Memory[PC+1];
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - MOV B");
                    PC += 2;
                    break;
                case 0x09: // DAD B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x0d: // DCR C
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x0e: // MVI C,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x0f: // RRC
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x11: // LXI D,D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LXI D");
                    D = (byte)(Memory[PC+2] << 8);
                    E = (byte)(Memory[PC+1] & 0x000000FF);
                    PC += 3;
                    break;
                case 0x19: // DAD D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x1a: // LDAX D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x21: // LXI H,D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x23: // INX H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x26: // MVI H,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x29: // DAD H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x31: // LXI SP, D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LXI (Push to stack pointer)");
                    SP = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    PC += 3;
                    break;
                case 0x32: // STA adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x36: // MVI M,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x3a: // LDA adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x3e: // MVI A,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x56: // MOV D,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x5e: // MOV E,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x66: // MOV H,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x6f: // MOV L,A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x77: // MOV M,A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x7a: // MOV A,D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x7b: // MOV A,E
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x7c: // MOV A,H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case 0x7e: // MOV A,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xa7: // ANA A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xaf: // XRA A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xc1: // POP B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - POP B");
                    C = Memory[SP];
                    B = Memory[SP+1];
                    SP += 2;
                    PC++;
                    break;
                case (byte) 0xc2: // JNZ adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xc3: // JMP adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - JMP");
                    PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    break;
                case (byte) 0xc5: // PUSH B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - PUSH B");
                    Memory[SP-1] = B;
                    Memory[SP-2] = C;
                    SP -= 2;
                    PC++;
                    break;
                case (byte) 0xc6: // ADI D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xc9: // RET
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xcd: // CALL adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - CALL");
                    Memory[SP-1] = (byte)(PC >> 8);
                    Memory[SP-2] = (byte)(PC & 0xFF);
                    SP -= 2;
                    PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    break;
                case (byte) 0xd1: // POP D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xd3: // OUT D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xd5: // PUSH D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xe1: // POP H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xe5: // PUSH H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xe6: // ANI D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xeb: // XCHG
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                case (byte) 0xf1: // POP PSW
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - POP PSW");
                    PSW = Memory[SP];
                    SP += 2;
                    PC++;
                    break;
                case (byte) 0xf5: // PUSH PSW
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    Memory[SP-1] = PSW;
                    SP -= 2;
                    PC++;
                    break;
                case (byte) 0xfb: // EI
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                case (byte) 0xfe: // CPI D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    break;
                default:
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Doesn't exist");
                    PC++;
            }
            // Print the next 8 bytes to the console for convenience
            System.out.print(String.format("0x%04X", (int)PC) + " - ");
            for (int i = 0; i < 8; i++) {
                System.out.print(String.format("%02X", Memory[PC+i]) + " ");
            }
            System.out.println("");

            counter++;
        }
    }
}
