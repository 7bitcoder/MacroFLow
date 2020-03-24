package Main;

import Instructions.Macro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
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
    @FXML
    TextArea messages;

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
           Macro macro = new Macro("C:\\Users\\Sylwo\\Desktop\\macro2.mcr");
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
            }
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
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //messages.setText(String.format("Could Not Save File: Error %s", ex.getMessage()));
        table.setItems(rows);
    }





    void addNewMacroFile(String path) {
        var file = new File(path);
        if (!file.exists())
            ;//todo

    }

    private void clearMsg() {
        messages.setText("");
    }

    static String separator = "\t";
    class MacrosHolder {
        class Data {
            Path path;
            Boolean enabled;
            Macro macro;
        }
        File recentFile = new File("recent.rcnt");
        Map<Path, Data> recentSet = new HashMap<Path, Data>();

        boolean resetRecentFile() throws IOException {
            if (recentFile.exists()) {
                PrintWriter writer = new PrintWriter(recentFile);
                writer.print("");
                writer.close();
            } else
                recentFile.createNewFile();
            return true;
        }

        void saveToRecent() {
            /*
            file format:
            path|enabled[|first|sec]
            .. etc
            */
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(recentFile);
                for (var dat : recentSet.entrySet()) {
                    var data = dat.getValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append(data.path.toString() + separator);
                    sb.append(data.enabled);
                    if(data.macro.firstKey != null)
                        sb.append(separator + data.macro.firstKey);
                    if(data.macro.secondKey != null)
                        sb.append(separator + data.macro.secondKey);
                    writer.println(sb.toString());
                }
            } catch (FileNotFoundException e) {
            } finally {
                writer.close();
            }
        }

        void load() {
           /* if (recentFile.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(recentFile));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    var values = line.split(" ");
                    File f = new File(values[0]);
                    var len = values.length;
                    if (len < 2 || len > 4 || !f.exists())
                        continue;
                    rows.add(new TableMacroRow(f, len == 3 ? Integer.parseInt(values[2]) : null, len == 4 ? Integer.parseInt(values[3]) : null, Boolean.parseBoolean(values[1])));
                }
            } else {
                recentFile.createNewFile();
            }
            if (!Files.exists(file))
                //throw new Exception(String.format("Loading failed: Recent macro file does not Exists: %s", file.toString()));
                if (recentFile.contains(path))
                    return false;
            recentFile.add(path);
            return true;*/
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