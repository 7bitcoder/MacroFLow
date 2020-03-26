package ControlOutput;

import Instructions.Executor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

public class System {
    static public Robot robot;
    static Validator v = Validator.getVaidator();

    public System(Robot r) {
        robot = r;
    }

    public static class Delay implements Executor.Instruction {
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

    public static class clipBoard implements Executor.Instruction {
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

    public static class Paste implements Executor.Instruction {
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
