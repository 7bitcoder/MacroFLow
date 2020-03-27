package Main;

import ControlInput.Keys;
import Instructions.Macro;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableMacroRow {
    public SimpleStringProperty macroName = new SimpleStringProperty("");
    public SimpleStringProperty hotkey = new SimpleStringProperty("");
    public SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    Macro macro_;

    public TableMacroRow(Macro mac) {
        macro_ = mac;
        updateHotKey();
        setMacroName(macro_.getName());
        setEnable(macro_.getEnable());
    }

    public void setMacroName(String mac) {
        this.macroName.set(mac);
    }

    public String getMacroName() {
        return this.macroName.get();
    }

    public void setEnable(Boolean en) {
        macro_.setEnable(en);
        enabled.set(en);
    }

    public Boolean getEnabled() {
        return enabled.get();
    }

    public SimpleBooleanProperty enabledProperty() {
        return enabled;
    }

    public void setHotkey(String hot) {
        this.hotkey.set(hot);
    }

    public String getHotkey() {
        return hotkey.get();
    }

    private void updateHotKey() {
        setHotkey(macro_.getHotKey().toString());
    }

    public Macro getMacro() {
        return macro_;
    }

    public void resetKeys() {
        macro_.getHotKey().reset();
        updateHotKey();
    }

    public void setKeys(Keys keys) {
        macro_.setKeys(keys);
        updateHotKey();
    }

    public boolean equalsMacro(Object obj) {
        return macro_.equals(((TableMacroRow) obj).macro_);
    }

    public boolean equalKeys(Integer fir, Integer sec) {
        return equalKeys(new Keys(fir, sec));
    }

    public boolean equalKeys(Keys keys) {
        return macro_.getHotKey().equals(keys);
    }

    public boolean equalKeys(Macro mac) {
        return macro_.equalKeys(mac);
    }
}
