package Instructions;

import ControlInput.Keys;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;

public class Macro implements IMacro {
    private Executor executor_ = new Executor();

    private volatile boolean running_ = false;
    public static Robot robot_;

    private String name_;
    private File file_;
    private Path path_;
    private Keys hotKey = new Keys();
    private boolean enable_ = false;

    static {
        try {
            robot_ = new Robot();
        } catch (AWTException e) {
            System.exit(1);
        }
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

    public void setKeys(Keys keys) {
        setKeys(keys.getFirst().get(), keys.getSecond().get());
    }

    public void setKeys(Integer fir, Integer sec) {
        hotKey.setKeys(fir, sec);
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

    synchronized public String getName() {
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
        return path_.equals(((Macro) obj).path_);
    }

    public boolean equalKeys(Macro mac) {
        return hotKey.equals(mac.getHotKey());
    }

    public boolean emptyKeys() {
        return !getFirstKey().isSet() && !getSecondtKey().isSet();
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
        executor_.reset();
        String msg = executor_.readInstructions(br);
        if (msg != null)
            return String.format("Macro: %s. ", msg);
        return msg;
    }

    public boolean isRunning() {
        return running_;
    }

    synchronized public void runMacro() throws InterruptedException {
        running_ = true;
        executor_.run();
        running_ = false;
    }

}

