package Parser;

import ContrtolOutput.ExecutableInstructions;
import ContrtolOutput.Validator;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ReadMacroFile {

    public static void main(String[] args) throws IOException, Validator.ParserExcetption, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InterruptedException, AWTException {
        File file = new File("C:\\Users\\Sylwo\\Desktop\\test.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        var robot  = new Robot();
        ExecutableInstructions.robot = robot;
        MakeInstructions generator = new MakeInstructions();
        generator.loadInstructions();
        MacroExecution execList = new MacroExecution();
        while ((st = br.readLine()) != null) {
            execList.addInstruction(generator.makeInstruction(st.split(" ")));
        }
        execList.run();
    }
}
