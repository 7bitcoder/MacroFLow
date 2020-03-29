package ControlOutput;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;


public class Validator {
    public static class ParserExcetption extends Exception {
        public ParserExcetption(String msg) {
            super(msg);
        }
    }

    private static final Validator val = new Validator();

    private Validator() {
    }

    public static Validator getVaidator() {
        return val;
    }

    public static void valSize(String[] args, int min, int max) throws ParserExcetption {
        if (args.length < min + 1 || args.length > max + 1)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction %s should have %d-%d arguments", args[0], min, max));
    }

    public static void valSize(int size, String[] args) throws ParserExcetption {
        if (args.length != size + 1)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction %s should have %d arguments", args[0], size));
    }

    public static int valEqSize(String[] args, int... size) throws ParserExcetption {
        boolean found = false;
        for (var siz : size)
            if (args.length - 1 == siz)
                found = true;
        if (!found)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction %s should have %s arguments", args[0], Arrays.toString(size)));
        return args.length;
    }

    public static boolean valBool(String[] args, int index) throws ParserExcetption {
        String read = args[index];
        var res = Boolean.parseBoolean(read);
        if (res)
            return true;
        return valNum(args, index) != 0;
    }

    public static int valNum(String[] args, int index) throws ParserExcetption {
        String read = args[index];
        int res = 0;
        try {
            res = Integer.parseInt(read);
        } catch (NumberFormatException e) {
            error(index, kind.numeric, read, e.getMessage());
        }
        return res;
    }

    public static String valStr(String[] args, int index, Set<String> req) throws ParserExcetption {
        String read = args[index];
        if (req == null || req.isEmpty()) // only runtime checking
            return read;
        if (!req.contains(read)) {
            if (req.size() > 10)
                error(index, kind.string, read, "Argument value is not allowed. Too see allowed values see manual");
            else
                error(index, kind.string, read, String.format("Argument value is not allowed. Allowed values: %s", req.toString()));
        }
        return read;
    }

    public static String valStr(String[] args, int index) throws ParserExcetption {
        return valStr(args, index, null);
    }

    public static void valSyntax(String[] args, String syntaxElement, int index) throws ParserExcetption {
        String read = args[index];
        if (!read.equals(syntaxElement))
            error(index, kind.syntax, read, String.format("syntax element should be %s", syntaxElement));
    }

    public static String valGetVar(String[] args, int index, Map<String, Integer> variables) throws ParserExcetption {
        String read = args[index];
        var variable = variables.get(read);
        if (variable == null)
            error(index, kind.syntax, read, String.format("Could not find variable '%s'", read));
        return read;
    }

    public static enum kind {
        bool, string, numeric, syntax;
    }

    public static void error(int index, kind kin, String value, String msg) throws ParserExcetption {
        genericError(String.format("Could not parse %s element '%s'.Error: %s ", kin.name(), value, msg), index);
    }

    public static void genericError(String msg, int index) throws ParserExcetption {
        throw new ParserExcetption(String.format("Index: %d. %s", index, msg));
    }
}
