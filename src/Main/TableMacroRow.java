package Main;

import Instructions.Macro;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TableMacroRow {
    public SimpleStringProperty macroName = new SimpleStringProperty("");
    public SimpleStringProperty hotkey = new SimpleStringProperty("");
    public SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    Macro macro;

    public TableMacroRow(Macro mac, Integer first, Integer sec, Boolean en) {
        macro = mac;
        setKeys(first, sec);
        setMacroName( macro.filePath.getName());
        setEnable(en);
    }

    public void setMacroName(String mac) {
        this.macroName.set(mac);
    }

    public String getMacroName() {
        return this.macroName.get();
    }

    public void setEnable(Boolean en) {
        macro.enable = en;
        enabled.set(en);
    }

    public Boolean getEnabled() {
        return enabled.get();
    }

    public void setHotkey(String hot) {
        this.hotkey.set(hot);
    }

    public String getHotkey() {
        return hotkey.get();
    }

    public void setKeys(Integer first, Integer sec) {
        String hotk = "Not Set";
        if (first != null) {
            hotk = KeyEvent.getKeyText(first);
            if (sec != null) {
                hotk += "+" + KeyEvent.getKeyText(sec);
            }
        }
        macro.setKeys(first, sec);
        this.hotkey.set(hotk);
    }

}
