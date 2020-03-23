package Main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;

public class TableMacroRow {
    public SimpleStringProperty macro;
    public SimpleStringProperty hotkey;
    public SimpleBooleanProperty enabled;
    File path;
    Integer firstKey;
    Integer secondKey;

    public TableMacroRow(String mac, String hotk, Boolean en) {
        this.macro = new SimpleStringProperty(mac);
        this.hotkey = new SimpleStringProperty(hotk);
        this.enabled = new SimpleBooleanProperty(en);
    }

    public void setMacro(String mac) {
        this.macro = new SimpleStringProperty(mac);
    }

    public String getMacro() {
        return this.macro.get();
    }

    public void setEnable(Boolean en) {
        this.enabled = new SimpleBooleanProperty(en);
    }

    public Boolean getEnabled() {
        return enabled.get();
    }

    public void setHotkey(String hot) {
        this.hotkey = new SimpleStringProperty(hot);
    }

    public String getHotkey() {
        return hotkey.get();
    }

    /*public void setHotkey(Integer first, Integer sec) {
        firstKey = first;
        String hotk = KeyEvent.getKeyText(firstKey);
        if (sec != null) {
            secondKey = sec;
            hotk += "+" + KeyEvent.getKeyText(secondKey);
        }
        this.hotkey = new SimpleStringProperty(hotk);
    }*/
}
