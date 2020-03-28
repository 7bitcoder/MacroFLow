package Main;

import ControlInput.HotkeyListener;
import ControlInput.Keys;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class TableCellFactories {
    static ToggleGroup tg = new ToggleGroup();
    private static HotkeyListener hotkeyListener = new HotkeyListener();
    static Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>> hotKeyFactory = new Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>>() {
        @Override
        public TableCell<TableMacroRow, String> call(final TableColumn<TableMacroRow, String> param) {
            final TableCell<TableMacroRow, String> cell = new TableCell<TableMacroRow, String>() {

                private final ToggleButton btn = new ToggleButton("Action");
                Boolean listening = false;

                {
                    btn.setToggleGroup(tg);
                    btn.setOnMouseClicked(e -> {
                        if (btn.isSelected()) {
                            startListening();
                        } else {
                            stopListening();
                        }
                    });
                }

                void startListening() {
                    hotkeyListener.lock();
                    hotkeyListener.reset();
                    hotkeyListener.setInvoker(new HotkeyListener.Invoker() {
                        @Override
                        public void writeRes(Keys key) {
                            writeResult(key);
                        }

                        @Override
                        public void stop() {
                            stopListening();
                        }
                    });
                    hotkeyListener.startListening();
                    listening = true;
                }

                synchronized void stopListening() {
                    if (listening) {
                        validate();
                        hotkeyListener.stopListening();
                        listening = false;
                    }
                }

                synchronized void writeResult(Keys hotkey) {
                    TableMacroRow data = getTableView().getItems().get(getIndex());
                    System.out.println(String.format("%d %d", hotkey.getFirst().get(), hotkey.getSecond().get()));
                    data.setKeys(hotkey);
                    btn.setText(data.getHotkey());
                }

                boolean validate() {
                    TableMacroRow data = getTableView().getItems().get(getIndex());
                    var hotkey = data.getMacro().getHotKey();
                    for (var macroRow : getTableView().getItems()) {
                        if (data != macroRow && macroRow.equalKeys(hotkey)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Wrong HotKey");
                            alert.setHeaderText(null);
                            alert.setContentText(String.format("Hot key is already set for macro '%s", macroRow.getMacroName()));
                            alert.showAndWait();
                            data.resetKeys();
                            btn.setText(data.getHotkey());
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        btn.setText(item);
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        }
    };

    static Callback<TableColumn<TableMacroRow, Boolean>, TableCell<TableMacroRow, Boolean>> EnableFactory = new Callback<TableColumn<TableMacroRow, Boolean>, TableCell<TableMacroRow, Boolean>>() {
        @Override
        public TableCell<TableMacroRow, Boolean> call(final TableColumn<TableMacroRow, Boolean> param) {
            final TableCell<TableMacroRow, Boolean> cell = new TableCell<TableMacroRow, Boolean>() {

                private final CheckBox btn = new CheckBox();

                {
                    btn.setOnAction((ActionEvent event) -> {
                        TableMacroRow data = getTableView().getItems().get(getIndex());
                        data.setEnable(btn.isSelected());
                    });
                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        btn.setSelected(item);
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        }
    };

    static Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>> NameFactory = new Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>>() {
        @Override
        public TableCell<TableMacroRow, String> call(final TableColumn<TableMacroRow, String> param) {
            final TableCell<TableMacroRow, String> cell = new TableCell<TableMacroRow, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };

            cell.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (e.getClickCount() == 2) {
                        Main.main.editMacro();
                    }
                }
            });
            return cell;
        }
    };
}