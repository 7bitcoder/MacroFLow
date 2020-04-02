package Instructions;

public interface IMacro {

    public void runMacro() throws InterruptedException;

    public boolean isRunning();

    public String readMacro(String path);

    public String readMacro();

}

