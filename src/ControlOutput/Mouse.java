package ControlOutput;

import Instructions.Executor;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

public class Mouse {
    static public Robot robot;
    static Validator v = Validator.getVaidator();

    public Mouse() {
    }

    public static class MoveMouse implements Executor.Instruction {
        String arg0;
        String arg1;
        Executor macro_;
        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;

            v.valSize(2, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
        }

        @Override
        public void run() {
            int var1 = macro_.variables.get(arg0);
            int var2 = macro_.variables.get(arg1);
            robot.mouseMove(var1, var2);
        }
    }

    public static class mousePress implements Executor.Instruction {
        static final Map<String , Integer> keys = new HashMap<String , Integer>() {{
            put("left", InputEvent.BUTTON1_MASK);
            put("right", InputEvent.BUTTON2_MASK);
        }};
        int arg0 = 0;
        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var value = v.valStr( args, 1, keys.keySet());
            arg0 = keys.get(value);
        }

        @Override
        public void run() {
            robot.mousePress(arg0);
            //robot.delay(1);
        }
    }

    public static class mouseRelease implements Executor.Instruction {
        static final Map<String , Integer> keys = new HashMap<String , Integer>() {{
            put("left", InputEvent.BUTTON1_MASK);
            put("right", InputEvent.BUTTON2_MASK);
        }};
        int arg0 = 0;
        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            v.valSize(1, args);
            var value = v.valStr( args, 1, keys.keySet());
            arg0 = keys.get(value);
        }

        @Override
        public void run() {
            robot.mouseRelease(arg0);
            //robot.delay(1);
        }
    }
}
