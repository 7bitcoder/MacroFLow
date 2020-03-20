package ContrtolOutput;

import Parser.Execution;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        String arg0;
        static Map<String, Integer> set = new HashMap<String, Integer>();

        {
            Field[] fields = KeyEvent.class.getDeclaredFields();
            try {
                for (Field f : fields) {
                    if (Modifier.isStatic(f.getModifiers())) {
                        var name = f.getName();
                        var index = name.indexOf("VK_");
                        if (index != -1)
                            set.put(name.substring(index + 3).toLowerCase(), f.getInt(name));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            var gg = 1;
        }

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            v.valSize(1, args);
            arg0 = v.valStr(args, 1, set.keySet());
        }

        @Override
        public void run() {
            robot.keyPress(set.get(arg0));
        }
    }

    public static class writeWords implements Execution.Instruction {
        String arg0;
        static Map<String, Integer> set = new HashMap<String, Integer>();

        @Override
        public void init(String[] args) throws Validator.ParserExcetption {
            // unlimited
            // v.valSize(1, args);
            arg0 = v.valStr(args, 1, set.keySet());
        }

        @Override
        public void run() {
            robot.keyPress(set.get(arg0));
        }
    }

}