package ControlInput;

import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HotkeyListener {
    private volatile boolean isWorking = false;

    public interface Invoker {

        void writeRes(Keys key);

        void stop();
    }

    public HotkeyListener() {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
    }

    Invoker invoker;

    Keys keys = new Keys();
    Keys.Key first = keys.getFirst();
    Keys.Key second = keys.getSecond();
    Keyboard keyboard = new Keyboard();
    Mouse mouse = new Mouse();

    public void lock() {
        isWorking = true;
    }

    public boolean isLocked() {
        return isWorking;
    }

    public void reset() {
        keys.reset();
        invoker = null;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    class Keyboard implements NativeKeyListener {
        @Override
        public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            Platform.runLater(() -> {
                int keyCode = nativeKeyEvent.getKeyCode();
                if (!first.isSet()) {
                    first.set(keyCode);
                    invoker.writeRes(keys);
                } else if (!second.isSet() && keyCode != first.get()) {
                    second.set(keyCode);
                    invoker.writeRes(keys);
                    invoker.stop();
                }
            });

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
        }
        GlobalScreen.addNativeKeyListener(keyboard);
        //GlobalScreen.addNativeMouseListener(mouse);
    }

    public void stopListening() {
        GlobalScreen.removeNativeKeyListener(keyboard);
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        reset();
        isWorking = false;
        //GlobalScreen.removeNativeMouseListener(mouse);
    }
}
