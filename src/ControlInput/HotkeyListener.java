package ControlInput;

import ContrtolOutput.Keyboard;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class HotkeyListener {
    public static HotkeyListener hotkeyListener = new HotkeyListener();
    public static class Key {
        public enum Type {keyboard, mouse}

        public Type type = Type.mouse;
        public Integer code;
    }

    Key first, second;
    Keyboard keyboard = new Keyboard();
    Mouse mouse = new Mouse();

    public void reset() {
        keyboard = null;
        mouse = null;
    }

    class Keyboard implements NativeKeyListener {
        @Override
        public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

            if (first == null) {
                first = new Key();
                first.code = nativeKeyEvent.getKeyCode();
                first.type = Key.Type.keyboard;
            } else if (second == null) {
                second = new Key();
                second.code = nativeKeyEvent.getKeyCode();
                second.type = Key.Type.keyboard;
                startListening();
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        }
    }

    class Mouse implements NativeMouseListener {

        @Override
        public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {

        }

        @Override
        public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
            if (first == null) {
                first = new Key();
                first.code = nativeMouseEvent.getButton();
                first.type = Key.Type.mouse;
            } else if (second == null) {
                second = new Key();
                second.code = nativeMouseEvent.getButton();
                second.type = Key.Type.mouse;
                startListening();
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {

        }
    }

    public void startListening() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(keyboard);
        //GlobalScreen.addNativeMouseListener(mouse);
    }

    public void stopListening() {
        GlobalScreen.removeNativeKeyListener(keyboard);
        //GlobalScreen.removeNativeMouseListener(mouse);
    }
}
