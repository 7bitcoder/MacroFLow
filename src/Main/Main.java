package Main;

import ControlInput.MacrosListener;
import Instructions.Macro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Main implements Initializable {
    static Main main = new Main();
    private String[] macroPaths;
    static final char separator = '\035';
    static File recentFile = new File("recent.rcnt");
    File recent = new File("recent.rcnt");
    @FXML
    MacrosManager macrosManager = new MacrosManager();
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

    public void close() {
        macrosManager.saveToRecent();
    }

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

    Boolean isListening = false;

    public void startListening() {
        if (isListening) {
            MacrosListener.macrosListener.stopListening();
            isListening = false;
        } else {
            for (var row : table.getItems()) {
                if (row.getEnabled()) {
                    if (row.equalKeys(null, null)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hot key not set");
                        alert.setHeaderText(null);
                        alert.setContentText(String.format("Hot key of macro '%s' is not set", row.getMacro().getName()));
                        alert.showAndWait();
                        return;
                    } else {
                        var macro = row.getMacro();
                        var msg = macro.readMacro();
                        if (msg != null) {
                            messages.setText(msg);
                            return;
                        }
                        MacrosListener.macrosListener.addMacro(macro);
                    }
                }
            }
            MacrosListener.macrosListener.startListening();
            isListening = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //table.setItems();
        try {
            Macro.loadInstructions();
            Macro.robot_ = new Robot();
        } catch (Exception e) {
            messages.setText(e.getMessage());
        }
        hotkey.setCellFactory(TableFactories.hotKeyFactory);
        enabled.setCellFactory(TableFactories.EnableFactory);
        macro.setCellValueFactory(new PropertyValueFactory<TableMacroRow, String>("macroName"));
        hotkey.setCellValueFactory(new PropertyValueFactory<TableMacroRow, String>("hotkey"));
        enabled.setCellValueFactory(new PropertyValueFactory<TableMacroRow, Boolean>("enabled"));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        macrosManager.load();
        macrosManager.validateData();
    }


    void addNewMacroFile(String path) {
        var file = new File(path);
        if (!file.exists())
            ;//todo
    }

    private void clearMsg() {
        messages.setText("");
    }


    class MacrosManager {

        private TableMacroRow makeRow(Macro mc) {
            return new TableMacroRow(mc);
        }

        boolean resetRecentFile() throws IOException {
            if (recentFile.exists()) {
                PrintWriter writer = new PrintWriter(recentFile);
                writer.print("");
                writer.close();
            } else
                recentFile.createNewFile();
            return true;
        }

        public void saveToRecent() {
            /*
            file format:
            path|enabled[|first|sec]
            .. etc
            */
            try {
                if (!recentFile.exists())
                    recentFile.createNewFile();
                var writer = new PrintWriter(recentFile);
                for (var row : table.getItems()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(row.getMacro().getPath().toString() + separator);
                    sb.append(row.getEnabled());
                    if (row.getMacro().getFirstKey() != null)
                        sb.append(separator + row.getMacro().getFirstKey().toString());
                    if (row.getMacro().getSecondtKey() != null)
                        sb.append(separator + row.getMacro().getSecondtKey().toString());
                    writer.println(sb.toString());
                }
                writer.close();
            } catch (Exception e) {
            }
        }

        void addNewMacro(File file) {
            addMacro(file, null, null, null);
        }

        void addMacro(File file, Integer first, Integer sec, Boolean en) {
            var macro = new Macro(file, first, sec, en);
            boolean found = false;
            for (var row : table.getItems()) {
                if (row.getMacro().equals(macro))
                    found = true;
            }
            if (!found)
                table.getItems().add(makeRow(macro));
            table.refresh();
        }

        void validateData() {
            //check if files still exists
            ArrayList<TableMacroRow> toDelete = new ArrayList<TableMacroRow>();
            for (var row : table.getItems()) {
                if (!row.getMacro().getFile().exists())
                    toDelete.add(row);
            }
            for (var del : toDelete)
                table.getItems().remove(del);
        }


        void load() {
            try {
                if (recentFile.exists()) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(recentFile));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        var values = line.split(String.valueOf(separator));
                        File f = new File(values[0]);
                        var len = values.length;
                        if (len < 2 || len > 4 || !f.exists())
                            continue;
                        Integer first, second;
                        Boolean en;
                        try {
                            en = Boolean.parseBoolean(values[1]);
                            first = (len >= 3 ? Integer.parseInt(values[2]) : null);
                            second = (len == 4 ? Integer.parseInt(values[3]) : null);
                        } catch (Exception e) {
                            continue;
                        }
                        addMacro(f, first, second, en);
                    }
                } else {
                    recentFile.createNewFile();
                }
            } catch (Exception e) {
                messages.setText(String.format("file %s error, delete corrupted file", recentFile.getName()));
            }
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