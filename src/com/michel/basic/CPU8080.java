package com.michel.basic;

public class CPU8080 {
    // PSW binary flags:
    static private byte ZeroFlag   = (byte)0x80;
    static private byte SignFlag   = 0x40;
    static private byte ParityFlag = 0x20;
    static private byte ACFlag     = 0x10;
    static private byte CarryFlag  = 0x08;

    private boolean Interrupt = false;
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

    private Instruction InstructionInformation;

    CPU8080(byte[] memory) {
        this.Memory = memory;
        InstructionInformation = new Instruction();
    }

    Instruction Run() {
        InstructionCounter++;
        InstructionInformation.setOut(false);
        InstructionInformation.setIn(false);
        switch(Memory[PC]) {
            case 0x00:
                PC++;
                InstructionInformation.setDescription("NOP\n-> Do nothing");
                break;
            case 0x01: // LXI B,D16
                B = (byte)(Memory[PC+2] << 8);
                C = (byte)(Memory[PC+1] & 0x000000FF);
                PC += 3;
                InstructionInformation.setDescription("LXI\n->Load data into B");
                break;
            case 0x05: // DCR B
                B -= 1;
                // Set flags
                if (B == 0) {
                    PSW = (byte)(PSW | ZeroFlag);
                }
                else {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                if (0x80 == (B & 0x80)) {
                    PSW = (byte)(PSW | SignFlag);
                } else {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                // Skip carry flag because there is no calculation
                PC++;
                InstructionInformation.setDescription("DCR\n-> Decrease B");
                break;
            case 0x06: // MVI B, D8
                B = Memory[PC+1];
                PC += 2;
                InstructionInformation.setDescription("MOV\n-> Move address into B");
                break;
            case 0x09: // DAD B
            {
                char HL = (char) ((H << 8) | (L & 0x000000FF));
                char BC = (char) ((B << 8) | (C & 0x000000FF));
                int Result = HL + BC;
                H = (byte)(Result >> 8);
                L = (byte)Result;
                if (Result > 0xFF) {
                    PSW = (byte)(PSW | CarryFlag);
                } else {
                    PSW = (byte)(PSW & (~CarryFlag));
                }
                PC++;
                InstructionInformation.setDescription("DAD B\n-> Add HL to BC");
                break;
            }
            case 0x0d: // DCR C
                C -= 1;
                // Set flags
                if (C == 0) {
                    PSW = (byte)(PSW | ZeroFlag);
                }
                else {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                if (0x80 == (C & 0x80)) {
                    PSW = (byte)(PSW | SignFlag);
                } else {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                // Skip carry flag because there is no calculation
                PC++;
                InstructionInformation.setDescription("DCR\n-> Decrease C");
                break;
            case 0x0e: // MVI C,D8
                C = Memory[PC+1];
                PC += 2;
                InstructionInformation.setDescription("Move byte into C");
                break;
            case 0x0f: // RRC
            {
                boolean RightBit = (A & 0x1) > 0; // Right bit active?
                A = (byte) (A >> 1);
                if (RightBit) { // If right bit is active
                    A = (byte) (A | 0x80); // Add bit to the left
                    PSW = (byte) (PSW | CarryFlag);
                } else {
                    A = (byte) (A & 0b01111111); // Remove bit to the right
                    PSW = (byte) (PSW & (~CarryFlag));
                }
                PC++;
                InstructionInformation.setDescription("RRC\n-> Shift A right and wrap");
                break;
            }
            case 0x11: // LXI D,D16
                D = Memory[PC+2];
                E = Memory[PC+1];
                PC += 3;
                InstructionInformation.setDescription("LXI D\n-> Load memory into DE");
                break;
            case 0x13: // INX D
                E++;
                if (E == 0) // If E overflows increment D
                    D++;
                PC++;
                InstructionInformation.setDescription("Increment DE");
                break;
            case 0x19: // DAD D
            {
                char HL = (char) ((H << 8) | (L & 0x000000FF));
                char DE = (char) ((D << 8) | (E & 0x000000FF));
                int Result = HL + DE;
                H = (byte) ((Result & 0x0000FF00) >> 8);
                L = (byte) (Result & 0x000000FF);
                if (Result > 0xFF) {
                    PSW = (byte) (PSW | CarryFlag);
                } else {
                    PSW = (byte) (PSW & (~CarryFlag));
                }
                PC++;
                InstructionInformation.setDescription("DAD D\n-> Add HL to DE");
                break;
            }
            case 0x1a: // LDAX D
                A = Memory[(char)((D << 8) | (E & 0x000000FF))];
                PC++;
                InstructionInformation.setDescription("LDAX\n-> Load memory in A)");
                break;
            case 0x21: // LXI H,D16
                H = Memory[PC+2];
                L = Memory[PC+1];
                PC += 3;
                InstructionInformation.setDescription("LXI H\n-> Load memory into HL");
                break;
            case 0x23: // INX H
                L++;
                if (L == 0) // If L overflows increment H
                    H++;
                PC++;
                InstructionInformation.setDescription("INX\n-> Increment HL");
                break;
            case 0x26: // MVI H,D8
                H = Memory[PC+1];
                PC += 2;
                InstructionInformation.setDescription("MVI H,D8\n->Move byte into H");
                break;
            case 0x29: // DAD H
            {
                char HL = (char) ((H << 8) | (L & 0x000000FF));
                int Result = HL + HL;
                H = (byte) ((Result & 0x0000FF00) >> 8);
                L = (byte) (Result & 0x000000FF);
                if ((Result & 0xFFFFFF00) != 0) {
                    PSW = (byte) (PSW | CarryFlag);
                } else {
                    PSW = (byte) (PSW & (~CarryFlag));
                }
                PC++;
                InstructionInformation.setDescription("DAD H\n-> Add HL to HL");
                break;
            }
            case 0x31: // LXI SP, D16
                SP = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                PC += 3;
                InstructionInformation.setDescription("LXI\n-> Push to stack pointer");
                break;
            case 0x32: // STA adr
                Memory[(char)((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF))] = A;
                PC += 3;
                InstructionInformation.setDescription("STA\n-> Save A to address");
                break;
            case 0x36: // MVI M,D8
                Memory[(char)((H << 8) | (L & 0x000000FF))] = Memory[PC+1];
                PC += 2;
                InstructionInformation.setDescription("Move byte immediately into Memory[HL]");
                break;
            case 0x3a: // LDA adr
                A = Memory[(char)((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF))];
                PC += 3;
                InstructionInformation.setDescription("LDA adr\n-> Load address into A");
                break;
            case 0x3e: // MVI A,D8
                A = Memory[PC+1];
                PC += 2;
                InstructionInformation.setDescription("Move byte immediately into A");
                break;
            case 0x56: // MOV D,M
                D = Memory[(char)((H << 8) | (L & 0x000000FF))];
                PC++;
                InstructionInformation.setDescription("Move Memory[HL] into D");
                break;
            case 0x5e: // MOV E,M
                E = Memory[(char)((H << 8) | (L & 0x000000FF))];
                PC++;
                InstructionInformation.setDescription("Move Memory[HL] into E");
                break;
            case 0x66: // MOV H,M
                H = Memory[(char)((H << 8) | (L & 0x000000FF))];
                PC++;
                InstructionInformation.setDescription("Move Memory[HL] into H");
                break;
            case 0x6f: // MOV L,A
                L = A;
                PC++;
                InstructionInformation.setDescription("Move A into L");
                break;
            case 0x77: // MOV M,A
                Memory[(char)((H << 8) | (L & 0x000000FF))] = A;
                PC++;
                InstructionInformation.setDescription("MOV A to Memory at HL");
                break;
            case 0x7a: // MOV A,D
                A = D;
                PC++;
                InstructionInformation.setDescription("Move D into A");
                break;
            case 0x7b: // MOV A,E
                A = E;
                PC++;
                InstructionInformation.setDescription("Move E into A");
                break;
            case 0x7c: // MOV A,H
                A = H;
                PC++;
                InstructionInformation.setDescription("Move H into A");
                break;
            case 0x7e: // MOV A,M
                A = Memory[(char)((H << 8) | (L & 0x000000FF))];
                PC++;
                InstructionInformation.setDescription("Move Memory[HL] into A");
                break;
            case (byte) 0xa7: // ANA A
                // A = (byte)(A & A);
                PSW = (byte)(PSW & (~CarryFlag)); // The description says it will be reset to 0
                if ((A & 0x80) > 0) {
                    PSW = (byte)(PSW | SignFlag);
                } else  {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                if (A == 0) {
                    PSW = (byte)(PSW | ZeroFlag);
                } else  {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                PC++;
                InstructionInformation.setDescription("And A with A");
                break;
            case (byte) 0xaf: // XRA A
                A = 0; //A = (byte)(A ^ A);
                PSW = (byte)(PSW & (~CarryFlag)); // Description says carry flag will be set to 0
                // Always false
                PSW = (byte)(PSW & (~SignFlag));
                // Always true
                PSW = (byte)(PSW | ZeroFlag);
                PC++;
                InstructionInformation.setDescription("XOR A with A");
                break;
            case (byte) 0xc1: // POP B
                C = Memory[SP];
                B = Memory[SP+1];
                SP += 2;
                PC++;
                InstructionInformation.setDescription("POP B");
                break;
            case (byte) 0xc2: // JNZ adr
                if ((PSW & ZeroFlag) == 0) { // If zero flag is zero jump
                    PC = (char) ((Memory[PC + 2] << 8) | (Memory[PC + 1] & 0x000000FF));
                } else {
                    PC += 3;
                }
                InstructionInformation.setDescription("Jump if not zero");
                break;
            case (byte) 0xc3: // JMP adr
                PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                InstructionInformation.setDescription("JMP to address");
                break;
            case (byte) 0xc5: // PUSH B
                Memory[SP-1] = B;
                Memory[SP-2] = C;
                SP -= 2;
                PC++;
                InstructionInformation.setDescription("PUSH B");
                break;
            case (byte) 0xc6: // ADI D8
                int Result = A + Memory[PC+1];
                if ((Result & 0x80) > 0) {
                    PSW = (byte)(PSW | SignFlag);
                } else  {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                if ((Result & 0xFF) == 0) {
                    PSW = (byte)(PSW | ZeroFlag);
                } else  {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                if ((Result & 0xFFFFFF00) > 0) {
                    PSW = (byte)(PSW | CarryFlag);
                } else  {
                    PSW = (byte)(PSW & (~CarryFlag));
                }
                A = (byte)Result;
                PC += 2;
                InstructionInformation.setDescription("Add byte immediately to A");
                break;
            case (byte) 0xc9: // RET
                PC = (char)((Memory[SP+1] << 8) | (Memory[SP] & 0x000000FF));
                SP += 2;
                InstructionInformation.setDescription("RET\n-> Return from call");
                break;
            case (byte) 0xcd: // CALL adr
                // Return address is the next instruction
                Memory[SP-1] = (byte)((PC + 3) >> 8);
                Memory[SP-2] = (byte)((PC + 3) & 0xFF);
                SP -= 2;
                PC = (char) ((Memory[PC+2] << 8) | (Memory[PC+1] & 0x000000FF));
                InstructionInformation.setDescription("CALL");
                break;
            case (byte) 0xd1: // POP D
                E = Memory[SP];
                D = Memory[SP+1];
                SP += 2;
                PC++;
                InstructionInformation.setDescription("Pop D");
                break;
            case (byte) 0xd3: // OUT D8
                // TODO: What to do here?
                PC += 2;
                InstructionInformation.setOut(true);
                InstructionInformation.setDescription("Out D8");
                break;
            case (byte) 0xd5: // PUSH D
                Memory[SP-2] = E;
                Memory[SP-1] = D;
                SP -= 2;
                PC++;
                InstructionInformation.setDescription("Push D");
                break;
            case (byte) 0xe1: // POP H
                L = Memory[SP];
                H = Memory[SP+1];
                SP += 2;
                PC++;
                InstructionInformation.setDescription("Pop H");
                break;
            case (byte) 0xe5: // PUSH H
                Memory[SP-2] = L;
                Memory[SP-1] = H;
                SP -= 2;
                PC++;
                InstructionInformation.setDescription("Push H");
                break;
            case (byte) 0xe6: // ANI D8
                A = (byte)(A & Memory[PC+1]);
                PSW = (byte)(PSW & (~CarryFlag)); // The description says it will be reset to 0
                if ((A & 0x80) > 0) {
                    PSW = (byte)(PSW | SignFlag);
                } else  {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                if (A == 0) {
                    PSW = (byte)(PSW | ZeroFlag);
                } else  {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                PC += 2;
                InstructionInformation.setDescription("And immediate with byte");
                break;
            case (byte) 0xeb: // XCHG
            {
                byte HTemp = H;
                byte LTemp = L;
                H = D;
                L = E;
                D = HTemp;
                E = LTemp;
                PC++;
                InstructionInformation.setDescription("Exchange HL and DE");
                break;
            }
            case (byte) 0xf1: // POP PSW
                A = Memory[SP+1];
                PSW = Memory[SP];
                SP += 2;
                PC++;
                InstructionInformation.setDescription("POP PSW");
                break;
            case (byte) 0xf5: // PUSH PSW
                Memory[SP-1] = A;
                Memory[SP-2] = PSW;
                SP -= 2;
                PC++;
                InstructionInformation.setDescription("Push PSW");
                break;
            case (byte) 0xfb: // EI
                Interrupt = true;
                PC++;
                InstructionInformation.setDescription("EI\n-> Enable interrupt");
                break;
            case (byte) 0xfe: // CPI D8
                int result = A - Memory[PC+1];
                if (result == 0) { // Zero flag
                    PSW = (byte)(PSW | ZeroFlag);
                } else {
                    PSW = (byte)(PSW & (~ZeroFlag));
                }
                if (result < 0) { // Sign Flag
                    PSW = (byte)(PSW | SignFlag);
                } else {
                    PSW = (byte)(PSW & (~SignFlag));
                }
                if (A < Memory[PC+1]) // Carry Flag
                    PSW = (byte)(PSW | CarryFlag);
                else
                    PSW = (byte)(PSW & (~CarryFlag));
                PC += 2;
                InstructionInformation.setDescription("CPI D8\n-> Compare A with byte");
                break;
            default:
                System.out.println(String.format("0x%04X", (int)PC) + " - " + String.format("0x%02X", Memory[PC]) + " - Doesn't exist");
                PC++;
                InstructionInformation.setDescription("");
        }
        return InstructionInformation;
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
        String Flags = "";
        if ((PSW & ACFlag) == ACFlag) Flags += "a";
        else Flags += ".";
        if ((PSW & CarryFlag) == CarryFlag) Flags += "c";
        else Flags += ".";
        if ((PSW & ParityFlag) == ParityFlag) Flags += "p";
        else Flags += ".";
        if ((PSW & SignFlag) == SignFlag) Flags += "s";
        else Flags += ".";
        if ((PSW & ZeroFlag) == ZeroFlag) Flags += "z";
        else Flags += ".";

        return "A  F   B  C   D  E   H  L   PC    SP    FLAGS\n" +
                String.format("%02X", A) + " " +
                String.format("%02X", PSW) + " " +
                String.format("%02X", B) + " " +
                String.format("%02X", C) + "  " +
                String.format("%02X", D) + " " +
                String.format("%02X", E) + "  " +
                String.format("%02X", H) + " " +
                String.format("%02X", L) + "  " +
                String.format("%04X", (int)PC) + "  " +
                String.format("%04X", (int)SP) + "  " +
                Flags;
                //String.format("%8s", Integer.toBinaryString((int)PSW)).replace(' ', '0');
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
                    i += 2;
                    Limit += 2;
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
