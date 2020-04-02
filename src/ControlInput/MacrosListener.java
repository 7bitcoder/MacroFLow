package ControlInput;

import Instructions.Macro;
import Main.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MacrosListener implements NativeKeyListener {
    public static MacrosListener macrosListener = new MacrosListener();
    private static Thread runner;

    private MacrosListener() {
    }

    private class MacroRunner implements Runnable {
        private Macro macro_;

        MacroRunner(Macro macro) {
            macro_ = macro;
        }

        @Override
        public void run() {
            try {
                macro_.runMacro();
            } catch (Exception ex) {
            } finally {
                Main.main.messages.setText(String.format("Macro '%s' ended ", macro_.getName()));
                macroRunning = false;
            }
        }
    }

    //map keys too macros using this key
    MacroListener actualRunning = null;
    Map<Integer, ArrayList<MacroListener>> listening = new HashMap<Integer, ArrayList<MacroListener>>();
    Integer actFirst;
    Integer actSec;
    volatile Boolean macroRunning = false;

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        var code = e.getKeyCode();
        if (!macroRunning) {
            ArrayList<MacroListener> list = listening.get(code);
            if (list == null)
                return;
            for (var listener : list)
                listener.KeyCheckReleased(code);
        } else {
            if (actSec == code || actFirst == code) {
                actualRunning.KeyCheckReleased(code);
            }
        }
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        var code = e.getKeyCode();
        if (!macroRunning) {
            ArrayList<MacroListener> list = listening.get(code);
            if (list == null)
                return;
            for (var listener : list)
                if (listener.KeyCheckPressed(code)) {
                    actualRunning = listener;
                    actualRunning.resetKeys();
                    actFirst = actualRunning.first.get();
                    actSec = actualRunning.second.get();
                    macroRunning = true;
                    runner = new Thread(new MacroRunner(actualRunning.macro_));
                    runner.start();
                }
        } else {
            if (actSec == code || actFirst == code)
                if (actualRunning.KeyCheckPressed(code)) {
                    runner.interrupt();
                    actualRunning.resetKeys();
                }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void startListening() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(MacrosListener.macrosListener);
    }

    public void stopListening() {
        GlobalScreen.removeNativeKeyListener(MacrosListener.macrosListener);
    }

    public void addMacro(Macro macro) {
        Integer first = macro.getFirstKey().get();
        Integer second = macro.getSecondtKey().get();
        var listener = new MacroListener(macro);
        register(listener, first);
        if (second != null)
            register(listener, second);
    }

    public void removeAllMacros() {
        listening.clear();
    }

    private void register(MacroListener ml, Integer key) {
        var array = listening.get(key);
        if (array != null) {
            array.add(ml);
        } else {
            var newArray = new ArrayList<MacroListener>();
            newArray.add(ml);
            listening.put(key, newArray);
        }
    }
}