package com.michel.basic;

public class CPU8080 {
    // PSW binary flags:
    static private byte ZeroFlag   = 0x01;
    static private byte SignFlag   = 0x02;
    static private byte ParityFlag = 0x04;
    static private byte CarryFlag  = 0x08;
    static private byte ACFlag     = 0x10;

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
        int InstructionCounter = 0;
        while(true) {
            PrintState();
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
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Decrease B");
                    B -= 1;
                    // Set flags
                    SetFlags(B, false);
                    PC++;
                    break;
                case 0x06: // MVI B, D8
                    B = Memory[PC+1];
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - MOV B");
                    PC += 2;break;
                case 0x09: // DAD B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x0d: // DCR C
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x0e: // MVI C,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x0f: // RRC
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x11: // LXI D,D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LXI D");
                    D = Memory[PC+2];
                    E = Memory[PC+1];
                    PC += 3;
                    break;
                case 0x13: // INX D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - INX D");
                    E++;
                    if (E == 0) // If E overflows increment D
                        D++;
                    PC++;
                    break;
                case 0x19: // DAD D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x1a: // LDAX D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LDAX (Load memory in A)");
                    A = Memory[(char)((D << 8) | (E & 0x000000FF))];
                    PC++;
                    break;
                case 0x21: // LXI H,D16
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - LXI H");
                    H = Memory[PC+2];
                    L = Memory[PC+1];
                    PC += 3;
                    break;
                case 0x23: // INX H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Increment HL");
                    L++;
                    if (L == 0) // If L overflows increment H
                        H++;
                    PC++;
                    break;
                case 0x26: // MVI H,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
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
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - STA (Save A to adress)");
                    Memory[(char)((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF))] = A;
                    PC += 3;
                    break;
                case 0x36: // MVI M,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x3a: // LDA adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x3e: // MVI A,D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x56: // MOV D,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x5e: // MOV E,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x66: // MOV H,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x6f: // MOV L,A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x77: // MOV M,A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - MOV A to Memory at HL");
                    Memory[(char)((H << 8) | (L & 0x000000FF))] = A;
                    PC++;
                    break;
                case 0x7a: // MOV A,D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x7b: // MOV A,E
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x7c: // MOV A,H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case 0x7e: // MOV A,M
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xa7: // ANA A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xaf: // XRA A
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xc1: // POP B
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - POP B");
                    C = Memory[SP];
                    B = Memory[SP+1];
                    SP += 2;
                    PC++;
                    break;
                case (byte) 0xc2: // JNZ adr
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - JNZ");
                    if ((PSW & ZeroFlag) == 0) { // If zero flag is zero jump
                        PC = (char) ((Memory[PC + 2] << 8) | (Memory[PC + 1] & 0x000000FF));
                    } else {
                        PC += 3;
                    }
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
                    return;
                    //break;
                case (byte) 0xc9: // RET
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - RET");
                    PC = (char)((Memory[SP+1] << 8) | (Memory[SP] & 0x000000FF));
                    SP -= 2;
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
                    return;
                    //break;
                case (byte) 0xd3: // OUT D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xd5: // PUSH D
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xe1: // POP H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xe5: // PUSH H
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xe6: // ANI D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                case (byte) 0xeb: // XCHG
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
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
                    return;
                    //break;
                case (byte) 0xfe: // CPI D8
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " is not implemented");
                    PC++;
                    return;
                    //break;
                default:
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Doesn't exist");
                    PC++;
                    return;
            }
            /*
            // Print the next 8 bytes to the console for convenience
            System.out.print("           ");
            for (int i = 0; i < 8; i++) {
                System.out.print(String.format("%02X", Memory[PC+i]) + " ");
            }
            System.out.println();
            if (InstructionCounter % 25000 == 0) {
                System.out.println("Instruction counter at " + InstructionCounter);
            }
            */
            InstructionCounter++;
        }
    }

    private void SetFlags(byte Input, boolean SetCarryFlag) {
        if (Input == 0) {
            PSW = (byte)(PSW | ZeroFlag);
        }
        else {
            PSW = (byte)(PSW & (~ZeroFlag));
        }
        if (0x80 == (Input & 0x80)) {
            PSW = (byte)(PSW | SignFlag);
        } else {
            PSW = (byte)(PSW & (~SignFlag));
        }
        if (SetCarryFlag) {
            System.out.println(" -- IMPLEMENT CARRY FLAG -- ");
        }
    }

    private void PrintState() {
        System.out.println("_____________________________________________");
        System.out.println("A  F   B  C   D  E   H  L   PC    SP    FLAGS");
        System.out.println(
                String.format("%02X", A) + " 00  " +
                        String.format("%02X", B) + " " +
                        String.format("%02X", C) + "  " +
                        String.format("%02X", D) + " " +
                        String.format("%02X", E) + "  " +
                        String.format("%02X", H) + " " +
                        String.format("%02X", L) + "  " +
                        String.format("%04X", (int)PC) + "  " +
                        String.format("%04X", (int)SP) + "  " +
                        Integer.toBinaryString((int)PSW));
    }
}
