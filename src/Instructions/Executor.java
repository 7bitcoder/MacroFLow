package Instructions;

import ControlOutput.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class Executor {
    public static interface Instruction {
        public void run();

        public void init(String[] args, Executor exec) throws Validator.ParserExcetption;
    }

    public Map<String, Integer> variables = new HashMap<>();
    public Map<String, Integer> JumpLabels = new HashMap<>();
    public int bracketIndex = 0;
    public Stack<String> brackets = new Stack<>();
    private int programCounter = 0;
    ArrayList<Instruction> instructions = new ArrayList<Instruction>();

    public void setProgramCounter(int i) {
        programCounter = i;
    }

    public int getInstructionIndex() {
        return instructions.size();
    }

    void addInstruction(Instruction exec) {
        instructions.add(exec);
    }

    public boolean check() {
        return brackets.empty();
    }

    public void run() {
        for (programCounter = 0; programCounter < instructions.size(); programCounter++)
            instructions.get(programCounter).run();
    }
}