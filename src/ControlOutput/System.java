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

    public System() {

    }

    public static class Delay implements Executor.Instruction {
        int arg0 = 0;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
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
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
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
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
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

    public static class Var implements Executor.Instruction {
        Executor macro_;
        int arg0 = 0;
        String name;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(args, 1, 2);
            name = v.valStr(args, 1);
            if (args.length == 3) {
                arg0 = v.valNum(args, 2);
            }
            var res = macro_.variables.get(name);
            if (res != null)
                Validator.error(1, Validator.kind.syntax, name, "Variable already defined");
            macro_.variables.put(name, arg0);
        }

        @Override
        public void run() {
            macro_.variables.put(name, arg0);
        }
    }

    public static class Add implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;
        boolean increment = true;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            var len = v.valEqSize(args, 1, 3);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            increment = true;
            if (len > 2) {
                increment = false;
                arg1 = v.valGetVar(args, 2, macro_.variables);
                arg2 = v.valGetVar(args, 3, macro_.variables);
            }
        }

        @Override
        public void run() {
            if (increment) {
                var var = macro_.variables.get(arg0);
                macro_.variables.put(arg0, ++var);
            } else {
                Integer var1 = macro_.variables.get(arg1);
                var var2 = macro_.variables.get(arg2);
                macro_.variables.put(arg0, var1 + var2);
            }
        }
    }

    public static class Sub implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;
        boolean decrement = true;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            var len = v.valEqSize(args, 1, 3);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            decrement = true;
            if (len > 2) {
                decrement = false;
                arg1 = v.valGetVar(args, 2, macro_.variables);
                arg2 = v.valGetVar(args, 3, macro_.variables);
            }
        }

        @Override
        public void run() {
            if (decrement) {
                var var = macro_.variables.get(arg0);
                macro_.variables.put(arg0, --var);
            } else {
                Integer var1 = macro_.variables.get(arg1);
                var var2 = macro_.variables.get(arg2);
                macro_.variables.put(arg0, var1 - var2);
            }
        }
    }

    public static class Mul implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(3, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
            arg2 = v.valGetVar(args, 3, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            var var2 = macro_.variables.get(arg2);
            macro_.variables.put(arg0, var1 * var2);
        }
    }

    public static class Div implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(3, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
            arg2 = v.valGetVar(args, 3, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            var var2 = macro_.variables.get(arg2);
            macro_.variables.put(arg0, var1 / var2);
        }
    }

    public static class Bigger implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(3, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
            arg2 = v.valGetVar(args, 3, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            var var2 = macro_.variables.get(arg2);
            macro_.variables.put(arg0, var1 > var2 ? 1 : 0);
        }
    }

    public static class Smaller implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(3, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
            arg2 = v.valGetVar(args, 3, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            var var2 = macro_.variables.get(arg2);
            macro_.variables.put(arg0, var1 < var2 ? 1 : 0);
        }
    }

    public static class Equal implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;
        String arg2;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(3, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
            arg2 = v.valGetVar(args, 3, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            var var2 = macro_.variables.get(arg2);
            macro_.variables.put(arg0, var1 == var2 ? 1 : 0);
        }
    }

    public static class Assign implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String arg1;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(2, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            arg1 = v.valGetVar(args, 2, macro_.variables);
        }

        @Override
        public void run() {
            var var1 = macro_.variables.get(arg1);
            macro_.variables.put(arg0, var1);
        }
    }

    public static class Label implements Executor.Instruction {
        Executor macro_;
        String arg0;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(1, args);
            arg0 = v.valStr(args, 1);
            if (arg0.length() > 6)
                Validator.error(1, Validator.kind.syntax, arg0, "Label should have less than 7 letters");
            var res = macro_.JumpLabels.get(arg0);
            if (res != null && res == -1) // requested to jump earlier
                ;//pass
            macro_.JumpLabels.put(arg0, macro_.getInstructionIndex());
        }

        @Override
        public void run() {
        }
    }

    public static class Jmp implements Executor.Instruction {
        Executor macro_;
        String arg0;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(1, args);
            arg0 = v.valStr(args, 1);
            macro_.JumpLabels.putIfAbsent(arg0, -1);

        }

        @Override
        public void run() {
            macro_.setProgramCounter(macro_.JumpLabels.get(arg0));
        }
    }

    public static class If implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String jumpLabel;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(1, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            jumpLabel = "ifstate" + macro_.bracketIndex++;
            macro_.brackets.push(jumpLabel);
        }

        @Override
        public void run() {
            if (macro_.variables.get(arg0) == 0) {
                macro_.setProgramCounter(macro_.JumpLabels.get(jumpLabel));
            }
        }
    }

    public static class EndIf implements Executor.Instruction {
        Executor macro_;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(0, args);
            String jumpLabel = null;
            try {
                jumpLabel = macro.brackets.pop();
            } catch (Exception e) {
                Validator.error(0, Validator.kind.syntax, args[0], "Too many endIf instructions in macro file");
            }
            if (!jumpLabel.startsWith("ifstate")) {
                Validator.error(0, Validator.kind.syntax, args[0], "Wrong closing bracket command");
            }
            macro_.JumpLabels.put(jumpLabel, macro.getInstructionIndex());
        }

        @Override
        public void run() {
        }
    }

    public static class Loop implements Executor.Instruction {
        Executor macro_;
        String arg0;
        String jumpLabel;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(1, args);
            arg0 = v.valGetVar(args, 1, macro_.variables);
            jumpLabel = "loestate" + macro_.bracketIndex;
            macro_.JumpLabels.put("lostate" + macro_.bracketIndex++, macro.getInstructionIndex()); //early
            macro_.brackets.push(jumpLabel);
        }

        @Override
        public void run() {
            if (macro_.variables.get(arg0) == 0) {
                macro_.setProgramCounter(macro_.JumpLabels.get(jumpLabel) + 1);
            }
        }
    }

    public static class EndLoop implements Executor.Instruction {
        Executor macro_;
        String jumpToLoop;

        @Override
        public void init(String[] args, Executor macro) throws Validator.ParserExcetption {
            macro_ = macro;
            v.valSize(0, args);
            String jumpLabel = null;
            try {
                jumpLabel = macro.brackets.pop();
            } catch (Exception e) {
                Validator.error(0, Validator.kind.syntax, args[0], "Too many endLoop instructions in macro file");
            }
            if (!jumpLabel.startsWith("loestate")) {
                Validator.error(0, Validator.kind.syntax, args[0], "Wrong closing bracket command");
            }
            int nmb = Integer.parseInt(jumpLabel.substring(8));
            jumpToLoop = "lostate" + nmb;
            macro_.JumpLabels.put(jumpLabel, macro.getInstructionIndex());
        }

        @Override
        public void run() {
            macro_.setProgramCounter(macro_.JumpLabels.get(jumpToLoop));
        }
    }
}