package ControlInput;

import Instructions.Macro;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MacrosListener implements NativeKeyListener {
    public static MacrosListener macrosListener = new MacrosListener();

    private MacrosListener() {
    }

    //map keys too macros using this key
    Map<Integer, ArrayList<MacroListener>> listening = new HashMap<Integer, ArrayList<MacroListener>>();
    volatile boolean macroRunning = false;

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        ArrayList<MacroListener> list = listening.get(e.getKeyCode());
        if (list == null)
            return;
        for (var listener : list)
            listener.KeyCheckReleased(e.getKeyCode());
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (macroRunning)
            return;
        ArrayList<MacroListener> list = listening.get(e.getKeyCode());
        if (list == null)
            return;
        for (var listener : list)
            if (listener.KeyCheckPressed(e.getKeyCode())) {
                macroRunning = true;
                listener.runMacro();
                macroRunning = false;
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