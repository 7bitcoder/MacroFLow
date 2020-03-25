package Main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.awt.event.KeyEvent;
import java.io.File;

public class TableMacroRow {
    public SimpleStringProperty macroName = new SimpleStringProperty("");
    public SimpleStringProperty hotkey = new SimpleStringProperty("");
    public SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    String path;

    public TableMacroRow(File mac, Integer first, Integer sec, Boolean en) {
        path = mac.getAbsolutePath();
        setKeys(first, sec);
        setMacroName(mac.getName());
        setEnable(en);
    }

    public void setMacroName(String mac) {
        this.macroName.set(mac);
    }

    public String getMacroName() {
        return this.macroName.get();
    }

    public void setEnable(Boolean en) {
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
        this.hotkey.set(hotk);
    }

    void setPath(File f) {
        path = f.getAbsolutePath();
    }

    String getPath() {
        return path;
    }

}
