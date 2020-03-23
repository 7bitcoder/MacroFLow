package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {
    static Main main = new Main();
    private String[] macroPaths;
    private ObservableList<TableMacroRow> rows = FXCollections.observableArrayList();
    File recent = new File("recent.rcnt");
    @FXML
    TableView<TableMacroRow> table;
    @FXML
    TableColumn<TableMacroRow, String> macro;
    @FXML
    TableColumn<TableMacroRow, String> hotkey;
    @FXML
    TableColumn<TableMacroRow, Boolean> enabled;

    private Main() {
        System.out.println("Main");
    }

    public void newMacro() {
        Controller.map.activate(Controller.Scenes.editor);
    }

    public void openMacro() {
        Editor.editor.openMacro();
        Controller.map.activate(Controller.Scenes.editor);
    }

    public void load() {
        try {
           /* Macro macro = new Macro("C:\\Users\\Sylwo\\Desktop\\macro2.mcr");
            macro.setKeys(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_D);
            macro.loadInstructions();
            macro.robot = new Robot();
            String msg = macro.readMacro(editArea.getText());
            if (msg != "")
                messages.setText(msg);
            else {

                MacrosListener.macrosListener.addMacro(macro);
                listener.list = new MacroListener(macro);
                listener.list.setKeys(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_D);
                listener.main();
            }*/
        } catch (Exception e) {
            //messages.setText(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //table.setItems();
        macro.setCellValueFactory(new PropertyValueFactory<>("macro"));
        hotkey.setCellValueFactory(new PropertyValueFactory<>("hotkey"));
        enabled.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        table.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        try {
            if (recent.exists()) {
                try (FileReader fileStream = new FileReader(recent);
                     BufferedReader bufferedReader = new BufferedReader(fileStream)) {
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        var values = line.split(" ");
                        rows.add(new TableMacroRow(values[0], values[1], Boolean.parseBoolean(values[2])));
                    }
                } catch (IOException ex) {
                    //messages.setText(String.format("Could Not Open File %s, Error: %s", file.getName(), ex.getMessage()));
                    recent.createNewFile();
                }
            } else {
                recent.createNewFile();
            }
            table.setItems(rows);
        } catch (IOException ex) {
            //messages.setText(String.format("Could Not Save File: Error %s", ex.getMessage()));
        }
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