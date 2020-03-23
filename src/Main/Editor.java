package Main;

import ControlInput.MacroListener;
import ControlInput.MacrosListener;
import Instructions.Macro;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.io.*;

public class Editor {
    static Editor editor = new Editor();
    String actualFile = null;
    @FXML
    TextArea messages;
    @FXML
    TextArea editArea;
    @FXML
    Button saver;
    @FXML
    Button loader;

    private Editor() {
        System.out.println("editor");
    }

    // menu
    public void newMacro() {
        clearMsg();
        if (actualFile != null) {
            saveActual();
        }
        editArea.setText("");
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
                editArea.setText(sb.toString());
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
            editArea.setText("");
            actualFile = null;
        }
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
            saveTextToFile(editArea.getText(), file);
           // Main.main.
        }
    }

    private void clearMsg() {
        messages.setText("");
    }

    //end menu
    // load button

    public void validate() {
        clearMsg();
        try {
            Macro macro = new Macro(actualFile);
            macro.loadInstructions();
            macro.robot = new Robot();
            String msg = macro.readMacro(editArea.getText());
            if (msg != "")
                messages.setText(msg);
        } catch (Exception e) {
            messages.setText(e.getMessage());
        }
    }

    //end load button
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

    private void saveActual() {
        clearMsg();
        File file = new File(actualFile);
        saveTextToFile(editArea.getText(), file);
    }
}
