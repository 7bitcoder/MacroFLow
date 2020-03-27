package ControlOutput;

import Instructions.Executor;

import java.awt.*;

public class Mouse {
    static public Robot robot;
    static Validator v = Validator.getVaidator();

    public Mouse() {
    }

    public static class MoveMouse implements Executor.Instruction {
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
}
