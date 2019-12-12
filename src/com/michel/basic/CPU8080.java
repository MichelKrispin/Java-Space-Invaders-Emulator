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
    private int InstructionCounter = 0; // Instruction counter

    CPU8080(byte[] memory) {
        Memory = memory;
    }

    String Run(boolean Step) {
        InstructionCounter++;
        while(true) {
            switch(Memory[PC]) {
                case 0x00:
                    PC++;
                    if (Step) {
                        return "NOP\n-> Do nothing";
                    }
                    break;
                case 0x01: // LXI B,D16
                    B = (byte)(Memory[PC+2] << 8);
                    C = (byte)(Memory[PC+1] & 0x000000FF);
                    PC += 3;
                    if (Step) {
                        return "LXI\n-> Load memory into B from parameter";
                    }
                    break;
                case 0x05: // DCR B
                    B -= 1;
                    // Set flags
                    SetFlags(B, false);
                    PC++;
                    if (Step) {
                        return "DCR\n-> Decrease B";
                    }
                    break;
                case 0x06: // MVI B, D8
                    B = Memory[PC+1];
                    PC += 2;
                    if (Step) {
                        return "MOV\n-> Move address into B";
                    }
                    break;
                case 0x09: // DAD B
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x0d: // DCR C
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x0e: // MVI C,D8
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x0f: // RRC
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x11: // LXI D,D16
                    D = Memory[PC+2];
                    E = Memory[PC+1];
                    PC += 3;
                    if (Step) {
                        return "LXI D";
                    }
                    break;
                case 0x13: // INX D
                    E++;
                    if (E == 0) // If E overflows increment D
                        D++;
                    PC++;
                    if (Step) {
                        return "INX D";
                    }
                    break;
                case 0x19: // DAD D
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x1a: // LDAX D
                    A = Memory[(char)((D << 8) | (E & 0x000000FF))];
                    PC++;
                    if (Step) {
                        return "LDAX\n-> Load memory in A)";
                    }
                    break;
                case 0x21: // LXI H,D16
                    H = Memory[PC+2];
                    L = Memory[PC+1];
                    PC += 3;
                    if (Step) {
                        return "LXI H";
                    }
                    break;
                case 0x23: // INX H
                    L++;
                    if (L == 0) // If L overflows increment H
                        H++;
                    PC++;
                    if (Step) {
                        return "INX\n-> Increment HL";
                    }
                    break;
                case 0x26: // MVI H,D8
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x29: // DAD H
                    PC++;
                    if (Step) {
                        return "is not implemented";
                    }
                    break;
                case 0x31: // LXI SP, D16
                    SP = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    PC += 3;
                    if (Step) {
                        return "LXI\n-> Push to stack pointer";
                    }
                    break;
                case 0x32: // STA adr
                    Memory[(char)((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF))] = A;
                    PC += 3;
                    if (Step) {
                        return "STA\n-> Save A to adress";
                    }
                    break;
                case 0x36: // MVI M,D8
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x3a: // LDA adr
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x3e: // MVI A,D8
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x56: // MOV D,M
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x5e: // MOV E,M
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x66: // MOV H,M
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x6f: // MOV L,A
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x77: // MOV M,A
                    Memory[(char)((H << 8) | (L & 0x000000FF))] = A;
                    PC++;
                    if (Step) {
                        return "MOV A to Memory at HL";
                    }
                    break;
                case 0x7a: // MOV A,D
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x7b: // MOV A,E
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x7c: // MOV A,H
                    PC++;
                    return "is not implemented";
                    //break;
                case 0x7e: // MOV A,M
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xa7: // ANA A
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xaf: // XRA A
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xc1: // POP B
                    C = Memory[SP];
                    B = Memory[SP+1];
                    SP += 2;
                    PC++;
                    if (Step) {
                        return "POP B";
                    }
                    break;
                case (byte) 0xc2: // JNZ adr
                    if ((PSW & ZeroFlag) == 0) { // If zero flag is zero jump
                        PC = (char) ((Memory[PC + 2] << 8) | (Memory[PC + 1] & 0x000000FF));
                    } else {
                        PC += 3;
                    }
                    if (Step) {
                        return "JNZ";
                    }
                    break;
                case (byte) 0xc3: // JMP adr
                    PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    if (Step) {
                        return "JMP to address";
                    }
                    break;
                case (byte) 0xc5: // PUSH B
                    Memory[SP-1] = B;
                    Memory[SP-2] = C;
                    SP -= 2;
                    PC++;
                    if (Step) {
                        return "PUSH B";
                    }
                    break;
                case (byte) 0xc6: // ADI D8
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xc9: // RET
                    PC = (char)((Memory[SP+1] << 8) | (Memory[SP] & 0x000000FF));
                    SP += 2;
                    if (Step) {
                        return "RET\n-> Return from call";
                    }
                    break;
                case (byte) 0xcd: // CALL adr
                    Memory[SP-1] = (byte)(PC >> 8);
                    Memory[SP-2] = (byte)(PC & 0xFF);
                    SP -= 2;
                    PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                    if (Step) {
                        return "CALL";
                    }
                    break;
                case (byte) 0xd1: // POP D
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xd3: // OUT D8
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xd5: // PUSH D
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xe1: // POP H
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xe5: // PUSH H
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xe6: // ANI D8
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xeb: // XCHG
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xf1: // POP PSW
                    PSW = Memory[SP];
                    SP += 2;
                    PC++;
                    if (Step) {
                        return "POP PSW";
                    }
                    break;
                case (byte) 0xf5: // PUSH PSW
                    Memory[SP-1] = PSW;
                    SP -= 2;
                    PC++;
                    if (Step) {
                        return "is not implemented";
                    }
                    break;
                case (byte) 0xfb: // EI
                    PC++;
                    return "is not implemented";
                    //break;
                case (byte) 0xfe: // CPI D8
                    PC++;
                    return "is not implemented";
                    //break;
                default:
                    System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Doesn't exist");
                    PC++;
                    return "";
            }
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

    /**
     * Print the memory for the next 96 bytes.
     * @return A pretty printed String.
     */
    String getMemoryView() {
        StringBuilder Result = new StringBuilder();
        for (int i = 0; i < 96; i++) {
            if (i % 8 == 0) {
                if (i != 0) {
                   Result.append("\n");
                }
                Result.append(String.format("%04X", PC+i)).append("\t");
            }
            Result.append(String.format("%02X", Memory[PC + i])).append(" ");
        }
        return Result.toString();
    }

    /**
     * Print the stack -12 to +36 bytes.
     * The current position is indicated by the arrow.
     * @return A pretty printed String.
     */
    String getStackView() {
        StringBuilder Result = new StringBuilder();
        for (int i = -12; i < 36; i++) {
            if (i % 4 == 0) {
                if (i != -12) {
                    Result.append("\n");
                }
                Result.append(String.format("%04X", (SP+i)&0x0000FFFF));
                if (i == 0){
                    Result.append(" ->");
                }
                Result.append("\t");
            }
            try {
                Result.append(String.format("%02X", Memory[SP + i])).append(" ");
            } catch (IndexOutOfBoundsException e) { // Skip this one as its probably negative
                Result.append("xx ");
            }
        }
        return Result.toString();
    }

    String getRegisterView() {
        return "A  F   B  C   D  E   H  L   PC    SP    FLAGS\n" +
                String.format("%02X", A) + " 00  " +
                String.format("%02X", B) + " " +
                String.format("%02X", C) + "  " +
                String.format("%02X", D) + " " +
                String.format("%02X", E) + "  " +
                String.format("%02X", H) + " " +
                String.format("%02X", L) + "  " +
                String.format("%04X", (int)PC) + "  " +
                String.format("%04X", (int)SP) + "  " +
                String.format("%8s", Integer.toBinaryString((int)PSW)).replace(' ', '0');
    }

    int getInstructionCounter() {
        return InstructionCounter;
    }

    String Disassemble(int NumberOfSteps) {
        StringBuilder Result = new StringBuilder();
        int Limit = NumberOfSteps + PC;
        for (int i = PC; i < Limit; i++) {
            switch(Memory[i]) {
                case 0x00:
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - NOP");
                    break;
                case 0x01: // LXI B,D16
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LXI B");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x05: // DCR B
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - Decrease B");
                    break;
                case 0x06: // MVI B, D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV B");
                    i++;
                    Limit++;
                    break;
                case 0x09: // DAD B
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - DAD B");
                    break;
                case 0x0d: // DCR C
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - DCR C");
                    break;
                case 0x0e: // MVI C,D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MVI C,D8");
                    i++;
                    Limit++;
                    break;
                case 0x0f: // RRC
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - RRC");
                    break;
                case 0x11: // LXI D,D16
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LXI D,D16");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x13: // INX D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - INX D");
                    break;
                case 0x19: // DAD D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - DAD D");
                    break;
                case 0x1a: // LDAX D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LDAX D");
                    break;
                case 0x21: // LXI H,D16
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LXI H");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x23: // INX H
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - INX H");
                    break;
                case 0x26: // MVI H,D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MVI H,D8");
                    i++;
                    Limit++;
                    break;
                case 0x29: // DAD H
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - DAD H");
                    break;
                case 0x31: // LXI SP, D16
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LXI SP, D16");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x32: // STA adr
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - STA adr");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x36: // MVI M,D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MVI M,D8");
                    i++;
                    Limit++;
                    break;
                case 0x3a: // LDA adr
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - LDA adr");
                    i += 2;
                    Limit += 2;
                    break;
                case 0x3e: // MVI A,D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MVI A, D8");
                    i++;
                    Limit++;
                    break;
                case 0x56: // MOV D,M
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV D,M");
                    break;
                case 0x5e: // MOV E,M
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV E,M");
                    break;
                case 0x66: // MOV H,M
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV H,M");
                    break;
                case 0x6f: // MOV L,A
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV L,A");
                    break;
                case 0x77: // MOV M,A
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV L,A");
                    break;
                case 0x7a: // MOV A,D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV A,D");
                    break;
                case 0x7b: // MOV A,E
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV A,E");
                    break;
                case 0x7c: // MOV A,H
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV A,H");
                    break;
                case 0x7e: // MOV A,M
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - MOV A,M");
                    break;
                case (byte) 0xa7: // ANA A
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - ANA A");
                    break;
                case (byte) 0xaf: // XRA A
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - XRA A");
                    break;
                case (byte) 0xc1: // POP B
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - POP B");
                    break;
                case (byte) 0xc2: // JNZ adr
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - JNZ");
                    i += 2;
                    Limit += 2;
                    break;
                case (byte) 0xc3: // JMP adr
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - JMP");
                    i += 2;
                    Limit += 2;
                    break;
                case (byte) 0xc5: // PUSH B
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - PUSH B");
                    break;
                case (byte) 0xc6: // ADI D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - ADI D8");
                    i++;
                    Limit++;
                    break;
                case (byte) 0xc9: // RET
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - RET");
                    break;
                case (byte) 0xcd: // CALL adr
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - CALL");
                    i += 2;
                    Limit += 2;
                    break;
                case (byte) 0xd1: // POP D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - POP D");
                    break;
                case (byte) 0xd3: // OUT D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - OUT D8");
                    i++;
                    Limit++;
                    break;
                case (byte) 0xd5: // PUSH D
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - PUSH D");
                    break;
                case (byte) 0xe1: // POP H
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - POP H");
                    break;
                case (byte) 0xe5: // PUSH H
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - PUSH H");
                    break;
                case (byte) 0xe6: // ANI D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - ANI D8");
                    i++;
                    Limit++;
                    break;
                case (byte) 0xeb: // XCHG
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - XCHG");
                    break;
                case (byte) 0xf1: // POP PSW
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - POP PSW");
                    break;
                case (byte) 0xf5: // PUSH PSW
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - PUSH PSW");
                    break;
                case (byte) 0xfb: // EI
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - EI");
                    break;
                case (byte) 0xfe: // CPI D8
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - CPI D8");
                    i++;
                    Limit++;
                    break;
                default:
                    Result.append(String.format("%04X", (int) i)).append(" - ").append(String.format("%02X", Memory[i])).append(" - Doesn't exist");
            }
            Result.append("\n");
        }
        return Result.toString();
    }

}
