package ControlInput;

import Instructions.Macro;

public class MacroListener {

    public Macro macro_;
    Keys keys;

    public MacroListener(Macro macro) {
        macro_ = macro;
        keys = new Keys(macro.firstKey, macro.secondKey);
    }

    @Override
    public boolean equals(Object o) {
        // no need for (o instanceof Point) by design
        return keys.first.code == ((MacroListener) o).keys.first.code && keys.second.code == ((MacroListener) o).keys.second.code;
    }

    @Override
    public int hashCode() {
        return keys.first.code * 31 + keys.second.code;
    }

    boolean KeyCheckPressed(int code) {
        if (keys.second == null) {
            if (keys.first.code == code) {
                keys.first.press();
                return true;
            }
        } else {
            if (keys.second.code == code) {
                keys.second.press();
                if (keys.first.pressed())
                    return true;
            }
            if (keys.first.code == code) {
                keys.first.press();
            }
        }
        return false;
    }

    boolean KeyCheckReleased(int code) {
        if (keys.second != null && keys.second.code == code) {
            keys.second.release();
        }
        if (keys.first.code == code) {
            keys.first.release();
        }
        return false;
    }

    void runMacro() {
        if (!macro_.isRunning())
            macro_.runMacro();
    }
}
