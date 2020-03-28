package Main;

import ControlInput.MacrosListener;
import Instructions.Macro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

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
    @FXML
    ToggleButton listenButton;

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
        clearMsg();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Macro File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MCR files (*.mcr)", "*.mcr");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(Controller.primaryStage);
        macrosManager.addNewMacro(file);
    }

    public void editMacro() {
        var data = Main.main.table.getSelectionModel().getSelectedItems();
        if (data.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Macro not selected");
            alert.setHeaderText(null);
            alert.setContentText("Select macro to edit");
            alert.showAndWait();
            return;
        }
        Editor.editor.openMacro(data.get(0).macro_.getFile());
        Controller.map.activate(Controller.Scenes.editor);
    }

    public void helpInstructions() {

    }

    Boolean isListening = false;

    public void startListening() {
        if (isListening) {
            MacrosListener.macrosListener.stopListening();
            stopListen();
        } else {
            for (var row : table.getItems()) {
                if (row.getEnabled()) {
                    if (row.getMacro().emptyKeys()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hot key not set");
                        alert.setHeaderText(null);
                        alert.setContentText(String.format("Hot key of macro '%s' is not set", row.getMacro().getName()));
                        alert.showAndWait();
                        stopListen();
                        return;
                    } else {
                        var macro = row.getMacro();
                        var msg = macro.readMacro();
                        if (msg != null) {
                            messages.setText(msg);
                            stopListen();
                            return;
                        }
                        MacrosListener.macrosListener.addMacro(macro);
                    }
                }
            }
            MacrosListener.macrosListener.startListening();
            startListen();
        }
    }

    private void stopListen() {
        isListening = false;
        listenButton.setSelected(false);
    }

    private void startListen() {
        isListening = true;
        listenButton.setSelected(true);
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
        macro.setCellFactory(TableCellFactories.NameFactory);
        hotkey.setCellFactory(TableCellFactories.hotKeyFactory);
        enabled.setCellFactory(TableCellFactories.EnableFactory);
        macro.setCellValueFactory(new PropertyValueFactory<>("macroName"));
        hotkey.setCellValueFactory(new PropertyValueFactory<>("hotkey"));
        enabled.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
                    if (row.getMacro().getFirstKey().isSet())
                        sb.append(separator + row.getMacro().getFirstKey().get().toString());
                    if (row.getMacro().getSecondtKey().isSet())
                        sb.append(separator + row.getMacro().getSecondtKey().get().toString());
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
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        var values = line.split(String.valueOf(separator));
                        File f = new File(values[0]);
                        var len = values.length;
                        if (len < 2 || len > 4 || !f.exists())
                            continue;
                        Integer first, second;
                        boolean en;
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