package ControlInput;

import Instructions.Macro;
import javafx.util.Pair;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.util.HashMap;

public class Listener implements NativeKeyListener {
    public static class MacroListener {


        private Macro macro_;
        Keys keys;

        public MacroListener(Macro macro) {
            macro_ = macro;
        }

        public void setKeys(int first, int second) {
            keys = new Keys(first, second);
        }

        public void setKeys(int first) {
            keys = new Keys(first);
        }

        boolean KeyCheckPressed(int code) {
            if (keys.second == null) {
                if (keys.first.code == code) {
                    keys.first.state = Keys.State.pressed;
                    return true;
                }
            } else {
                if (keys.second.code == code) {
                    keys.second.state = Keys.State.pressed;
                    if (keys.first.state == Keys.State.pressed)
                        return true;
                }
                if (keys.first.code == code) {
                    keys.first.state = Keys.State.pressed;
                }
            }
            return false;
        }

        boolean KeyCheckReleased(int code) {
            if (keys.second == null) {
                if (keys.first.code == code) {
                    keys.first.state = Keys.State.released;
                    return true;
                }
            } else {
                if (keys.second.code == code) {
                    keys.second.state = Keys.State.released;
                }
                if (keys.first.code == code) {
                    keys.first.state = Keys.State.released;
                }
            }
            return false;
        }

        void runMacro() {
            if (!macro_.isRunning())
                macro_.runMacro();
        }
    }

    //todo macros hashmap
    public static MacroListener list;
    volatile boolean macroRunning = false;

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (list.KeyCheckReleased(e.getKeyCode()))
            ;
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (!macroRunning && list.KeyCheckPressed(e.getKeyCode())) {
            macroRunning = true;
            list.runMacro();
            macroRunning = false;
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public static void main() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Listener());
    }
}