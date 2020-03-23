package Instructions;

import ContrtolOutput.Validator;

import java.util.ArrayList;


public class Executor {
    public static interface Instruction {
        public void run();

        public void init(String[] args) throws Validator.ParserExcetption;
    }

    ArrayList<Instruction> instructions = new ArrayList<Instruction>();

    void addInstruction(Instruction exec) {
        instructions.add(exec);
    }

    public void run() {
        for (var instr : instructions)
            instr.run();
    }
}