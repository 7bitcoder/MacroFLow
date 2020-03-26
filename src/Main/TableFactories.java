package Main;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;

public class TableFactories {
    static ToggleGroup tg = new ToggleGroup();

    static Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>> hotKeyFactory = new Callback<TableColumn<TableMacroRow, String>, TableCell<TableMacroRow, String>>() {
        @Override
        public TableCell<TableMacroRow, String> call(final TableColumn<TableMacroRow, String> param) {
            final TableCell<TableMacroRow, String> cell = new TableCell<TableMacroRow, String>() {

                private final ToggleButton btn = new ToggleButton("Action");
                Integer first = null;
                Integer second = null;
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
                    listening = true;
                    first = second = null;
                    writeResult();
                    btn.setOnKeyPressed(ke -> {
                        if (!btn.isSelected()) {
                            stopListening();
                        }
                        Integer keyCode = ke.getCode().getCode();
                        if (first == null) {
                            first = keyCode;
                            writeResult();
                        } else if (second == null && keyCode != first) {
                            second = keyCode;
                            stopListening();
                        }
                    });
                }

                void stopListening() {
                    if (listening) {
                        validate();
                        writeResult();
                        btn.setOnKeyPressed(null);
                        first = second = null;
                        listening = false;
                    }
                }

                void writeResult() {
                    TableMacroRow data = getTableView().getItems().get(getIndex());
                    System.out.println(String.format("%d %d", first, second));
                    data.setKeys(first, second);
                    btn.setText(data.getHotkey());
                }

                boolean validate() {
                    TableMacroRow data = getTableView().getItems().get(getIndex());
                    if (data.equalKeys(first, second))
                        return true;
                    for (var macroRow : getTableView().getItems()) {
                        if (macroRow.equalKeys(first, second)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Wrong HotKey");
                            alert.setHeaderText(null);
                            alert.setContentText("Hot key is already set for another macro");
                            alert.showAndWait();
                            second = first = null;
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
}