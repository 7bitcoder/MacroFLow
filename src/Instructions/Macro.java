package Instructions;

import ContrtolOutput.ExecutableInstructions;
import ContrtolOutput.Validator;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.Buffer;
import java.nio.file.Path;

public interface Macro {

    public void runMacro();
    public boolean isRunning();
    public String readMacro(String path);

}

