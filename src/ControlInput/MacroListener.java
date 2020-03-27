package ControlInput;

import Instructions.Macro;

public class MacroListener {

    public Macro macro_;
    public Keys keys;
    public Keys.Key first;
    public Keys.Key second;

    public MacroListener(Macro macro) {
        macro_ = macro;
        keys = macro_.getHotKey();
        first = keys.getFirst();
        second = keys.getSecond();
    }

    @Override
    public boolean equals(Object o) {
        return macro_.equalKeys(((MacroListener) o).macro_);
    }

    @Override
    public int hashCode() {
        return keys.getFirst().get() * 31 + keys.getSecond().get();
    }

    boolean KeyCheckPressed(int code) {
        if (!second.isSet()) {
            if (first.equals(code)) {
                first.press();
                return true;
            }
        } else {
            if (second.equals(code)) {
                second.press();
                if (first.pressed())
                    return true;
            }
            if (first.equals(code)) {
                first.press();
            }
        }
        return false;
    }

    boolean KeyCheckReleased(int code) {
        if (second.isSet() && second.equals(code)) {
            second.release();
        }
        if (first.isSet() && first.equals(code)) {
            first.release();
        }
        return false;
    }

    void runMacro() {
        if (!macro_.isRunning())
            macro_.runMacro();
    }
}
