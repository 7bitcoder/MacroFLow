package ControlOutput;

import Instructions.Executor;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import static ControlOutput.System.Delay;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemTest {
    String[] spl(String str) {
        return str.split(" ");
    }

    Executor exec = new Executor();
    Executor.Instruction actualInstruction;

    String testException(String str) {

        try {
            actualInstruction.init(spl(str), exec);
        } catch (Validator.ParserExcetption parserExcetption) {
            return parserExcetption.getMessage();
        }
        return "";
    }

    @Test
    void delay() throws Validator.ParserExcetption {
        actualInstruction = new Delay();
        String msg = testException("delay wrongNumb");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay wrongNumb toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay 123");
        assertTrue(msg.isEmpty());
    }

    @Test
    void Var() throws Validator.ParserExcetption {
        actualInstruction = new System.Var();
        exec = new Executor();
        exec.variables.put("xddd", 0);
        exec.variables.put("syl", 0);
        exec.variables.put("dix", 0);

        //already defined
        String msg = testException("var xddd");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay ala wrongInteger");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay ala ads Toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("delay correct 123");
        assertTrue(msg.isEmpty());
    }

    @Test
    void Add() throws Validator.ParserExcetption {
        actualInstruction = new System.Add();
        exec = new Executor();
        exec.variables.put("xddd", 2);
        exec.variables.put("syl", 4);
        exec.variables.put("dix", 2);

        //not defined
        String msg = testException("add asd syk okd");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("add asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("add asd wrongNUmberOfParams");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("add");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("add xddd");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.variables.get("xddd") == 3);

        msg = testException("add xddd syl dix");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.variables.get("xddd") == 6);
    }

    @Test
    void Sub() throws Validator.ParserExcetption {
        actualInstruction = new System.Sub();
        exec = new Executor();
        exec.variables.put("xddd", 2);
        exec.variables.put("syl", 4);
        exec.variables.put("dix", 2);

        //not defined
        String msg = testException("sub asd syk okd");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub asd wrongNUmberOfParams");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub xddd");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.variables.get("xddd") == 1);

        msg = testException("sub xddd syl dix");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.variables.get("xddd") == 2);
    }

    @Test
    void Div() throws Validator.ParserExcetption {
        actualInstruction = new System.Div();
        exec = new Executor();
        exec.variables.put("xddd", 2);
        exec.variables.put("syl", 4);
        exec.variables.put("dix", 2);

        //not defined
        String msg = testException("sub asd syk okd");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub asd wrongNUmberOfParams");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("sub xddd syl dix");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.variables.get("xddd") == 2);
    }

    @Test
    void Label() throws Validator.ParserExcetption {
        actualInstruction = new System.Label();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", -1);

        //early request
        exec.setProgramCounter(7);
        String msg = testException("Label asd");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        //actual instruction index is 0
        assertTrue(exec.JumpLabels.get("asd") == 0);

        msg = testException("Label TooLOngName");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Label asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Label asd wrongNUmberOfParams");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Label");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Label newone");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.JumpLabels.get("newone") == 0);
    }

    @Test
    void Jump() throws Validator.ParserExcetption {
        actualInstruction = new System.Jmp();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", 6);

        //early request
        exec.setProgramCounter(7);
        String msg = testException("Jmp qwe");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        //actual instruction index is 0
        assertTrue(exec.JumpLabels.get("qwe") == -1);


        msg = testException("Jmp asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Jmp asd wrongNUmberOfParams");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Jmp");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Jmp asd");
        assertTrue(msg.isEmpty());
        actualInstruction.run();
        assertTrue(exec.getPc() == 5);
    }

    @Test
    void If() throws Validator.ParserExcetption {
        actualInstruction = new System.If();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", 6);
        exec.variables.put("var", 0);
        exec.variables.put("var2", 3);

        //var not defined
        String msg = testException("If qwe");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);
        //actual instruction index is 0


        msg = testException("If asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("If");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("If var");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("ifstate0"));
        msg = "";
        try {
            actualInstruction.run();
        } catch (Exception e) {
            msg = e.toString();
        }
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("If var2");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("ifstate1"));
        actualInstruction.run();

        msg = testException("If var2");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("ifstate2"));
        actualInstruction.run();
    }

    @Test
    void EndIf() throws Validator.ParserExcetption {
        actualInstruction = new System.EndIf();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", 6);
        exec.variables.put("var", 4);
        exec.variables.put("var2", 0);
        exec.brackets.push("ifstate0");
        exec.brackets.push("ifstate1");

        //var not defined
        String msg = testException("EndIf tomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("EndIf");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        assertTrue(exec.brackets.size() == 1);
        assertTrue(exec.JumpLabels.get("ifstate1") == 0);

        msg = testException("EndIf");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        assertTrue(exec.brackets.size() == 0);
        assertTrue(exec.JumpLabels.get("ifstate0") == 0);

        msg = testException("EndIf");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        exec.brackets.push("loopState1");
        msg = testException("EndIf");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);
    }

    @Test
    void Loop() throws Validator.ParserExcetption {
        actualInstruction = new System.Loop();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", 6);
        exec.variables.put("var", 0);
        exec.variables.put("var2", 0);

        //var not defined
        String msg = testException("Loop qwe");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);
        //actual instruction index is 0


        msg = testException("Loop asd asd asd toomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Loop");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Loop var");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("loestate0"));
        assertTrue(exec.JumpLabels.get("lostate0") == 0);
        msg = "";
        try {
            actualInstruction.run();
        } catch (Exception e) {
            msg = e.toString();
        }
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("Loop var2");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("loestate1"));
        assertTrue(exec.JumpLabels.get("lostate1") == 0);
        exec.JumpLabels.put("loestate1", 2);
        actualInstruction.run();
        assertTrue(exec.getPc() == 2);

        msg = testException("Loop var2");
        assertTrue(msg.isEmpty());
        assertTrue(exec.brackets.contains("loestate2"));
        assertTrue(exec.JumpLabels.get("lostate2") == 0);
        exec.JumpLabels.put("loestate2", 5);
        actualInstruction.run();
        assertTrue(exec.getPc() == 5);
    }

    @Test
    void EndLoop() throws Validator.ParserExcetption {
        actualInstruction = new System.EndLoop();
        exec = new Executor();
        exec.JumpLabels.put("xddd", 2);
        exec.JumpLabels.put("syl", 4);
        exec.JumpLabels.put("asd", 6);
        exec.variables.put("var", 4);
        exec.variables.put("var2", 0);
        exec.brackets.push("loestate0");
        exec.brackets.push("loestate1");

        //var not defined
        String msg = testException("EndLoop tomany");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        msg = testException("EndLoop");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        assertTrue(exec.brackets.size() == 1);
        assertTrue(exec.JumpLabels.get("loestate1") == 0);

        msg = testException("EndLoop");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        assertTrue(exec.brackets.size() == 0);
        assertTrue(exec.JumpLabels.get("loestate0") == 0);

        msg = testException("EndLoop");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        exec.brackets.push("ifState1");
        msg = testException("EndLoop");
        assertFalse(msg.isEmpty());
        java.lang.System.out.println(msg);

        exec.JumpLabels.put("loestate0", 2);
        exec.brackets.push("loestate0");
        msg = testException("EndLoop");
        assertTrue(msg.isEmpty());
        java.lang.System.out.println(msg);
        exec.JumpLabels.put("lostate0", 2);
        actualInstruction.run();
        assertTrue(exec.getPc() == 1);
    }

    @Test
    void IfStatements() throws Exception {
        exec = new Executor();
        String input = "var w 4\n" +
                "if w\n" +
                "var gg 0\n" +
                "if gg\n" +
                "var hh\n" +
                "if hh\n" +
                "var check 1\n" +
                "add check w gg\n" +
                "endif\n" +
                "endif\n" +
                "endif\n";
        Reader inputString = new StringReader(input);
        exec.loadInstructions();
        var msg = exec.readInstructions(new BufferedReader(inputString));
        assertTrue(msg == null);
        exec.run();
        assertTrue(exec.variables.get("check") == 1);

        exec = new Executor();
        input = "var w 1\n" +
                "if w\n" +
                "var gg 2\n" +
                "if gg\n" +
                "var hh 1\n" +
                "if hh\n" +
                "var check 1\n" +
                "add check w gg\n" +
                "endif\n" +
                "endif\n" +
                "endif\n";
        inputString = new StringReader(input);
        exec.loadInstructions();
        msg = exec.readInstructions(new BufferedReader(inputString));
        assertTrue(msg == null);
        exec.run();
        assertTrue(exec.variables.get("check") == 3);
    }

    @Test
    void LoopStatements() throws Exception {
        exec.loadInstructions();
        exec = new Executor();
        String input = "var index 5\n" +
                "loop index\n" +
                "sub index\n" +
                "endloop\n";
        Reader inputString = new StringReader(input);
        var msg = exec.readInstructions(new BufferedReader(inputString));
        assertTrue(msg == null);
        exec.run();
        assertTrue(exec.variables.get("index") == 0);

        exec = new Executor();
        input = "var index 5\n" +
                "var res\n" +
                "loop index\n" +
                "sub index\n" +
                "var offset 3\n" +
                "equal offset index offset\n" +
                "if offset\n" +
                "assign res offset\n" +
                "endif\n" +
                "endloop\n";
        inputString = new StringReader(input);
        msg = exec.readInstructions(new BufferedReader(inputString));
        assertTrue(msg == null);
        exec.run();
        assertTrue(exec.variables.get("res") == 1);
    }
}