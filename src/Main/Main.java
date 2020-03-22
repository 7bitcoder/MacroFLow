package Main;

import javafx.fxml.FXML;

public class Main {
    @FXML
    void newMacro() {
        Controller.map.activate(Controller.Scenes.editor);
    }

    @FXML
    public void openMacro() {
        Editor editor = (Editor)Controller.map.screenMap.get(Controller.Scenes.editor).controller;
        editor.openMacro();
        Controller.map.activate(Controller.Scenes.editor);
    }
    public Main() {
        System.out.println("Main");
    }
}
/*
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

 */