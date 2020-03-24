package Instructions;

import ContrtolOutput.Keyboard;
import ContrtolOutput.Mouse;
import ContrtolOutput.Validator;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Macro implements IMacro {
    public static Robot robot = null;
    static MakeInstructions generator = new MakeInstructions();
    public File filePath;
    private Executor execList = new Executor();
    volatile boolean running = false;
    public boolean enable = false;
    public Integer firstKey;
    public Integer secondKey;

    public Macro(File path) {
        filePath = path;
    }

    public void setKeys(Integer f, Integer s) {
        firstKey = f;
        secondKey = s;
    }

    public static void loadInstructions() throws Exception {
        generator.loadInstructions();
    }

    public String readMacro(String instructions) {
        BufferedReader br;
        Reader inputString = new StringReader(instructions);
        br = new BufferedReader(inputString);
        String st;
        Keyboard.robot = robot;
        int index = 0;
        try {
            while ((st = br.readLine()) != null) {
                index++;
                execList.addInstruction(generator.makeInstruction(st.split(" ")));
            }
        } catch (Validator.ParserExcetption e) {
            return String.format("Line: %d. %s", index, e.getMessage());
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                return "Could not close properly macro file";
            }
        }
        return "";
    }

    public boolean isRunning() {
        return running;
    }

    synchronized public void runMacro() {
        running = true;
        execList.run();
        running = false;
    }

}

class MakeInstructions {
    public Executor.Instruction makeInstruction(String[] args) throws Validator.ParserExcetption, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args.length == 0)
            throw new Validator.ParserExcetption(String.format("Line is empty: %s", args[0]));
        var instruction = getInstruction(args[0]); //name
        Executor.Instruction instance = instruction.getDeclaredConstructor().newInstance();
        instance.init(args);
        return instance;
    }

    private Class<? extends Executor.Instruction> getInstruction(String name) throws Validator.ParserExcetption {
        var instr = availableInstructions.get(name.toLowerCase());
        if (instr == null)
            throw new Validator.ParserExcetption(String.format("Wrong macro instruction name %s", name));
        return instr;
    }

    public void loadInstructions() throws Exception {
        try {
            Class[] classes = new Class[]{Keyboard.class, Mouse.class, System.class};
            for (var cl : classes) {
                var innerClasses = cl.getClasses();
                for (var iCl : innerClasses)
                    availableInstructions.put(iCl.getSimpleName().toLowerCase(), (Class<? extends Executor.Instruction>) iCl);
            }
        } catch (Exception e) {
            throw new Exception("Could not load available instructions");
        }
    }

    Map<String, Class<? extends Executor.Instruction>> availableInstructions = new HashMap<String, Class<? extends Executor.Instruction>>();
}
