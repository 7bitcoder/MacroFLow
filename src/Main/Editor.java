package Main;

import ControlInput.Listener;
import Instructions.MacroImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class Editor {
    String actualFile = null;

    public Editor() {
        System.out.println("editor");
    }

    // menu
    public void newMacro() {
        clearMsg();
        if (actualFile != null) {
            saveActual();
        }
        editor.setText("");
        saveNewFile();
    }

    public void saverSave() {
        clearMsg();
        if (actualFile == null) {
            saveNewFile();
        } else {
            saveActual();
        }
    }

    public void saveAs() {
        clearMsg();
        saveNewFile();
    }

    public void changeToMain() {
        Controller.map.activate(Controller.Scenes.main);
    }

    public void openMacro() {
        clearMsg();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MCR files (*.mcr)", "*.mcr");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(Controller.primaryStage);
        if (file != null) {
            try (FileReader fileStream = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileStream)) {
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                editor.setText(sb.toString());
            } catch (IOException ex) {
                messages.setText(String.format("Could Not Open File %s, Error: %s", file.getName(), ex.getMessage()));
            }
            actualFile = file.getAbsolutePath();
        }
    }

    public void close() {
        clearMsg();
        if (actualFile != null) {
            saveActual();
            editor.setText("");
            actualFile = null;
        }
    }

    @FXML
    TextArea messages;

    @FXML
    TextArea editor;

    //end editor

    @FXML
    Button saver;

    private void saveActual() {
        clearMsg();
        File file = new File(actualFile);
        saveTextToFile(editor.getText(), file);
    }

    private void saveNewFile() {
        clearMsg();
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MCR files (*.mcr)", "*.mcr");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(Controller.primaryStage);

        if (file != null) {
            saveTextToFile(editor.getText(), file);
        }
    }

    private void saveTextToFile(String content, File file) {
        clearMsg();
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            messages.setText(String.format("Could Not Save File: Error %s", ex.getMessage()));
        }
    }
    //end saver

    Button loader;

    @FXML
    void load() {
        clearMsg();
        try {
            MacroImpl macro = new MacroImpl();
            macro.loadInstructions();
            macro.robot = new Robot();
            String msg = macro.readMacro(editor.getText());
            if (msg != "")
                messages.setText(msg);
            else {
                var listener = new Listener();
                listener.list = new Listener.MacroListener(macro);
                listener.list.setKeys(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_D);
                listener.main();
            }
        } catch (Exception e) {
            messages.setText(e.getMessage());
        }
    }
    //end loader

    private void clearMsg() {
        messages.setText("");
    }
}
