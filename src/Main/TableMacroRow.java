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
    public SimpleStringProperty macro = new SimpleStringProperty("");
    public SimpleStringProperty hotkey = new SimpleStringProperty("");
    public SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    File path;
    Integer firstKey;
    Integer secondKey;

    public TableMacroRow(File mac, Integer first, Integer sec, Boolean en) {
        setKeys(first, sec);
        path = mac;
        setMacro(mac.getName());
        setEnable(en);
    }

    public void setMacro(String mac) {
        this.macro.set(mac);
    }

    public String getMacro() {
        return this.macro.get();
    }

    public void setEnable(Boolean en) {
        this.enabled.set(en);
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
        firstKey = first;
        String hotk = "Not Set";
        if (firstKey != null) {
            hotk = KeyEvent.getKeyText(firstKey);
            if (sec != null) {
                secondKey = sec;
                hotk += "+" + KeyEvent.getKeyText(secondKey);
            }
        }
        this.hotkey.set(hotk);
    }

}
