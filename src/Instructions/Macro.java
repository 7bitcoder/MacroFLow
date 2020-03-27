package Instructions;

import ControlInput.Keys;
import ControlOutput.Validator;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Macro implements IMacro {
    static MakeInstructions generator_ = new MakeInstructions();
    private Executor execList_ = new Executor();
    private volatile boolean running_ = false;
    public static Robot robot_ = null;
    private String name_;
    private File file_;
    private Path path_;
    private Keys hotKey = new Keys();
    private boolean enable_ = false;

    static {
        ControlOutput.Keyboard.robot =
                ControlOutput.System.robot =
                        ControlOutput.Mouse.robot = robot_;
    }

    public Macro(File file, Integer fir, Integer sec, Boolean en) {
        file_ = file;
        path_ = file.toPath();
        name_ = file.getName();
        setEnable(en);
        setKeys(fir, sec);
    }

    public void setKeys(Integer f, Integer s) {
        hotKey.setKeys(f, s);
    }

    public void setEnable(Boolean en) {
        if (en == null)
            enable_ = false;
        else
            enable_ = en;
    }

    public Keys getHotKey() {
        return hotKey;
    }

    public Keys.Key getFirstKey() {
        return hotKey.getFirst();
    }

    public Keys.Key getSecondtKey() {
        return hotKey.getSecond();
    }

    public Boolean getEnable() {
        return enable_;
    }

    public String getName() {
        return name_;
    }

    public Path getPath() {
        return path_;
    }

    public File getFile() {
        return file_;
    }

    @Override
    public boolean equals(Object obj) {
        return path_.equals(obj);
    }

    public boolean equalKeys(Macro mac) {
        return hotKey.equals(mac.getHotKey());
    }

    public boolean emptyKeys() {
        return !getFirstKey().isSet() && !getSecondtKey().isSet();
    }

    public static void loadInstructions() throws Exception {
        generator_.loadInstructions();
    }

    public String readMacro() {
        FileReader fileStream;
        try {
            fileStream = new FileReader(file_);
        } catch (FileNotFoundException e) {
            return e.getMessage();
        }
        BufferedReader bufferedReader = new BufferedReader(fileStream);
        return read(bufferedReader);
    }

    public String readMacro(String instructions) {
        BufferedReader br;
        Reader inputString = new StringReader(instructions);
        br = new BufferedReader(inputString);
        return read(br);
    }

    private String read(BufferedReader br) {
        String st;
        int index = 0;
        try {
            while ((st = br.readLine()) != null) {
                index++;
                execList_.addInstruction(generator_.makeInstruction(st.split(" ")));
            }
        } catch (Validator.ParserExcetption e) {
            return String.format("Macro: %s, line: %d. %s", name_, index, e.getMessage());
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

    public boolean isRunning() {
        return running_;
    }

    synchronized public void runMacro() {
        running_ = true;
        execList_.run();
        running_ = false;
    }

}

class MakeInstructions {
    public Executor.Instruction makeInstruction(String[] args) throws Validator.ParserExcetption, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args.length == 0)
            throw new Validator.ParserExcetption("Line is empty");
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
