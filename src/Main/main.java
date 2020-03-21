package Main;
import ControlInput.Listener;
import Instructions.MacroImpl;
import java.awt.*;
import org.jnativehook.keyboard.NativeKeyEvent;


public class main {
    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\Sylwo\\Desktop\\test.txt";
        MacroImpl macro = new MacroImpl();
        macro.loadInstructions();
        macro.robot = new Robot();
        macro.readMacro(path);
        var listener = new Listener();
        listener.list = new Listener.MacroListener(macro);
        listener.list.setKeys(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_D);
        listener.main();
    }
}