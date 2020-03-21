package Instructions;

import ContrtolOutput.ExecutableInstructions;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MacroImpl implements Macro {
    public static Robot robot = null;
    static MakeInstructions generator = new MakeInstructions();

    public static void loadInstructions() throws Exception {
        generator.loadInstructions();
    }

    volatile boolean running = false;
    MacroExecution execList = new MacroExecution();

    public String readMacroFile(String path) {
        File file;
        BufferedReader br;
        try {
            file = new File(path);
            br = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            return "Could not Read macro file " + path;
        }
        String st;
        ExecutableInstructions.robot = robot;
        try {

            while ((st = br.readLine()) != null)
                execList.addInstruction(generator.makeInstruction(st.split(" ")));
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
