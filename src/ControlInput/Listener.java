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
        static enum State {
            pressed, released;
        }

        class Key {
            int code;
            State state = State.released;

            Key(int c) {
                code = c;
            }
        }

        private Macro macro_;

        static class Keys {
            Key first;
            Key second;
        }

        Keys keys;

        public MacroListener(Macro macro) {
            macro_ = macro;
        }

        public void setKeys(int first, int second) {
            keys = new Keys();
            keys.first = new Key(first);
            keys.second = new Key(second);
        }

        public void setKeys(int first) {
            keys = new Keys();
            keys.first = new Key(first);
        }

        boolean KeyCheckPressed(int code) {
            if (keys.second == null) {
                if (keys.first.code == code) {
                    keys.first.state = State.pressed;
                    return true;
                }
            } else {
                if (keys.second.code == code) {
                    keys.second.state = State.pressed;
                    if (keys.first.state == State.pressed)
                        return true;
                }
                if (keys.first.code == code) {
                    keys.first.state = State.pressed;
                }
            }
            return false;
        }

        boolean KeyCheckReleased(int code) {
            if (keys.second == null) {
                if (keys.first.code == code) {
                    keys.first.state = State.released;
                    return true;
                }
            } else {
                if (keys.second.code == code) {
                    keys.second.state = State.released;
                }
                if (keys.first.code == code) {
                    keys.first.state = State.released;
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

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (list.KeyCheckReleased(e.getKeyCode()))
            ;
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (list.KeyCheckPressed(e.getKeyCode()))
            list.runMacro();
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