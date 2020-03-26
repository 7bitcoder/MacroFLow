package Main;

import Instructions.Macro;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.awt.event.KeyEvent;

public class TableMacroRow {
    public SimpleStringProperty macroName = new SimpleStringProperty("");
    public SimpleStringProperty hotkey = new SimpleStringProperty("");
    public SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    Macro macro_;

    public TableMacroRow(Macro mac) {
        macro_ = mac;
        setKeys(macro_.getFirstKey(), macro_.getSecondtKey());
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

    public void setKeys(Integer first, Integer sec) {
        macro_.setKeys(first, sec);
        String hotk = "Not Set";
        if (first != null) {
            hotk = KeyEvent.getKeyText(first);
            if (sec != null) {
                hotk += "+" + KeyEvent.getKeyText(sec);
            }
        }
        setHotkey(hotk);
    }

    public Macro getMacro() {
        return macro_;
    }

    public boolean equalsMacro(Object obj) {
        return macro_.equals(((TableMacroRow) obj).macro_);
    }

    public boolean equalKeys(Integer fir, Integer sec) {
        return fir == macro_.getFirstKey() && sec == macro_.getSecondtKey();
    }
}
