package Keyboard;

import Parser.Execution;
import Parser.Validator;

import java.awt.Robot;

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
    
}