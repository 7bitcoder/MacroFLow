package Instructions;

import ControlOutput.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class MakeInstructions {
    public Executor.Instruction makeInstruction(String[] args, Executor exec) throws Validator.ParserExcetption, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args.length == 0)
            throw new Validator.ParserExcetption("Line is empty");
        var instruction = getInstruction(args[0]); //name
        Executor.Instruction instance = instruction.getDeclaredConstructor().newInstance();
        instance.init(args, exec);
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
            Class[] classes = new Class[]{ControlOutput.Keyboard.class, ControlOutput.Mouse.class, ControlOutput.System.class};
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
