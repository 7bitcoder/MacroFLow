package Parser;

import ContrtolOutput.Validator;

import java.util.ArrayList;

public interface Execution {
    public int instrCounter = 0;

    public static interface Instruction {
        public void run();
        public void init(String[] args) throws Validator.ParserExcetption;
    }
    public void run();
}

class MacroExecution implements Execution {
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    void addInstruction(Execution.Instruction exec) {
        instructions.add(exec);
    }

    @Override
    public void run() {
        for (var instr : instructions)
            instr.run();
    }
}