package ContrtolOutput;

import Instructions.Execution;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExecutableInstructions {
    static public Robot robot;
    static Validator v = Validator.getVaidator();

    public ExecutableInstructions(Robot r) {
        robot = r;
    }

    public static class MoveMouse implements Execution.Instruction {
        int arg0 = 0;
        int arg1 = 0;

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(2, args);
            arg0 = v.valNum(args, 1);
            arg1 = v.valNum(args, 2);
        }

        @Override
        public void run() {
            robot.mouseMove(arg0, arg1);
            //robot.delay(1);
        }
    }

    public static class Delay implements Execution.Instruction {
        int arg0 = 0;

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            arg0 = v.valNum(args, 1);
        }

        @Override
        public void run() {
            robot.delay(arg0);
        }
    }

    public static class pressKey implements Execution.Instruction {
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

    public static class releaseKey implements Execution.Instruction {
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

    public static class Write implements Execution.Instruction {
        ArrayList<Integer> listOfKeys = new ArrayList<Integer>();
        static Map<String, Integer> set = new HashMap<String, Integer>();

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var cnt = 0;
            for (char c : args[1].toCharArray()) {
                var code = KeyEvent.getExtendedKeyCodeForChar(c);
                if (KeyEvent.CHAR_UNDEFINED == code) {
                    Validator.error(1, Validator.kind.numeric, args[1], String.format("Could not find character {} on keyboard. Number of character in word: {}", c, cnt));
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

    public static class clipBoard implements Execution.Instruction {
        String toClipBoard;
        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            toClipBoard = args[1];

        }

        @Override
        public void run() {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = new StringSelection(toClipBoard);
            clipboard.setContents(transferable, null);
        }
    }

    public static class Paste implements Execution.Instruction {
        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(0, args);
        }

        @Override
        public void run() {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.delay(100);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
        }
    }
}
