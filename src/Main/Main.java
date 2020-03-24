package Main;

import ControlInput.HotkeyListener;
import Instructions.Macro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import org.jnativehook.mouse.NativeMouseEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main implements Initializable {
    static Main main = new Main();
    private String[] macroPaths;
    private MacrosHolder macrosHolder = new MacrosHolder();
    static final char separator = '\035';
    static File recentFile = new File("recent.rcnt");
    File recent = new File("recent.rcnt");
    MacrosHolder holder = new MacrosHolder();
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
    Button hotKeyArea;

    public void close() {
        holder.saveToRecent();
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

    private HotkeyListener.Key first, second;
    static boolean isHotKey = false;

    public void setHotKey() {
        if (!isHotKey) {
            if(table.getSelectionModel().getSelectedItems().size() > 1) {
                messages.setText("Select one Macro");
                return;
            }
            isHotKey = true;
            table.setDisable(true);
            hotKeyArea.setText("Press button\nto Stop");
            HotkeyListener.hotkeyListener.startListening();
        } else {
            hotKeyArea.setText("set  selected \nmacro HotKey");
            isHotKey = false;
            table.setDisable(false);
            var p = table.getSelectionModel().getSelectedItems().get(0);
            var value = holder.recentSet.get(p.macro.filePath.getAbsolutePath());
            value.setKeys(first.code, second.code);
            HotkeyListener.hotkeyListener.stopListening();
            holder.tableUpdate();
        }
    }

    public void setFirstHotKey(HotkeyListener.Key key) {
        first = key;
        //olny keyboard now
        hotkey.setText(KeyEvent.getKeyText(key.code));
    }

    public void setSecondHotKey(HotkeyListener.Key key) {
        second = key;
        //olny keyboard now
        hotkey.setText(KeyEvent.getKeyText(first.code) + "+" + KeyEvent.getKeyText(key.code));
        isHotKey = false;
        setHotKey();
    }

    private void setHotKeyImplementation() {

    }

    public void load() {
       /* try {
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
        }*/
    }

    public void enableSelected() {
        ArrayList<TableMacroRow> p = new ArrayList<TableMacroRow>(table.getSelectionModel().getSelectedItems());
        for (TableMacroRow res : p) {
            var value = holder.recentSet.get(res.macro.filePath.getAbsolutePath());
            value.setEnable(!value.getEnabled());
        }
        holder.tableUpdate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //table.setItems();
        macro.setCellValueFactory(new PropertyValueFactory<>("macroName"));
        hotkey.setCellValueFactory(new PropertyValueFactory<>("hotkey"));
        enabled.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        holder.load();
        holder.tableUpdate();
    }


    void addNewMacroFile(String path) {
        var file = new File(path);
        if (!file.exists())
            ;//todo
    }

    private void clearMsg() {
        messages.setText("");
    }


    class MacrosHolder {
        Map<String, TableMacroRow> recentSet = new HashMap<String, TableMacroRow>();

        public void tableUpdate() {
            table.setItems(FXCollections.observableArrayList(recentSet.values()));
            table.refresh();
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

        void addNewMacro(File file) {
            var abs = file.getAbsolutePath();
            if (recentSet.get(abs) == null)
                recentSet.put(abs, new TableMacroRow(new Macro(file), null, null, false));
            tableUpdate();
        }

        void validateData() {
            //check if files still exists
            recentSet.entrySet().removeIf(entry -> !Files.exists(Paths.get(entry.getKey())));
        }

        void saveToRecent() {
            /*
            file format:
            path|enabled[|first|sec]
            .. etc
            */
            try {
                if (!recentFile.exists())
                    recentFile.createNewFile();
                var writer = new PrintWriter(recentFile);
                for (var dat : recentSet.entrySet()) {
                    var data = dat.getValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append(data.macro.filePath.toString() + separator);
                    sb.append(data.macro.enable);
                    if (data.macro.firstKey != null)
                        sb.append(separator + data.macro.firstKey);
                    if (data.macro.secondKey != null)
                        sb.append(separator + data.macro.secondKey);
                    writer.println(sb.toString());
                }
                writer.close();
            } catch (Exception e) {
            }
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
                            first = (len == 3 ? Integer.parseInt(values[2]) : null);
                            second = (len == 4 ? Integer.parseInt(values[3]) : null);
                        } catch (Exception e) {
                            continue;
                        }
                        String abs = f.getAbsolutePath();
                        if (recentSet.get(abs) == null)
                            recentSet.put(abs, new TableMacroRow(new Macro(f), first, second, en));
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