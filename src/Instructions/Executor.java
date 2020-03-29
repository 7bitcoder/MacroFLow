package Instructions;

import ControlOutput.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class Executor {
    static MakeInstructions generator_ = new MakeInstructions();

    public static void loadInstructions() throws Exception {
        generator_.loadInstructions();
    }

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
        programCounter = i - 1;
    }

    public int getPc() {
        return programCounter;
    }

    public int getInstructionIndex() {
        return instructions.size();
    }

    void addInstruction(Instruction exec) {
        ;
    }

    private void check() throws Validator.ParserExcetption {
        if (!brackets.empty())
            throw new Validator.ParserExcetption("Closing commands error, every control commands have to be closed by instructions endloop/endif");
        for (var entry : JumpLabels.entrySet())
            if (entry.getValue() == -1)
                throw new Validator.ParserExcetption(String.format("Requested jump label '%s' is not defined", entry.getKey()));
    }

    public void run() {
        for (programCounter = 0; programCounter < instructions.size(); programCounter++)
            instructions.get(programCounter).run();
    }

    public void reset() {
        variables.clear();
        JumpLabels.clear();
        instructions.clear();
        bracketIndex = 0;
        brackets.clear();
        programCounter = 0;
    }

    public String readInstructions(BufferedReader br) {
        String st;
        try {
            while ((st = br.readLine()) != null) {
                instructions.add(generator_.makeInstruction(st.split(" "), this));
            }
            check();
        } catch (Validator.ParserExcetption e) {
            return String.format("line: %d. %s", getInstructionIndex() + 1, e.getMessage());
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                return "Could not close properly macro file";
            }
        }
        return null;
    }
}