package ControlOutput;

import Instructions.Executor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    static public Robot robot;
    static Validator v = Validator.getVaidator();

    public Keyboard(Robot r) {
        robot = r;
    }

    public static class pressKey implements Executor.Instruction {
        int arg0;
        static Map<String, Integer> set = new HashMap<String, Integer>();

        {
            Field[] fields = KeyEvent.class.getDeclaredFields();
            try {
                for (Field f : fields) {
                    if (Modifier.isStatic(f.getModifiers())) {
                        var name = f.getName();
                        var index = name.indexOf("VK_");
                        if (index != -1) {
                            var keyCode = f.getInt(name.substring(index));
                            set.put(KeyEvent.getKeyText(keyCode).toLowerCase(), keyCode);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var str = args[1].toLowerCase();
            if (str.length() == 1) {
                arg0 = KeyEvent.getExtendedKeyCodeForChar(str.charAt(0));
                if (KeyEvent.CHAR_UNDEFINED == arg0)
                    Validator.error(1, Validator.kind.numeric, str, "Could not find character on keyboard");
            } else {
                v.valStr(args, 1, set.keySet());
                arg0 = set.get(str);
            }
        }

        @Override
        public void run() {
            robot.keyPress(arg0);
        }
    }

    public static class releaseKey implements Executor.Instruction {
        int arg0;

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var str = args[1].toLowerCase();
            if (str.length() == 1) {
                arg0 = KeyEvent.getExtendedKeyCodeForChar(str.charAt(0));
                if (KeyEvent.CHAR_UNDEFINED == arg0)
                    Validator.error(1, Validator.kind.numeric, str, "Could not find character on keyboard");
            } else {
                v.valStr(args, 1, pressKey.set.keySet());
                arg0 = pressKey.set.get(str);
            }
        }

        @Override
        public void run() {
            robot.keyRelease(arg0);
        }
    }

    public static class Write implements Executor.Instruction {
        ArrayList<Integer> listOfKeys = new ArrayList<Integer>();
        static Map<String, Integer> set = new HashMap<String, Integer>();

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var cnt = 0;
            for (char c : args[1].toCharArray()) {
                var code = KeyEvent.getExtendedKeyCodeForChar(c);
                if (KeyEvent.CHAR_UNDEFINED == code) {
                    Validator.error(1, Validator.kind.numeric, args[1], String.format("Could not find character %c on keyboard. Number of character in word: %s", c, cnt));
                }
                //todo check if  code is pressable
                //robot.keyRelease(code);
                listOfKeys.add(code);
                cnt++;
            }
        }

        @Override
        public void run() {
            for (var keyCode : listOfKeys) {
                robot.keyPress(keyCode);
                robot.delay(1);
                robot.keyRelease(keyCode);
            }
        }
    }


}
