package Instructions;

import ContrtolOutput.ExecutableInstructions;
import ContrtolOutput.Validator;

import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.io.*;

public class MacroImpl implements Macro {
    public static Robot robot = null;
    static MakeInstructions generator = new MakeInstructions();
    public String filePath;

    public static void loadInstructions() throws Exception {
        generator.loadInstructions();
    }

    volatile boolean running = false;
    MacroExecution execList = new MacroExecution();

    public String readMacro(String instructions) {
        BufferedReader br;
        Reader inputString = new StringReader(instructions);
        br = new BufferedReader(inputString);
        String st;
        ExecutableInstructions.robot = robot;
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
